package com.thefirstlineofcode.basalt.oxm.translators.core.stream.tls;

import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stream.tls.StartTls;

public class StartTlsTranslatorFactory implements ITranslatorFactory<StartTls> {
	private static final ITranslator<StartTls> translator = new StartTlsTranslator();

	@Override
	public Class<StartTls> getType() {
		return StartTls.class;
	}

	@Override
	public ITranslator<StartTls> create() {
		return translator;
	}
	
	private static class StartTlsTranslator implements ITranslator<StartTls> {

		@Override
		public Protocol getProtocol() {
			return StartTls.PROTOCOL;
		}

		@Override
		public String translate(StartTls startTls, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(StartTls.PROTOCOL);
			if (startTls.getRequired()) {
				writer.writeEmptyElement("required");
			}
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
