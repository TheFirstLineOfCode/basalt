package com.thefirstlineofcode.basalt.oxm.parsers.xep;

import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.parsing.ElementParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.oxm.parsing.ParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TIqRegister;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRegistrationField;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRegistrationForm;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRemove;
import com.thefirstlineofcode.basalt.oxm.xep.oob.TXOob;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TXData;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;

public class TIqRegisterParserFactory implements IParserFactory<TIqRegister> {
	@Override
	public Protocol getProtocol() {
		return TIqRegister.PROTOCOL;
	}

	@Override
	public IParser<TIqRegister> create() {
		return new IqRegisterParser();
	}
	
	private static class IqRegisterParser extends ParserAdaptor<TIqRegister> {
		private static String ROOT_PARSING_PATH = "/";
		
		private static String[] stringFields = new String[] {
			"instructions",
			"username",
			"nick",
			"password",
			"name",
			"first",
			"last",
			"email",
			"address",
			"city",
			"state",
			"zip",
			"phone",
			"url",
			"date",
			"misc",
			"text",
			"key"
		};
		
		private IElementParser<TIqRegister> stringFieldElementParser = new StringFieldElementParser();

		public IqRegisterParser() {
			super(TIqRegister.class);
		}
		
		@Override
		public void processEmbeddedObject(IParsingContext<TIqRegister> context, Protocol protocol, Object embedded) {
			if (protocol.equals(TXData.PROTOCOL)) {
				context.getObject().setXData((TXData)embedded);
			} else if (protocol.equals(TXOob.PROTOCOL)) {
				context.getObject().setOob((TXOob)embedded);
			} else {
				super.processEmbeddedObject(context, protocol, embedded);
			}
		}
		
		@Override
		public IElementParser<TIqRegister> getElementParser(IParsingPath parsingPath) {
			if (parsingPath.match("/")) {
				return new ElementParserAdaptor<>();
			} else if (parsingPath.match("/registered")) {
				return new ElementParserAdaptor<TIqRegister>() {
					@Override
					public void processText(IParsingContext<TIqRegister> context, Value<?> text) {
						super.processText(context, text);
						
						TRegistrationForm form = getRegistrationForm(context);
						form.setRegistered(true);
					}
				};
			} else if (parsingPath.match("/remove")) {
				return new ElementParserAdaptor<TIqRegister>() {
					@Override
					public void processText(IParsingContext<TIqRegister> context, Value<?> text) {
						super.processText(context, text);
						
						if (context.getObject().getRegister() != null) {
							throw new ProtocolException(new BadRequest("'remove' must be the only element in iq register."));
						}
						
						context.getObject().setRegister(new TRemove());
					}
				};
			} else {
				for (String fieldPath : stringFields) {
					if (parsingPath.match((ROOT_PARSING_PATH + fieldPath))) {
						return stringFieldElementParser;
					}
				}
				
				return super.getElementParser(parsingPath);
			}
		}
		
		private static TRegistrationForm getRegistrationForm(IParsingContext<TIqRegister> context) {
			Object register = context.getObject().getRegister();
			
			if (register == null) {
				TRegistrationForm form = new TRegistrationForm();
				context.getObject().setRegister(form);
				
				return form;
			} else {
				if (!(register instanceof TRegistrationForm)) {
					throw new ProtocolException(new BadRequest("'register' must be a registration form."));
				}
				
				return (TRegistrationForm)register;
			}
		}
		
		private static class StringFieldElementParser extends ElementParserAdaptor<TIqRegister> {
			@Override
			public void processText(IParsingContext<TIqRegister> context, Value<?> text) {
				String parsingPathString = context.getParsingPath().toString();
				String fieldName = parsingPathString.substring(1, parsingPathString.length());
				TRegistrationForm form = getRegistrationForm(context);
				
				for (TRegistrationField field : form.getFields()) {
					if (field.getName().equals(fieldName)) {
						throw new ProtocolException(new BadRequest(String.format(
							"Reduplicate registration field %s", fieldName)));
					}
				}
				
				form.getFields().add(new TRegistrationField(fieldName, text == null ? null : text.toString()));
			}
		}
	}
}
