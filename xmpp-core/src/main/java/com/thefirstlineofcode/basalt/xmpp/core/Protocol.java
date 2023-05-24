package com.thefirstlineofcode.basalt.xmpp.core;

public final class Protocol {
	private String localName;
	private String namespace;
	
	public Protocol(String localName) {
		this(null, localName);
	}
	
	public Protocol(String namespace, String localName) {
		if (localName == null) {
			throw new IllegalArgumentException("Null local name");
		}
		
		if ("".equals(namespace)) {
			namespace = null;
		}
		
		this.namespace = namespace;
		this.localName = localName;
	}
	
	public String getLocalName() {
		return localName;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		
		if (namespace != null)
			hash += 31 * hash + namespace.hashCode();
		
		hash += 31 * hash + localName.hashCode();
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (obj instanceof Protocol) {
			Protocol other = (Protocol)obj;
			if (!localName.equals(other.localName))
				return false;
			
			if (namespace == null) {
				if (other.namespace != null)
					return false;
			} else {
				if (!namespace.equals(other.namespace))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public String getProtocolString() {
		if (namespace == null)
			return localName;
		
		return String.format("%s|%s", namespace, localName);
	}
	
	public static Protocol parse(String string) {
		if (string == null)
			throw new IllegalArgumentException("Null protocol string.");
		
		int indexOfVerticalBar = string.indexOf('|');
		
		if (indexOfVerticalBar == string.length() - 1)
			throw new IllegalArgumentException("Illegal protocol string.");
		
		if (indexOfVerticalBar == -1)
			return new Protocol(string);
		
		
		return new Protocol(string.substring(0, indexOfVerticalBar),
				string.substring(indexOfVerticalBar + 1, string.length()));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Protocol[");
		sb.append(getProtocolString());
		sb.append("]");
		
		return sb.toString();
	}
}
