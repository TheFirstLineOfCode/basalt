package com.thefirstlineofcode.basalt.oxm.translators.core.stanza;

import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;

public class IqTranslatorFactory implements ITranslatorFactory<Iq> {
	private static final ITranslator<Iq> translator = new IqTranslator();

	@Override
	public Class<Iq> getType() {
		return Iq.class;
	}

	@Override
	public ITranslator<Iq> create() {
		return translator;
	}
	
	private static class IqTranslator extends StanzaTranslator<Iq> {

		@Override
		public Protocol getProtocol() {
			return Iq.PROTOCOL;
		}

		@Override
		protected String getType(Iq iq) {
			if (iq.getType() == null) {
				throw new IllegalArgumentException("Null iq type.");
			}
			
			return iq.getType().toString().toLowerCase();
		}

		@Override
		protected void translateSpecific(Iq iq, IProtocolWriter writer, ITranslatingFactory translatingFactory) {}
		
	}

}
