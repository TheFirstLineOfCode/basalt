package com.thefirstlineofcode.basalt.xmpp.core.stream.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class XmlNotWellFormed extends StreamError {
	public static final String DEFINED_CONDITION = "xml-not-well-formed";
	
	public XmlNotWellFormed() {
		this(null);
	}
	
	public XmlNotWellFormed(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
