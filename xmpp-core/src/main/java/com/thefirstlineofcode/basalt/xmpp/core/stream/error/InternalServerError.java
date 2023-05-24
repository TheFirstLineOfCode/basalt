package com.thefirstlineofcode.basalt.xmpp.core.stream.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class InternalServerError extends StreamError {
	public static final String DEFINED_CONDITION = "internal-server-error";
	
	private Exception exception;
	
	public InternalServerError() {
		this(null);
	}
	
	public InternalServerError(String text) {
		this(text, null);
	}
	
	public InternalServerError(String text, Exception exception) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
		
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
}
