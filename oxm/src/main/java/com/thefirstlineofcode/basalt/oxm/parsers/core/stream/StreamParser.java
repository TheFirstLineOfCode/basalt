package com.thefirstlineofcode.basalt.oxm.parsers.core.stream;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessAttributes;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessText;
import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Stream;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.InvalidXml;

@Parser(namespace="http://etherx.jabber.org/streams", localName="stream", objectType=Stream.class)
public class StreamParser {
	@ProcessText("/close")
	public void processCloseText(IParsingContext<Stream> context, Value<?> close) {
		if (close != null) {
			throw new BadMessageException("/stream/close should be a boolean only element.");
		}
			
		context.getObject().setClose(true);
	}
	
	@ProcessAttributes("/")
	public void processAttributes(IParsingContext<Stream> context, List<Attribute> attributes) {
		Stream stream = context.getObject();
		
		for (Attribute attribute : attributes) {
			if ("from".equals(attribute.getName())) {
				stream.setFrom(JabberId.parse(attribute.getValue().getString()));
			} else if ("to".equals(attribute.getName())) {
				stream.setTo(JabberId.parse(attribute.getValue().getString()));
			} else if ("id".equals(attribute.getName())) {
				stream.setId(attribute.getValue().getString());
			} else if ("version".equals(attribute.getName())) {
				stream.setVersion(attribute.getValue().getString());
			} else if ("xml:lang".equals(attribute.getName())) {
				stream.setLang(attribute.getValue().getString());
			} else {
				throw new ProtocolException(new InvalidXml(String.format("Unknown stream attribute: %s.",
						attribute.getName())));
			}
		}
	}
}
