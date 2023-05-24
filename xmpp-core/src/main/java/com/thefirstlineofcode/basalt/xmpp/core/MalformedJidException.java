package com.thefirstlineofcode.basalt.xmpp.core;

public class MalformedJidException extends RuntimeException {
	
	private static final long serialVersionUID = -6629173818601861300L;

	public MalformedJidException() {
		super();
	}

	public MalformedJidException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MalformedJidException(String arg0) {
		super(arg0);
	}

	public MalformedJidException(Throwable arg0) {
		super(arg0);
	}

	
}
