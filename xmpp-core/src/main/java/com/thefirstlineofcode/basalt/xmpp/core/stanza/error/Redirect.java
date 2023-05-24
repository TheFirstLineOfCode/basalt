package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class Redirect extends StanzaError {
	public static final String DEFINED_CONDITION = "redirect";
	
	public Redirect() {
		this(null);
	}
	
	public Redirect(String text) {
		this(text, null);
	}
	
	public Redirect(String text, String lang) {
		super(StanzaError.Type.MODIFY, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
