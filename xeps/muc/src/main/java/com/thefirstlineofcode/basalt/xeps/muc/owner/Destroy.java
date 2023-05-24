package com.thefirstlineofcode.basalt.xeps.muc.owner;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;

public class Destroy {
	@TextOnly
	private String reason;
	@TextOnly
	private String password;
	
	@String2JabberId
	private JabberId jid;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JabberId getJid() {
		return jid;
	}

	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
}
