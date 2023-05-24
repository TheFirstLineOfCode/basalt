package com.thefirstlineofcode.basalt.oxm.parsers.core.stream;

import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessText;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Bind;

@Parser(namespace="urn:ietf:params:xml:ns:xmpp-bind", localName="bind", objectType=Bind.class)
public class BindParser {
	@ProcessText("/jid")
	public void processJid(IParsingContext<Bind> context, Value<?> value) {
		context.getObject().setJid(JabberId.parse(value.toString()));
	}
	
	@ProcessText("/resource")
	public void processResource(IParsingContext<Bind> context, Value<?> value) {
		context.getObject().setResource(value.toString());
	}
}
