package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class JidMalformed extends StanzaError {
	public static final String DEFINED_CONDITION = "jid-malformed";
	
	public JidMalformed() {
		this(null);
	}
	
	public JidMalformed(String text) {
		this(text, null);
	}
	
	public JidMalformed(String text, String lang) {
		super(StanzaError.Type.MODIFY, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
	}
}
