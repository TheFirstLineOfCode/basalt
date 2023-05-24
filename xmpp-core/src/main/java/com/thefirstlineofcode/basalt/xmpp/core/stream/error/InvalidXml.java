package com.thefirstlineofcode.basalt.xmpp.core.stream.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class InvalidXml extends StreamError {
	public static final String DEFINED_CONDITION = "invalid-xml";
	
	public InvalidXml() {
		this(null);
	}
	
	public InvalidXml(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
