package com.thefirstlineofcode.basalt.oxm.parsing;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;

public interface IElementParser<T> {
	void processText(IParsingContext<T> context, Value<?> text);
	void processAttributes(IParsingContext<T> context, List<Attribute> attributes);
}
