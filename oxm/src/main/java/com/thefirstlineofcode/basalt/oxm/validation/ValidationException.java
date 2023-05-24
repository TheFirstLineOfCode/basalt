package com.thefirstlineofcode.basalt.oxm.validation;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 7769054449182690341L;

	public ValidationException() {
		super();
	}

	public ValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ValidationException(String arg0) {
		super(arg0);
	}

	public ValidationException(Throwable arg0) {
		super(arg0);
	}
}
