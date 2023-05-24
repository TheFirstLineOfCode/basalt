package com.thefirstlineofcode.basalt.oxm.translators.core.stream.sasl;

import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stream.sasl.Mechanisms;

public class MechanismsTranslatorFactory implements ITranslatorFactory<Mechanisms> {
	private static final ITranslator<Mechanisms> translator = new MechanismsTranslator();

	@Override
	public Class<Mechanisms> getType() {
		return Mechanisms.class;
	}

	@Override
	public ITranslator<Mechanisms> create() {
		return translator;
	}
	
	private static class MechanismsTranslator implements ITranslator<Mechanisms> {

		@Override
		public Protocol getProtocol() {
			return Mechanisms.PROTOCOL;
		}

		@Override
		public String translate(Mechanisms mechanisms, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Mechanisms.PROTOCOL);
			
			for (String mechanism : mechanisms.getMechanisms()) {
				writer.writeTextOnly("mechanism", Value.create(mechanism));
			}
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
