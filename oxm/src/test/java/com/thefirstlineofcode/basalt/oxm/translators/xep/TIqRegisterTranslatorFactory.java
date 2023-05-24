package com.thefirstlineofcode.basalt.oxm.translators.xep;

import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TIqRegister;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRegistrationField;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRegistrationForm;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRemove;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class TIqRegisterTranslatorFactory implements ITranslatorFactory<TIqRegister> {
	private ITranslator<TIqRegister> translator = new IqRegisterTranslator();

	@Override
	public Class<TIqRegister> getType() {
		return TIqRegister.class;
	}

	@Override
	public ITranslator<TIqRegister> create() {
		return translator;
	}
	
	private class IqRegisterTranslator implements ITranslator<TIqRegister> {

		@Override
		public Protocol getProtocol() {
			return TIqRegister.PROTOCOL;
		}

		@Override
		public String translate(TIqRegister iqRegister, IProtocolWriter writer,
				ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(TIqRegister.PROTOCOL);
			
			Object register = iqRegister.getRegister();
			
			if (register != null) {
				if (register instanceof TRegistrationForm) {
					TRegistrationForm form = (TRegistrationForm)register;
					
					if (form.isRegistered()) {
						writer.writeElementBegin("registered");
						writer.writeElementEnd();
					}
					
					for (TRegistrationField field : form.getFields()) {
						if (field.getValue() == null) {
							writer.writeEmptyElement(field.getName());
						} else {
							writer.writeTextOnly(field.getName(), field.getValue());
						}
					}
				} else if (register instanceof TRemove) {
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
