package com.thefirstlineofcode.basalt.xmpp.core;

import com.thefirstlineofcode.basalt.xmpp.im.stanza.Presence;

public class PresenceProtocolChain extends ProtocolChain {
	public PresenceProtocolChain() {
		append(Presence.PROTOCOL);
	}
	
	public PresenceProtocolChain(Protocol xep) {
		append(Presence.PROTOCOL).append(xep);
	}
}
