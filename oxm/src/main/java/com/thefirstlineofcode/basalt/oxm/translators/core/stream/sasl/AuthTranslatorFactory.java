package com.thefirstlineofcode.basalt.oxm.translators.core.stream.sasl;

import com.thefirstlineofcode.basalt.oxm.Attributes;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stream.sasl.Auth;

public class AuthTranslatorFactory implements ITranslatorFactory<Auth> {
	private static final ITranslator<Auth> translator = new AuthTranslator();

	@Override
	public Class<Auth> getType() {
		return Auth.class;
	}

	@Override
	public ITranslator<Auth> create() {
		return translator;
	}
	
	private static class AuthTranslator implements ITranslator<Auth> {

		@Override
		public Protocol getProtocol() {
			return Auth.PROTOCOL;
		}

		@Override
		public String translate(Auth auth, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Auth.PROTOCOL);
			
			writer.writeAttributes(new Attributes().
					add(Auth.ATTRIBUTE_NAME_MECHANISM, auth.getMechanism()).
					get()
				);
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
