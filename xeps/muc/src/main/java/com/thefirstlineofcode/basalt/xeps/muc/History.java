package com.thefirstlineofcode.basalt.xeps.muc;

import com.thefirstlineofcode.basalt.xmpp.datetime.DateTime;

public class History {
	private int maxChars;
	private int maxStanzas;
	private int seconds;
	private DateTime since;
	
	public int getMaxChars() {
		return maxChars;
	}
	
	public void setMaxChars(int maxChars) {
		this.maxChars = maxChars;
	}
	
	public int getMaxStanzas() {
		return maxStanzas;
	}
	
	public void setMaxStanzas(int maxStanzas) {
		this.maxStanzas = maxStanzas;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public DateTime getSince() {
		return since;
	}
	
	public void setSince(DateTime since) {
		this.since = since;
	}
	
}
