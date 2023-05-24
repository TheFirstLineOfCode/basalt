package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class Conflict extends StanzaError {
	public static final String DEFINED_CONDITION = "conflict";
	
	public Conflict() {
		this(null);
	}
	
	public Conflict(String text) {
		this(text, null);
	}
	
	public Conflict(String text, String lang) {
		super(StanzaError.Type.CANCEL, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
