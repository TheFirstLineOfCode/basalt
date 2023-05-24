package com.thefirstlineofcode.basalt.oxm.parsing;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class ParserAdaptor<T> implements IParser<T> {
	protected Class<? extends T> objectType;
	
	public ParserAdaptor(Class<? extends T> objectType) {
		this.objectType = objectType;
	}
	
	@Override
	public T createObject() {
		try {
			return objectType.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Can't create object instance.", e);
		}
	}

	@Override
	public IElementParser<T> getElementParser(IParsingPath parsingPath) {
		return null;
	}

	@Override
	public void processEmbeddedObject(IParsingContext<T> context, Protocol protocol, Object embedded) {
		throw new BadMessageException(String.format("Embedded object[%s] not allowed in here[current object: %s].",
				embedded, context.getObject()));
	}
	
}
