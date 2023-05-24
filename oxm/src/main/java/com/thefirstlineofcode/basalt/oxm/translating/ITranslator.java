package com.thefirstlineofcode.basalt.oxm.translating;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public interface ITranslator<T> {
	Protocol getProtocol();
	String translate(T object, IProtocolWriter writer, ITranslatingFactory translatingFactory);
}
