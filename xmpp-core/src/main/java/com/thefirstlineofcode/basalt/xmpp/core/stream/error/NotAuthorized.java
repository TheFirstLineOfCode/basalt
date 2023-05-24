package com.thefirstlineofcode.basalt.xmpp.core.stream.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class NotAuthorized extends StreamError {
	public static final String DEFINED_CONDITION = "not-authorized";
	
	public NotAuthorized() {
		this(null);
	}
	
	public NotAuthorized(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
