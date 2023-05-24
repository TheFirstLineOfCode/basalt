package com.thefirstlineofcode.basalt.oxm.parsing;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public interface IParserFactory<T> {
	Protocol getProtocol();
	IParser<T> create();
}
