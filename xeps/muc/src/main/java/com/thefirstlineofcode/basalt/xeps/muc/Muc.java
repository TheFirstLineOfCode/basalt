package com.thefirstlineofcode.basalt.xeps.muc;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="http://jabber.org/protocol/muc", localName="x")
public class Muc {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/muc", "x");
	
	private History history;
	@TextOnly
	private String password;
	
	public History getHistory() {
		return history;
	}
	
	public void setHistory(History history) {
		this.history = history;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
