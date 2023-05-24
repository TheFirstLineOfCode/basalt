package com.thefirstlineofcode.basalt.oxm.translating;

import com.thefirstlineofcode.basalt.oxm.IProtocolWriterFactory;

public interface ITranslatingFactory {
	String translate(Object object);
	
	void register(Class<?> type, ITranslatorFactory<?> translatorFactory);
	void unregister(Class<?> type);
	boolean isRegistered(Class<?> type);
	
	
	IProtocolWriterFactory getWriterFactory();
}
