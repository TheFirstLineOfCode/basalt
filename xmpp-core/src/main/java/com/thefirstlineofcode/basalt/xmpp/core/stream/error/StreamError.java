package com.thefirstlineofcode.basalt.xmpp.core.stream.error;

import com.thefirstlineofcode.basalt.xmpp.core.AbstractError;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class StreamError extends AbstractError {
	public static final Protocol PROTOCOL = new Protocol("http://etherx.jabber.org/streams", "error");
	public static final String NAMESPACE_STREAM_ERROR_CONTEXT = "urn:ietf:params:xml:ns:xmpp-streams";
	public static final Protocol PROTOCOL_ERROR_DEFINED_CONDITION = new Protocol(NAMESPACE_STREAM_ERROR_CONTEXT, "*");
	
	public StreamError() {
	}
	
	public StreamError(String definedCondition) {
		super(definedCondition);
	}
	
	// (rfc3290 4.7.1)
	// stream error is always unrecoverable
	@Override
	public boolean closeStream() {
		return true;
	}

}
