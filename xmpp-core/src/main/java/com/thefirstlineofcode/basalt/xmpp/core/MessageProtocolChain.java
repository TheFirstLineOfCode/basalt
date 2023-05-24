package com.thefirstlineofcode.basalt.xmpp.core;

import com.thefirstlineofcode.basalt.xmpp.im.stanza.Message;

public class MessageProtocolChain extends ProtocolChain {
	public MessageProtocolChain() {
		append(Message.PROTOCOL);
	}
	
	public MessageProtocolChain(Protocol xep) {
		append(Message.PROTOCOL).append(xep);
	}
}
