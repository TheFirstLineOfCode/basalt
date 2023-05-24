package com.thefirstlineofcode.basalt.oxm.parsing;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public interface IParser<T> {
	T createObject();
	IElementParser<T> getElementParser(IParsingPath parsingPath);
	void processEmbeddedObject(IParsingContext<T> context, Protocol protocol, Object embedded);
}
