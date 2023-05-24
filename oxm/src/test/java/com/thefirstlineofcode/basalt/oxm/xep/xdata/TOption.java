package com.thefirstlineofcode.basalt.oxm.xep.xdata;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;

public class TOption {
	private String label;
	@TextOnly
	private String value;
	
	public TOption() {}
	
	public TOption(String label, String value) {
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
