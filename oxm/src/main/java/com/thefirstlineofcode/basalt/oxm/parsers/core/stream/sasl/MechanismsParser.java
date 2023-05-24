package com.thefirstlineofcode.basalt.oxm.parsers.core.stream.sasl;

import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessText;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.xmpp.core.stream.sasl.Mechanisms;

@Parser(namespace="urn:ietf:params:xml:ns:xmpp-sasl", localName="mechanisms", objectType=Mechanisms.class)
public class MechanismsParser {
	@ProcessText("/mechanism")
	public void processText(IParsingContext<Mechanisms> context, Value<?> text) {
		if (text != null) {
			context.getObject().getMechanisms().add(text.toString());
		}
	}
}
