package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class BadRequest extends StanzaError {
	public static final String DEFINED_CONDITION = "bad-request";

	public BadRequest() {
		this(null);
	}
	
	public BadRequest(String text) {
		this(text, null);
	}
	
	public BadRequest(String text, String lang) {
		super(StanzaError.Type.MODIFY, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
