package com.thefirstlineofcode.basalt.oxm.parsing;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;

public class ElementParserAdaptor<T> implements IElementParser<T> {
	
	@Override
	public void processText(IParsingContext<T> context, Value<?> text) {
		if (text != null) {
			throw new BadMessageException(String.format("Text not allowed in %s->(%s).",
					context.getProtocolChain(), context.getParsingPath()));
		}
	}

	@Override
	public void processAttributes(IParsingContext<T> context, List<Attribute> attributes) {
		if (attributes.size() > 0) {
			throw new BadMessageException(String.format("Attributes not allowed in %s->(%s).",
					context.getProtocolChain(), context.getParsingPath()));
		}
	}

}
