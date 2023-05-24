package com.thefirstlineofcode.basalt.oxm.parsing;

import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;

public interface IParsingContext<T> {
	ProtocolChain getProtocolChain();
	IParsingPath getParsingPath();
	T getObject();
	void setAttribute(Object key, Object value);
	<K> K getAttribute(Object key);
	<K> K removeAttribute(Object key);
	void setMessage(String message);
	String getMessage();
}
