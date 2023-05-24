package com.thefirstlineofcode.basalt.oxm.parsing;

import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;

public interface IParsingFactory {
	public static final Object KEY_FLAWS = new Object();
	
	Object parse(String message);
	Object parse(String message, boolean stream);
	
	void register(ProtocolChain chain, IParserFactory<?> parserFactory);
	void unregister(ProtocolChain chain);
	boolean isRegistered(ProtocolChain chain);
}
