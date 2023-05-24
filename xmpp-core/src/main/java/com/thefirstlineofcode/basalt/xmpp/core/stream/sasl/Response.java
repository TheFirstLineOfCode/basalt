package com.thefirstlineofcode.basalt.xmpp.core.stream.sasl;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class Response {
	public static final Protocol PROTOCOL = new Protocol("urn:ietf:params:xml:ns:xmpp-sasl", "response");
	
	private String text;
	
	public Response() {}
	
	public Response(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
