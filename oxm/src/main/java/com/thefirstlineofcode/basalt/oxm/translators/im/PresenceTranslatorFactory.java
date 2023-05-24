package com.thefirstlineofcode.basalt.oxm.translators.im;

import com.thefirstlineofcode.basalt.oxm.Attributes;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.translators.core.stanza.StanzaTranslator;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Presence;

public class PresenceTranslatorFactory implements ITranslatorFactory<Presence> {
	private static final ITranslator<Presence> translator = new PresenceTranslator();

	@Override
	public Class<Presence> getType() {
		return Presence.class;
	}

	@Override
	public ITranslator<Presence> create() {
		return translator;
	}
	
	private static class PresenceTranslator extends StanzaTranslator<Presence> {

		private static final String STRING_SHOW = "show";
		private static final String STRING_STATUS = "status";
		private static final String STRING_PRIORITY = "priority";

		@Override
		public Protocol getProtocol() {
			return Presence.PROTOCOL;
		}

		@Override
		protected String getType(Presence presence) {
			if (presence.getType() == null)
				return null;
			
			return presence.getType().toString().toLowerCase();
		}

		@Override
		protected void translateSpecific(Presence presence, IProtocolWriter writer,
					ITranslatingFactory translatingFactory) {
			if (presence.getShow() != null) {
				writer.writeTextOnly(STRING_SHOW, presence.getShow().toString().toLowerCase());
			}
			
			if (presence.getStatuses() != null && presence.getStatuses().size() > 0) {
				for (LangText status : presence.getStatuses()) {
					writer.writeElementBegin(STRING_STATUS);
					
					if (status.getLang() != null)
						writer.writeAttributes(new Attributes(LangText.PREFIX_LANG_TEXT,
								LangText.LOCAL_NAME_LANG_TEXT, status.getLang()).get());
					
					writer.writeText(status.getText());
					
					writer.writeElementEnd();
				}
			}
			
			if (presence.getPriority() != null) {
				writer.writeTextOnly(STRING_PRIORITY, Value.create(presence.getPriority()));
			}
			
		}
		
	}

}
