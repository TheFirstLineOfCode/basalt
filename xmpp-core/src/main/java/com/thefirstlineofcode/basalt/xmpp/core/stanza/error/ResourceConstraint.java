package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class ResourceConstraint extends StanzaError {
	public static final String DEFINED_CONDITION = "resource-constraint";
	
	public ResourceConstraint() {
		this(null);
	}
	
	public ResourceConstraint(String text) {
		this(text, null);
	}
	
	public ResourceConstraint(String text, String lang) {
		super(StanzaError.Type.WAIT, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
