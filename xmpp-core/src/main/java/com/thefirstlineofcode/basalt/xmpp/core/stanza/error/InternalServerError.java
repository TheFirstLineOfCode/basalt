package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class InternalServerError extends StanzaError {
	public static final String DEFINED_CONDITION = "internal-server-error";
	
	private Exception exception;
	
	public InternalServerError() {
		this(null);
	}
	
	public InternalServerError(String text) {
		this(text, null, null);
	}
	
	public InternalServerError(String text, String lang) {
		this(text, lang, null);
	}
	
	public InternalServerError(String text, Exception exception) {
		this(text, null, exception);
	}
	
	public InternalServerError(String text, String lang, Exception exception) {
		super(StanzaError.Type.WAIT, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
}
