package com.thefirstlineofcode.basalt.xeps.xdata;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;

public class Option {
	private String label;
	@TextOnly
	private String value;
	
	public Option() {}
	
	public Option(String label, String value) {
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}
