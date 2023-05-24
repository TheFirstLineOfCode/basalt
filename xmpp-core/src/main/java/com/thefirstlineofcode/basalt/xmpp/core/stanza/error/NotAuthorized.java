package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class NotAuthorized extends StanzaError {
	public static final String DEFINED_CONDITION = "not-authorized";
	
	public NotAuthorized() {
		this(null);
	}
	
	public NotAuthorized(String text) {
		this(text, null);
	}
	
	public NotAuthorized(String text, String lang) {
		super(StanzaError.Type.AUTH, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
