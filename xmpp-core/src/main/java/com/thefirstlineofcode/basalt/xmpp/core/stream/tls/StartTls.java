package com.thefirstlineofcode.basalt.xmpp.core.stream.tls;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Feature;

public class StartTls implements Feature {
	public static final Protocol PROTOCOL = new Protocol("urn:ietf:params:xml:ns:xmpp-tls", "starttls");
	
	private boolean required;

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}
