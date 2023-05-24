package com.thefirstlineofcode.basalt.xeps.ibr.oxm;

import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xeps.ibr.IqRegister;
import com.thefirstlineofcode.basalt.xeps.ibr.RegistrationField;
import com.thefirstlineofcode.basalt.xeps.ibr.RegistrationForm;
import com.thefirstlineofcode.basalt.xeps.ibr.Remove;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class IqRegisterTranslatorFactory implements ITranslatorFactory<IqRegister> {
	private ITranslator<IqRegister> translator = new IqRegisterTranslator();

	@Override
	public Class<IqRegister> getType() {
		return IqRegister.class;
	}

	@Override
	public ITranslator<IqRegister> create() {
		return translator;
	}
	
	private class IqRegisterTranslator implements ITranslator<IqRegister> {

		@Override
		public Protocol getProtocol() {
			return IqRegister.PROTOCOL;
		}

		@Override
		public String translate(IqRegister iqRegister, IProtocolWriter writer,
				ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(IqRegister.PROTOCOL);
			
			Object register = iqRegister.getRegister();
			
			if (register != null) {
				if (register instanceof RegistrationForm) {
					RegistrationForm form = (RegistrationForm)register;
					
					if (form.isRegistered()) {
						writer.writeElementBegin("registered");
						writer.writeElementEnd();
					}
					
					for (RegistrationField field : form.getFields()) {
						if (field.getValue() == null) {
							writer.writeEmptyElement(field.getName());
						} else {
							writer.writeTextOnly(field.getName(), field.getValue());
						}
					}
				} else if (register instanceof Remove) {
					writer.writeElementBegin("remove");
					writer.writeElementEnd();
				} else {
					throw new RuntimeException("Unknown register object.");
				}
			}
			
			if (iqRegister.getOob() != null) {
				String oobString = translatingFactory.translate(iqRegister.getOob());
				if (oobString != null)
					writer.writeString(oobString);
			}
			
			if(iqRegister.getXData() != null) {
				String xDataString = translatingFactory.translate(iqRegister.getXData());
				if (xDataString != null)
					writer.writeString(xDataString);
			}
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
