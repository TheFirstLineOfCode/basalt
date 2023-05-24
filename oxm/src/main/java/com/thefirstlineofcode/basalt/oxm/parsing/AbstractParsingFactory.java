package com.thefirstlineofcode.basalt.oxm.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError.Kind;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Features;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.BadFormat;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Message;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Presence;

public abstract class AbstractParsingFactory implements IParsingFactory {
	protected ConcurrentMap<ProtocolChain, IParserFactory<?>> factories;
	protected ConcurrentMap<ProtocolChain, ConcurrentMap<String, IElementParser<?>>> elementParsersCache;
	
	public AbstractParsingFactory() {
		factories = new ConcurrentHashMap<>();
		elementParsersCache = new ConcurrentHashMap<>();
	}

	protected ProtocolException convertParsingExceptionToProtocolException(ParsingException e, String message) {
		ProtocolException pe = null;
		
		if (e.getCause() instanceof ProtocolException) {
			pe = (ProtocolException)e.getCause();
		} else {
			pe = new ProtocolException(new BadRequest(), e.getCause());
		}
		
		StanzaError error = (StanzaError)pe.getError();
		Stanza stanza = e.getStanza();
		if (stanza != null) {
			error.setId(stanza.getId());
			if (stanza instanceof Presence) {
				error.setKind(StanzaError.Kind.PRESENCE);
			} else if (stanza instanceof Message) {
				error.setKind(StanzaError.Kind.MESSAGE);
			} else {
				// stanza instanceof Iq
				error.setKind(StanzaError.Kind.IQ);
			}
		}
		
		return pe;
	}

	protected ProtocolException newBadMessageException(Exception e, boolean stream, String message) {
		if (stream) {
			return new ProtocolException(new BadFormat(), e);
		} else {
			StanzaError.Kind kind = guessStanzaKind(message);
			if (kind == null)
				throw new ProtocolException(new BadFormat());
			
			ProtocolException pe = new ProtocolException(new BadRequest(), e);
			((StanzaError)pe.getError()).setKind(kind);
			
			return pe;
		}
	}

	protected boolean isStanza(Protocol protocol) {
		return protocol.equals(Iq.PROTOCOL) || protocol.equals(Message.PROTOCOL) || protocol.equals(Presence.PROTOCOL);
	}

	protected ProtocolChain createProtocolChain(IParsingContext<?> parentContext, Protocol protocol) {
		if (isNullParsingContext(parentContext)) {
			return ProtocolChain.first(protocol);
		} else {
			return parentContext.getProtocolChain().clone().next(protocol);
		}
	}

	@Override
	public void register(ProtocolChain chain, IParserFactory<?> parserFactory) {
		factories.put(chain, parserFactory);
	}

	@Override
	public void unregister(ProtocolChain chain) {
		elementParsersCache.remove(chain);
		factories.remove(chain);
	}
	
	@Override
	public boolean isRegistered(ProtocolChain protocolChain) {
		return factories.containsKey(protocolChain);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> IParser<T> getParser(ProtocolChain protocolChain) {
		IParserFactory<?> factory = factories.get(protocolChain);
		if (factory == null) {
			throw new RuntimeException(String.format("Parser not found. Protocol chain: %s.", protocolChain));
		}
		
		IParser<?> parser = factory.create();
		return (IParser<T>)parser;
	}
	
	protected Stanza getStanza(IParsingContext<?> context) {
		if (context == null)
			return null;
		
		if (context.getObject() == null)
			return null;
		
		if (context.getObject() instanceof Stanza) {
			return ((Stanza)context.getObject());
		}
		
		return null;
	}
	
	protected Object processFlaws(IParsingContext<?> parentContext, ParsingContext<?> context) {
		if (!isNullParsingContext(parentContext)) {
			List<ProtocolChain> flaws = context.getAttribute(KEY_FLAWS);
			if (flaws != null) {
				context.removeAttribute(KEY_FLAWS);
				
				List<ProtocolChain> parentFlaws = parentContext.getAttribute(KEY_FLAWS);
				if (parentFlaws == null) {
					parentFlaws = new ArrayList<>();
					parentContext.setAttribute(KEY_FLAWS, parentFlaws);
				}
				
				parentFlaws.addAll(flaws);
			}
		} else {
			List<ProtocolChain> flaws = context.getAttribute(KEY_FLAWS);
			
			if (flaws != null) {
				Object object = context.getObject();
				if (object instanceof Stanza) {
					Stanza stanza = (Stanza)object;
					stanza.getObjects().add(new FlawedProtocolObject(flaws));
					stanza.getObjectProtocols().put(FlawedProtocolObject.class, FlawedProtocolObject.PROTOCOL);
				} else if (object instanceof Features) {
					((Features)object).getFeatures().add(new FlawedProtocolFeature(flaws));
				} else {
					throw new RuntimeException("???Is a flawed object not in stanza or features?");
				}
				
				context.removeAttribute(KEY_FLAWS);
				
				return object;
			}
		}
		
		return null;
	}
	
	protected boolean isNullParsingContext(IParsingContext<?> context) {
		return context == null;
	}

	@SuppressWarnings("unchecked")
	protected <T> IElementParser<T> getElementParser(IParser<T> parser, IParsingContext<?> context) {
		ConcurrentMap<String, IElementParser<?>> elementParsers = elementParsersCache.get(context.getProtocolChain());
		if (elementParsers == null) {
			elementParsers = new ConcurrentHashMap<>();
			ConcurrentMap<String, IElementParser<?>> existed = elementParsersCache.putIfAbsent(context.getProtocolChain(), elementParsers);
			if (existed != null) {
				elementParsers = existed;
			}
		}
		
		IElementParser<?> elementParser = elementParsers.get(context.getParsingPath().toString());
		if (elementParser == null) {
			elementParser = parser.getElementParser(context.getParsingPath());
			if (elementParser != null) {
				IElementParser<?> existed = elementParsers.putIfAbsent(context.getParsingPath().toString(), elementParser);
				if (existed != null) {
					elementParser = existed;
				}
			}
		}
		
		return (IElementParser<T>)elementParser;
	}
	
	protected abstract Kind guessStanzaKind(String message);

}