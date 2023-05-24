package com.thefirstlineofcode.basalt.oxm;

import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;

public interface IOxmFactory {
	Object parse(String message);
	Object parse(String message, boolean stream);
	String translate(Object object);
	
	void setParsingFactory(IParsingFactory parsingFactory);
	IParsingFactory getParsingFactory();
	
	void setTranslatingFactory(ITranslatingFactory translatingFactory);
	ITranslatingFactory getTranslatingFactory();
	
	void register(ProtocolChain chain, IParserFactory<?> parserFactory);
	void unregister(ProtocolChain protocolChain);
	boolean isParserRegistered(ProtocolChain protocolChain);
	
	void register(Class<?> type, ITranslatorFactory<?> translatorFactory);
	void unregister(Class<?> type);
	boolean isTranslatorRegistered(Class<?> type);
}
