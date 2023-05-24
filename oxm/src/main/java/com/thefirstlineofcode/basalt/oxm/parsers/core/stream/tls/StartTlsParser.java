package com.thefirstlineofcode.basalt.oxm.parsers.core.stream.tls;

import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessText;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.BadFormat;
import com.thefirstlineofcode.basalt.xmpp.core.stream.tls.StartTls;

@Parser(namespace="urn:ietf:params:xml:ns:xmpp-tls", localName="starttls", objectType=StartTls.class)
public class StartTlsParser {
	@ProcessText("/required")
	public void processRequired(IParsingContext<StartTls> context, Value<?> text) {
		if (text != null) {
			throw new ProtocolException(new BadFormat("Element 'required' must be an empty element."));
		}
		
		context.getObject().setRequired(true);
	}
}
