package com.thefirstlineofcode.basalt.xmpp.core.stream.error;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;

public class ResourceConstraint extends StreamError {
	public static final String DEFINED_CONDITION = "resource-constraint";
	
	public ResourceConstraint() {
		this(null);
	}
	
	public ResourceConstraint(String text) {
		super(DEFINED_CONDITION);
		if (text != null) {
			setText(new LangText(text));
		}
	}
}
