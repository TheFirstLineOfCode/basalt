package com.thefirstlineofcode.basalt.xeps.rsm;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.Text;

public class Before {
	@Text
	private String text;
	
	public Before() {}
	
	public Before(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
