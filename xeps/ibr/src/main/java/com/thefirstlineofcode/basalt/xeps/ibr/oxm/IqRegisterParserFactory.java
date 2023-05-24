package com.thefirstlineofcode.basalt.xeps.ibr.oxm;

import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.parsing.ElementParserAdaptor;
import com.thefirstlineofcode.basalt.oxm.parsing.IElementParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingPath;
import com.thefirstlineofcode.basalt.oxm.parsing.ParserAdaptor;
import com.thefirstlineofcode.basalt.xeps.ibr.IqRegister;
import com.thefirstlineofcode.basalt.xeps.ibr.RegistrationField;
import com.thefirstlineofcode.basalt.xeps.ibr.RegistrationForm;
import com.thefirstlineofcode.basalt.xeps.ibr.Remove;
import com.thefirstlineofcode.basalt.xeps.oob.XOob;
import com.thefirstlineofcode.basalt.xeps.xdata.XData;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;

public class IqRegisterParserFactory implements IParserFactory<IqRegister> {
	@Override
	public Protocol getProtocol() {
		return IqRegister.PROTOCOL;
	}

	@Override
	public IParser<IqRegister> create() {
		return new IqRegisterParser();
	}
	
	private static class IqRegisterParser extends ParserAdaptor<IqRegister> {
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
		
		private IElementParser<IqRegister> stringFieldElementParser = new StringFieldElementParser();
		private IElementParser<IqRegister> resourceFieldElementParser = new ResourceFieldElementParser();

		public IqRegisterParser() {
			super(IqRegister.class);
		}
		
		@Override
		public void processEmbeddedObject(IParsingContext<IqRegister> context, Protocol protocol, Object embedded) {
			if (protocol.equals(XData.PROTOCOL)) {
				context.getObject().setXData((XData)embedded);
			} else if (protocol.equals(XOob.PROTOCOL)) {
				context.getObject().setOob((XOob)embedded);
			} else {
				super.processEmbeddedObject(context, protocol, embedded);
			}
		}
		
		@Override
		public IElementParser<IqRegister> getElementParser(IParsingPath parsingPath) {
			if (parsingPath.match("/")) {
				return new ElementParserAdaptor<>();
			} else if (parsingPath.match("/registered")) {
				return new ElementParserAdaptor<IqRegister>() {
					@Override
					public void processText(IParsingContext<IqRegister> context, Value<?> text) {
						super.processText(context, text);
						
						RegistrationForm form = getRegistrationForm(context);
						form.setRegistered(true);
					}
				};
			} else if (parsingPath.match("/remove")) {
				return new ElementParserAdaptor<IqRegister>() {
					@Override
					public void processText(IParsingContext<IqRegister> context, Value<?> text) {
						super.processText(context, text);
						
						if (context.getObject().getRegister() != null) {
							throw new ProtocolException(new BadRequest("'remove' must be the only element in iq register."));
						}
						
						context.getObject().setRegister(new Remove());
					}
				};
			} else {
				for (String fieldPath : stringFields) {
					if (parsingPath.match((ROOT_PARSING_PATH + fieldPath))) {
						return stringFieldElementParser;
					}
				}
				
				// tsung's register info includes a 'resource' element.
				// we need to ignore this element.
				if (parsingPath.match("/resource")) {
					return resourceFieldElementParser;
				}
				
				return super.getElementParser(parsingPath);
			}
		}
		
		private static RegistrationForm getRegistrationForm(IParsingContext<IqRegister> context) {
			Object register = context.getObject().getRegister();
			
			if (register == null) {
				RegistrationForm form = new RegistrationForm();
				context.getObject().setRegister(form);
				
				return form;
			} else {
				if (!(register instanceof RegistrationForm)) {
					throw new ProtocolException(new BadRequest("'register' must be a registration form."));
				}
				
				return (RegistrationForm)register;
			}
		}
		
		// use this element parser to ignore 'resource' element
		private static class ResourceFieldElementParser extends ElementParserAdaptor<IqRegister> {
			@Override
			public void processText(IParsingContext<IqRegister> context, Value<?> text) {
				// ignore
			}
		}
		
		private static class StringFieldElementParser extends ElementParserAdaptor<IqRegister> {
			@Override
			public void processText(IParsingContext<IqRegister> context, Value<?> text) {
				String parsingPathString = context.getParsingPath().toString();
				String fieldName = parsingPathString.substring(1, parsingPathString.length());
				RegistrationForm form = getRegistrationForm(context);
				
				for (RegistrationField field : form.getFields()) {
					if (field.getName().equals(fieldName)) {
						throw new ProtocolException(new BadRequest(String.format(
							"Reduplicate registration field %s.", fieldName)));
					}
				}
				
				form.getFields().add(new RegistrationField(fieldName, text == null ? null : text.toString()));
			}
		}
	}
}
