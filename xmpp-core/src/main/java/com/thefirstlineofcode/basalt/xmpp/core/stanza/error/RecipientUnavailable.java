package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class RecipientUnavailable extends StanzaError {
	public static final String DEFINED_CONDITION = "recipient-unavailable";
	
	public RecipientUnavailable() {
		this(null);
	}
	
	public RecipientUnavailable(String text) {
		this(text, null);
	}
	
	public RecipientUnavailable(String text, String lang) {
		super(StanzaError.Type.WAIT, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
