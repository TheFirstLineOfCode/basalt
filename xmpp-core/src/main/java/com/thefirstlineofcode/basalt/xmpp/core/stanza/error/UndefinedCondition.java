package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class UndefinedCondition extends StanzaError {
	public static final String DEFINED_CONDITION = "undefined-condition";
	
	public UndefinedCondition(StanzaError.Type type) {
		this(type, null);
	}
	
	public UndefinedCondition(StanzaError.Type type, String text) {
		this(type, text, null);
	}
	
	public UndefinedCondition(StanzaError.Type type, String text, String lang) {
		super(type, DEFINED_CONDITION, text == null ? null : new LangText(text, lang));
		if (type == null) {
			this.type = StanzaError.Type.CANCEL;
		}
	}
}
