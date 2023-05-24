package com.thefirstlineofcode.basalt.oxm.parsers.core.stream;

import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessEmbeddedObject;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Feature;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Features;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.InvalidXml;

@Parser(localName="stream:features", objectType=Features.class)
public class FeaturesParser {
	@ProcessEmbeddedObject
	public void processEmbeddedObject(IParsingContext<Features> context, Protocol protocol, Object embedded) {
		if (!(embedded instanceof Feature)) {
			throw new ProtocolException(new InvalidXml("Only feature object is allowed here."));
		}
		
		context.getObject().getFeatures().add((Feature)embedded);
	}
}
