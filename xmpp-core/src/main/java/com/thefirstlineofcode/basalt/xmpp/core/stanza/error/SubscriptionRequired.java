package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class SubscriptionRequired extends StanzaError {
	public static final String DEFINED_CONDITION = "subscription-required";
	
	public SubscriptionRequired() {
		this(null);
	}
	
	public SubscriptionRequired(String text) {
		this(text, null);
	}
	
	public SubscriptionRequired(String text, String lang) {
		super(StanzaError.Type.AUTH, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
