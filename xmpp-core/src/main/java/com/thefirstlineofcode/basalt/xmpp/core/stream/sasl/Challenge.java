package com.thefirstlineofcode.basalt.xmpp.core.stream.sasl;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class Challenge {
	public static final Protocol PROTOCOL = new Protocol("urn:ietf:params:xml:ns:xmpp-sasl", "challenge");
	
	private String text;
	
	public Challenge() {}
	
	public Challenge(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
