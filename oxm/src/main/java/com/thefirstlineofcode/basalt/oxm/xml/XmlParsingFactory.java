package com.thefirstlineofcode.basalt.oxm.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.parsers.core.stanza.StanzaParser;
import com.thefirstlineofcode.basalt.oxm.parsing.AbstractParsingFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.oxm.parsing.ParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.ParsingException;
import com.thefirstlineofcode.basalt.oxm.parsing.ParsingUtils;
import com.thefirstlineofcode.basalt.oxm.xml.parsers.error.StanzaErrorParser;
import com.thefirstlineofcode.basalt.xmpp.core.IError;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.ServiceUnavailable;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError.Kind;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Features;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Stream;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.BadFormat;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.UnsupportedStanzaType;

public class XmlParsingFactory extends AbstractParsingFactory {
	private static final String LOCAL_NAME_WILDCARD_MATCH = "*";
	
	private IXmlStreamReaderFactory streamReaderFactory;
	
	public XmlParsingFactory() {
		streamReaderFactory = createXmlStreamReaderFactory();
	}

	protected IXmlStreamReaderFactory createXmlStreamReaderFactory() {
		return new XmlStreamReaderFactory();
	}
	
	@Override
	public Object parse(String message) {
		return parse(message, false);
	}

	@Override
	public Object parse(String message, boolean stream) {
		try {
			message = message.trim();
			
			StringBuilder sb = new StringBuilder();
			
			boolean isStreamMessage = false;
			if (message.startsWith("<stream:stream")) {
				sb.append(message).append("</stream:stream>");
				isStreamMessage = true;
			} else if (message.equals("</stream:stream>")) {
				sb.append("<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\"><close/></stream:stream>");
				isStreamMessage = true;
			} else {
				sb.append("<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\">");
				sb.append(message);
				sb.append("</stream:stream>");
			}
			
			Object object = doParse(sb.toString(), stream, isStreamMessage);
			if (object instanceof Stanza) {
				((Stanza)object).setOriginalMessage(message);
			}
			
			return object;
		} catch (ProtocolException e) {
			IError error = e.getError();
			if (error instanceof StanzaError) {
				((StanzaError)error).setOriginalMessage(message);
			}
			
			throw e;
		}
	}

	private Object doParse(String message, boolean stream, boolean isStreamMessage) {
		XMLStreamReader reader = null;
		try {
			reader = this.createXmlStreamReader(message);
			
			if (!isStreamMessage) {
				reader.next();
			}
			
			Object obj = parseProtocol(reader, message);
			
			if (!isStreamMessage) {
				reader.next();
			}
			
			if (reader.next() != XMLStreamConstants.END_DOCUMENT) {
				throw new BadMessageException("End document expected.");
			}
			
			return obj;
		} catch (ParsingException e) {
			if (stream) {
				throw new ProtocolException(new BadFormat(), e.getCause());
			}
			
			throw convertParsingExceptionToProtocolException(e, message);
		} catch (BadMessageException e) {
			throw newBadMessageException(e, stream, message);
		} catch (XMLStreamException e) {
			throw newBadMessageException(new BadMessageException(String.format("Bad xml format: %d.",
					e.getLocation().getCharacterOffset()), e), stream, message);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException e) {
					// ignore
				}
			}
		}
	}
	
	private <T> Object parseProtocol(XMLStreamReader reader, String message) throws XMLStreamException {
		int eventType = reader.next();
		if (eventType != XMLStreamConstants.START_ELEMENT) {
			throw new BadMessageException("No element start tag.");
		}
		
		return parseProtocol(reader, new MessageCarrierParsingContext(message));
	}
	
	private class MessageCarrierParsingContext implements IParsingContext<Object> {
		private String message;
		
		public MessageCarrierParsingContext(String message) {
			this.message = message;
		}

		@Override
		public ProtocolChain getProtocolChain() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IParsingPath getParsingPath() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Object getObject() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttribute(Object key, Object value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <K> K getAttribute(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public <K> K removeAttribute(Object key) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMessage(String message) {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getMessage() {
			return message;
		}
		
	}
	
	
	protected ProtocolChain createProtocolChain(IParsingContext<?> parsingContext, Protocol protocol) {
		if (parsingContext instanceof MessageCarrierParsingContext) {
			return ProtocolChain.first(protocol);
		} else {
			return parsingContext.getProtocolChain().clone().next(protocol);
		}
	}

	private <T> Object parseProtocol(XMLStreamReader reader, IParsingContext<?> parentContext) throws XMLStreamException {
		Protocol protocol = new Protocol(reader.getNamespaceURI(), reader.getLocalName());
		ProtocolChain real = createProtocolChain(parentContext, protocol);
		
		IParser<T> parser = null;
		if (factories.containsKey(real)) {
			parser = getParser(real);
		} else {
			ProtocolChain wildcardMatch = createProtocolChain(parentContext, new Protocol(
					protocol.getNamespace(), LOCAL_NAME_WILDCARD_MATCH));
			
			if (factories.containsKey(wildcardMatch)) {
				parser = getParser(wildcardMatch);
			} else {
				// unknown namespace
				try {
					processUnknownNamespace(reader, parentContext, protocol);
					return null;
				} catch (XMLStreamException e) {
					throw ParsingUtils.newParsingException(reader, e.getMessage());
				}
			}
		}
		
		ParsingContext<T> context = new ParsingContext<>(real, parser.createObject());
		context.setMessage(parentContext.getMessage());
		
		StanzaError error;
		try {
			error = parseElement(reader, parser, context);
		} catch (Exception e) {
			Stanza stanza = getStanza(context);
			if (stanza == null) {
				if (e instanceof XMLStreamException) {
					throw (XMLStreamException)e;
				} else { // (e instanceof RuntimeException)
					throw (RuntimeException)e;
				}
			} else {
				throw new ParsingException(e, stanza);
			}
		}
		
		if (error != null) {
			return error;
		} else {
			Object flawedObject = processFlaws(parentContext, context);
			
			if (flawedObject != null)
				return flawedObject;
			
			Object object = context.getObject();
			if (object instanceof Stream) {
				((Stream)object).setDefaultNamespace(reader.getNamespaceURI(""));
			}
			
			return object;
		}
	}
	
	@Override
	protected boolean isNullParsingContext(IParsingContext<?> parentContext) {
		return parentContext instanceof MessageCarrierParsingContext;
	}
	
	private <T> StanzaError parseElement(XMLStreamReader reader, IParser<T> parser,
				ParsingContext<T> context) throws XMLStreamException {
		IElementParser<T> elementParser = getElementParser(parser, context);
		
		if (elementParser == null) {
			throw new BadMessageException(String.format("Element %s not allowed be here: %s.",
					context.getParsingPath(), context));
		}
		
		List<Attribute> attributes = readAttributes(reader);
		StanzaError error = processElementAttributesAndParseError(reader, context, elementParser, attributes);
		if (error != null)
			return error;
		
		StringBuilder text = null;
		while (true) {
			int eventType = reader.next();
			if (eventType == XMLStreamConstants.CHARACTERS || eventType == XMLStreamConstants.CDATA) {
				text = ParsingUtils.appendText(reader, text);
			} else if (eventType == XMLStreamConstants.END_ELEMENT) {
				elementParser.processText(context, text == null ? null : Value.create(text.toString()));
				break;
			} else if (eventType == XMLStreamConstants.START_ELEMENT) {
				String namespace = reader.getNamespaceURI();
				String localName = reader.getLocalName();
				int lastProtocolIndex = context.getProtocolChain().size() - 1;
				String parentNamespace = context.getProtocolChain().get(lastProtocolIndex).getNamespace();
				if (namespace != null && !"".equals(namespace) && !namespace.equals(parentNamespace)) {
					Object object = parseProtocol(reader, context);
					if (object != null) {
						parser.processEmbeddedObject(context, new Protocol(namespace, localName), object);
					}
				} else {
					context.enter(reader.getLocalName());
					parseElement(reader, parser, context);
					context.exit();
				}				
			} else {
				throw ParsingUtils.newParsingException(reader, "Unsupported XML event type.");
			}
		}
		
		return null;
	}
	
	private <T> StanzaError processElementAttributesAndParseError(XMLStreamReader reader, ParsingContext<T> context,
			IElementParser<T> elementParser, List<Attribute> attributes) throws XMLStreamException {
		elementParser.processAttributes(context, attributes);
		if (context.removeAttribute(StanzaParser.KEY_ERROR) == StanzaParser.KEY_ERROR) {
			// stanza error is special, so we process it separately
			if (!(context.getObject() instanceof Stanza) && context.getObject() instanceof StanzaError) {
				throw new RuntimeException("Found error. But need a Presence, Message or Iq instance.");
			}
			
			StanzaErrorParser errorParser = new StanzaErrorParser();
			return errorParser.parse(this, context, reader);
		}
		
		return null;
	}
	
	private List<Attribute> readAttributes(XMLStreamReader reader) {
		if (reader.getAttributeCount() == 0) {
			return new ArrayList<>();
		}
		
		List<Attribute> attributes = new ArrayList<>();
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			String prefix = reader.getAttributePrefix(i);
			if ("".equals(prefix)) {
				prefix = null;
			}
			
			attributes.add(new Attribute(prefix, reader.getAttributeLocalName(i),
					reader.getAttributeValue(i)));
		}
		
		return attributes;
	}

	private void processUnknownNamespace(XMLStreamReader reader, IParsingContext<?> parentContext,
				Protocol protocol) throws XMLStreamException {
		if (parentContext == null || (parentContext instanceof MessageCarrierParsingContext)) {
			throw new ProtocolException(new UnsupportedStanzaType(String.format("Unsupported stanza type: %s.", protocol.getLocalName())));
		}
		
		Protocol topLevelProtocol = parentContext.getProtocolChain().get(0);
		if (!isStanza(topLevelProtocol) && !isFeatures(topLevelProtocol)) {
			throw new ProtocolException(new ServiceUnavailable(String.format("Unsupported protocol: %s.", protocol)));
		}
		
		int level = 1;
		while (true) {
			int eventType = reader.next();
			if (eventType == XMLStreamConstants.START_ELEMENT) {
				level++;
			} else if (eventType == XMLStreamConstants.END_ELEMENT) {
				level--;
				
				if (level == 0) {
					String localName = reader.getLocalName();
					String namespace = reader.getNamespaceURI();
					
					if (new Protocol(namespace, localName).equals(protocol)) {
						List<ProtocolChain> flaws = parentContext.getAttribute(KEY_FLAWS);
						if (flaws == null)
							flaws = new ArrayList<>();
						
						flaws.add(parentContext.getProtocolChain().next(protocol));
						
						parentContext.setAttribute(KEY_FLAWS, flaws);
						break;
					}
				}
			} else if (eventType == XMLStreamConstants.END_DOCUMENT) {
				throw new BadMessageException("Unexpected end of document when processing unkown namespace.");
			} else {
				continue;
			}
		}
	}
	
	private boolean isFeatures(Protocol protocol) {
		return protocol.equals(Features.PROTOCOL);
	}

	@Override
	protected Kind guessStanzaKind(String message) {
		if (message.startsWith("<stream:stream xmlns:stream=\"http://etherx.jabber.org/streams\">"))
			message = message.substring(63);
		if (message.endsWith("</stream:stream>"))
			message = message.substring(0, message.length() - 16);
		
		int i = 0;
		
		if (message.charAt(i) != '<')
			return null;
		i++;
		
		StanzaError.Kind kind;
		
		if ((message.length() > (i + 2)) && "iq".equals(message.substring(i, i + 2))) {
			kind = StanzaError.Kind.IQ;
			i += 2;
		} else if ((message.length() > (i + 8)) && "presence".equals(message.substring(i, i + 8))) {
			kind = StanzaError.Kind.PRESENCE;
			i += 8;
		} else if ((message.length() > (i + 7)) && "message".equals(message.substring(i, i + 7))) {
			kind =StanzaError.Kind.MESSAGE;
			i += 7;
		} else {
			return null;
		}
		
		if (message.charAt(i) != ' ')
			return null;
		
		return kind;
	}
	
	protected XMLStreamReader createXmlStreamReader(String message)throws XMLStreamException {
		return streamReaderFactory.createXmlStreamReader(message);
	}

}
