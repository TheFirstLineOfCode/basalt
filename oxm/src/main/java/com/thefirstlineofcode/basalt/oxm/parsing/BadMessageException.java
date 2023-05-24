package com.thefirstlineofcode.basalt.oxm.parsing;

public class BadMessageException extends RuntimeException {
	private static final long serialVersionUID = 155089319386731599L;

	public BadMessageException() {
		super();
	}

	public BadMessageException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public BadMessageException(String arg0) {
		super(arg0);
	}

	public BadMessageException(Throwable arg0) {
		super(arg0);
	}
		
}
