package com.thefirstlineofcode.basalt.xmpp.core.stream.sasl;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class Failure {
	public static final Protocol PROTOCOL = new Protocol("urn:ietf:params:xml:ns:xmpp-sasl", "failure");
	
	// rfc3920(6.4)
	public enum ErrorCondition {
		ABORTED,
		INCORRECT_ENCODING,
		INVALID_AUTHZID,
		INVALID_MECHANISM,
		MECHANISM_TOO_WEAK,
		NOT_AUTHORIZED,
		TEMPORARY_AUTH_FAILURE
	}
	
	private ErrorCondition errorCondition;
	
	public Failure() {}
	
	public Failure(ErrorCondition errorCondition) {
		this.errorCondition = errorCondition;
	}

	public ErrorCondition getErrorCondition() {
		return errorCondition;
	}

	public void setErrorCondition(ErrorCondition errorCondition) {
		this.errorCondition = errorCondition;
	}
	
}
