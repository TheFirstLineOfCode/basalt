package com.thefirstlineofcode.basalt.oxm.parsers.core.stream.sasl;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessAttributes;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessText;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.BadFormat;
import com.thefirstlineofcode.basalt.xmpp.core.stream.sasl.Auth;

@Parser(namespace="urn:ietf:params:xml:ns:xmpp-sasl", localName="auth", objectType=Auth.class)
public class AuthParser {
	@ProcessAttributes("/")
	public void processAttributes(IParsingContext<Auth> context, List<Attribute> attributes) {
		for (Attribute attribute : attributes) {
			if (Auth.ATTRIBUTE_NAME_MECHANISM.equals(attribute.getName()) && attribute.getPrefix() == null) {
				context.getObject().setMechanism(attribute.getValue().getString());
			}
		}
			
	}
	
	@ProcessText("/")
	public void processText(IParsingContext<Auth> context, Value<?> value) {
		if (context.getObject().getMechanism() == null)
			throw new ProtocolException(new BadFormat(String.format("Must have a mechanism attribute[protocol %s].", context.getProtocolChain())));
	}
}
