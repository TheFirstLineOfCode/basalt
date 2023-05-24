package com.thefirstlineofcode.basalt.xmpp.core;

import java.util.ArrayList;
import java.util.List;

public class ProtocolChain {
	protected List<Protocol> protocols;
	
	protected ProtocolChain() {
		protocols = new ArrayList<>();
	}
	
	public static ProtocolChain first(Protocol first) {
		return new ProtocolChain().append(first);
	}
	
	protected ProtocolChain append(Protocol protocol) {
		protocols.add(protocol);
		
		return this;
	}
	
	public ProtocolChain next(Protocol next) {
		return append(next);
	}
	
	public ProtocolChain clone() {
		ProtocolChain cloned = new ProtocolChain();
		cloned.protocols.addAll(this.protocols);
		
		return cloned;
	}
	
	@Override
	public int hashCode() {		
		if (protocols.size() == 0)
			return 0;
		
		int hash = 7;
		for (Protocol protocol : protocols) {
			hash += 31 * hash + protocol.hashCode();
		}
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (obj instanceof ProtocolChain) {
			ProtocolChain other = (ProtocolChain)obj;
			if (protocols.size() != other.protocols.size())
				return false;
			
			for (int i = 0; i < protocols.size(); i++) {
				if (!protocols.get(i).equals(other.protocols.get(i))) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("ProtocolChain[");
		
		if (protocols.size() == 0) {
			sb.append("null");
		} else {
			for (Protocol protocol : protocols) {
				sb.append(protocol.toString());
				sb.append("->");
			}
			
			sb.delete(sb.length() - 2, sb.length());
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	public int size() {
		return protocols.size();
	}
	
	public Protocol get(int i) {
		Protocol protocol = protocols.get(i);
		
		return new Protocol(protocol.getNamespace(), protocol.getLocalName());
	}
}
