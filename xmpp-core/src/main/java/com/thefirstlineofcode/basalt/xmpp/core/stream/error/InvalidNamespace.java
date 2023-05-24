package com.thefirstlineofcode.basalt.xmpp.core.stream.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class InvalidNamespace extends StreamError {
	public static final String DEFINED_CONDITION = "invalid-namespace";
	
	public InvalidNamespace() {
		this(null);
	}
	
	public InvalidNamespace(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
