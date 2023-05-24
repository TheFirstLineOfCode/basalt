package com.thefirstlineofcode.basalt.oxm;

public class TypeConversionException extends RuntimeException {

	private static final long serialVersionUID = 7535318148419146845L;

	public TypeConversionException() {
		super();
	}

	public TypeConversionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TypeConversionException(String arg0) {
		super(arg0);
	}

	public TypeConversionException(Throwable arg0) {
		super(arg0);
	}	

}
