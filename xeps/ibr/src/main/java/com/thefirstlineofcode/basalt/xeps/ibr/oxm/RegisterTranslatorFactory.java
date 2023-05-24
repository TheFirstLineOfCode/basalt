package com.thefirstlineofcode.basalt.xeps.ibr.oxm;

import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xeps.ibr.Register;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class RegisterTranslatorFactory implements ITranslatorFactory<Register> {
	private static final ITranslator<Register> translator = new RegisterTranslator();

	@Override
	public Class<Register> getType() {
		return Register.class;
	}

	@Override
	public ITranslator<Register> create() {
		return translator;
	}
	
	private static class RegisterTranslator implements ITranslator<Register> {
		

		@Override
		public Protocol getProtocol() {
			return Register.PROTOCOL;
		}

		@Override
		public String translate(Register register, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Register.PROTOCOL);
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}
}
