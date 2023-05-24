package com.thefirstlineofcode.basalt.oxm.binary;

public class BxmppConversionException extends Exception {
	private static final long serialVersionUID = -8357844796122957264L;

	public BxmppConversionException() {
		super();
	}
	
	public BxmppConversionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public BxmppConversionException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BxmppConversionException(String message) {
		super(message);
	}
	
	public BxmppConversionException(Throwable cause) {
		super(cause);
	}
}
