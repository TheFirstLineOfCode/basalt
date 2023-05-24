package com.thefirstlineofcode.basalt.xmpp.core;

import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;

public class IqProtocolChain extends ProtocolChain {
	public IqProtocolChain() {
		append(Iq.PROTOCOL);
	}
	
	public IqProtocolChain(Protocol xep) {
		append(Iq.PROTOCOL).append(xep);
	}
}
