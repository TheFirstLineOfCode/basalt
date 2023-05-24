package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class FeatureNotImplemented extends StanzaError {
	public static final String DEFINED_CONDITION = "feature-not-implemented";
	
	public FeatureNotImplemented() {
		this(null);
	}
	
	public FeatureNotImplemented(String text) {
		this(text, null);
	}
	
	public FeatureNotImplemented(String text, String lang) {
		super(StanzaError.Type.CANCEL, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
