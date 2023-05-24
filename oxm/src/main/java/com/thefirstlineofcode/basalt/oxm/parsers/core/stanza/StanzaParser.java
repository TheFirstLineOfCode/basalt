package com.thefirstlineofcode.basalt.oxm.parsers.core.stanza;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.parsing.ElementParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;

public abstract class StanzaParser<T extends Stanza> implements IParser<T> {
	private static final String VALUE_TYPE_ERROR = "error";
	private static final String PATH_ROOT = "/";
	private static final String ATTRIBUTE_NAME_ID = "id";
	private static final String ATTRIBUTE_NAME_TYPE = "type";
	private static final String ATTRIBUTE_NAME_TO = "to";
	private static final String ATTRIBUTE_NAME_FROM = "from";
	
	public static final Object KEY_ERROR = new Object();

	@Override
	public IElementParser<T> getElementParser(IParsingPath parsingPath) {
		if (parsingPath.match(PATH_ROOT)) {
			return new ElementParserAdaptor<T>() {
				@Override
				public void processAttributes(IParsingContext<T> context, List<Attribute> attributes) {
					T stanza = (T) context.getObject();
					boolean isError = false;
					for (Attribute attribute : attributes) {
						if (LangText.NAME_LANG_TEXT.equals(attribute.getName())) {
							stanza.setLang(attribute.getValue().toString());
						} else if (ATTRIBUTE_NAME_FROM.equals(attribute.getName())) {
							stanza.setFrom(parseJid(attribute.getValue().toString()));
						} else if (ATTRIBUTE_NAME_TO.equals(attribute.getName())) {
							stanza.setTo(parseJid(attribute.getValue().toString()));
						} else if (ATTRIBUTE_NAME_TYPE.equals(attribute.getName())) {
							String sType = attribute.getValue().toString();
							if (VALUE_TYPE_ERROR.equals(sType)) {
								isError = true;
								continue;
							}
							
							processType(context, sType);
						} else if (ATTRIBUTE_NAME_ID.equals(attribute.getName())) {
							stanza.setId(attribute.getValue().toString());
						} else {
							throw new ProtocolException(new BadRequest(String.format(
									"Invalid stanza attribute: %s.", attribute.getName())));
						}
					}
					
					if (isError) {
						context.setAttribute(KEY_ERROR, KEY_ERROR);
					}
				}
				
				@Override
				public void processText(IParsingContext<T> context, Value<?> text) {
					super.processText(context, text);
					
					if (context.getObject() instanceof Iq) {
						Iq iq = (Iq)context.getObject();
						
						if (iq.getId() == null) {
							throw new ProtocolException(new BadRequest("Null ID."));
						}
						
						if (Iq.Type.RESULT != iq.getType() && iq.getObject() == null &&
								context.getAttribute(IParsingFactory.KEY_FLAWS) == null) {
							throw new ProtocolException(new BadRequest("No embedded object found."));
						}
					}
				}
			};
		}
		
		return doGetElementParser(parsingPath);
	}
	
	private JabberId parseJid(String value) {
		return JabberId.parse(value);
	}
	
	@Override
	public void processEmbeddedObject(IParsingContext<T> context, Protocol protocol, Object embedded) {
		((Stanza)context.getObject()).getObjects().add(embedded);
		((Stanza)context.getObject()).getObjectProtocols().put(embedded.getClass(), protocol);
	}
	
	protected abstract IElementParser<T> doGetElementParser(IParsingPath parsingPath);
	protected abstract void processType(IParsingContext<T> context, String value);
}
