package com.thefirstlineofcode.basalt.oxm.parsers.im;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.parsers.core.stanza.StanzaParser;
import com.thefirstlineofcode.basalt.oxm.parsing.ElementParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.oxm.parsing.ParsingUtils;
import com.thefirstlineofcode.basalt.xmpp.HandyUtils;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Presence;

public class PresenceParserFactory implements IParserFactory<Presence> {
	private static final String VALUE_SHOW_CHAT = "chat";
	private static final String VALUE_SHOW_AWAY = "away";
	private static final String VALUE_SHOW_DND = "dnd";
	private static final String VALUE_SHOW_XA = "xa";
	
	private static final String PATH_PRESENCE_STATUS_ITEM = "/status";
    private static final String PATH_PRESENCE_SHOW = "/show";
    private static final String PATH_PRESENCE_PRIORITY = "/priority";
    
    private static final Object KEY_LANG_TEXT = new Object();
	
	@Override
	public Protocol getProtocol() {
		return Presence.PROTOCOL;
	}

	@Override
	public IParser<Presence> create() {
		return new PresenceParser();
	}

	private class PresenceParser extends StanzaParser<Presence> {

		@Override
		public Presence createObject() {
			return new Presence();
		}

		@Override
		protected IElementParser<Presence> doGetElementParser(IParsingPath parsingPath) {
			if (parsingPath.match(PATH_PRESENCE_STATUS_ITEM)) {
				return new IElementParser<Presence>() {
					@Override
					public void processText(IParsingContext<Presence> context, Value<?> text) {
						if (HandyUtils.isBlank(text.toString())) {
							throw new ProtocolException(new BadRequest("Status text is null."));
						}
						
						LangText langText = context.removeAttribute(KEY_LANG_TEXT);
						if (langText == null) {
							throw new RuntimeException("Null LangText???");
						}
						
						langText.setText(text.toString());
					}
					
					@Override
					public void processAttributes(IParsingContext<Presence> context, List<Attribute> attributes) {
						LangText langText = new LangText(null, ParsingUtils.processLangTextAttributes(attributes));
						context.getObject().getStatuses().add(langText);
						context.setAttribute(KEY_LANG_TEXT, langText);
					}
				};
			} else if (parsingPath.match(PATH_PRESENCE_SHOW)) {
				return new ElementParserAdaptor<Presence>() {
					@Override
					public void processText(IParsingContext<Presence> context, Value<?> text) {
						if (VALUE_SHOW_CHAT.equals(text.toString())) {
							context.getObject().setShow(Presence.Show.CHAT);
						} else if (VALUE_SHOW_AWAY.equals(text.toString())) {
							context.getObject().setShow(Presence.Show.AWAY);
						} else if (VALUE_SHOW_DND.equals(text.toString())) {
							context.getObject().setShow(Presence.Show.DND);
						} else if (VALUE_SHOW_XA.equals(text.toString())) {
							context.getObject().setShow(Presence.Show.XA);
						} else {
							throw new ProtocolException(new BadRequest(String.format("Invalid presence show '%s'.", text.toString())));
						}
					}
				};
			} else if (parsingPath.match(PATH_PRESENCE_PRIORITY)) {
				return new ElementParserAdaptor<Presence>() {
					@Override
					public void processText(IParsingContext<Presence> context, Value<?> text) {
						if (context.getObject().getPriority() != null) {
							throw new ProtocolException(new BadRequest("Reduplicate 'priority'."));
						}
						
						try {
							context.getObject().setPriority(Integer.valueOf(text.toString()));
						} catch (NumberFormatException e) {
							throw new ProtocolException(new BadRequest("Value of 'priority' must be an integer."));
						}
						
						// (rfc3921 2.2.2.3)
						if (context.getObject().getPriority() < -128 || context.getObject().getPriority() > 127) {
							throw new ProtocolException(new BadRequest("Value of 'priority' must be between -128 and 127."));
						}
					}
				};
			} else {
				return null;
			}
		}

		@Override
		protected void processType(IParsingContext<Presence> context, String value) {
			try {
				context.getObject().setType(Presence.Type.valueOf(value.toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new ProtocolException(new BadRequest(String.format("Invalid presence type '%s'.", value)));
			}
		}
	}
}
