package com.thefirstlineofcode.basalt.oxm.parsing;

import java.util.HashMap;
import java.util.Map;

import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;

public class ParsingContext<T> implements IParsingContext<T> {
	private ProtocolChain protocolChain;
	private T object;
	private ParsingPath parsingPath;
	private Map<Object, Object> attributes;
	
	private String message;
	
	public ParsingContext(ProtocolChain protocolChain, T object) {
		this.protocolChain = protocolChain;
		this.object = object;
		parsingPath = new ParsingPath();
		attributes = new HashMap<>();
	}

	@Override
	public ProtocolChain getProtocolChain() {
		return protocolChain.clone();
	}

	@Override
	public IParsingPath getParsingPath() {
		return parsingPath;
	}

	@Override
	public T getObject() {
		return object;
	}
	
	public void setObject(T object) {
		this.object = object;
	}
	
	@Override
	public void setAttribute(Object key, Object value) {
		attributes.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K getAttribute(Object key) {
		return (K)attributes.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> K removeAttribute(Object key) {
		return (K)attributes.remove(key);
	}
	
	public void enter(String element) {
		parsingPath.enter(element);
	}
	
	public void exit() {
		parsingPath.exit();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("ParsingContext{ProtocolChain:");
		sb.append(protocolChain);
		sb.append(", ");
		sb.append("ParsingPath:");
		sb.append(parsingPath.toString());
		sb.append(", ");
		sb.append("Object:");
		sb.append(object.getClass().getName());
		sb.append("]");
		
		return sb.toString();
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
