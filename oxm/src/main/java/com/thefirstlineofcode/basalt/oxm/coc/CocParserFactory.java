package com.thefirstlineofcode.basalt.oxm.coc;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.parsing.IParser;
import com.thefirstlineofcode.basalt.oxm.parsing.IParserFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class CocParserFactory<T> implements IParserFactory<T> {
	private Protocol protocol;
	private CocParser<T> parser;
	
	public CocParserFactory(Class<T> objectType) {
		this(getProtocol(objectType), objectType);
	}
	
	private CocParserFactory(Protocol protocol, Class<T> objectType) {
		this.protocol = protocol;
		parser = new CocParser<>(objectType);
	}
	
	private static Protocol getProtocol(Class<?> objectType) {
		ProtocolObject protocolObject = objectType.getAnnotation(ProtocolObject.class);
		if (protocolObject == null) {
			throw new IllegalArgumentException("@ProtocolObject not found.");
		}
		
		return new Protocol(protocolObject.namespace(), protocolObject.localName());
	}

	@Override
	public Protocol getProtocol() {
		return protocol;
	}

	@Override
	public IParser<T> create() {
		return parser;
	}
}
