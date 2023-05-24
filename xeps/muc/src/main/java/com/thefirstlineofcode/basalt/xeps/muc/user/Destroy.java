package com.thefirstlineofcode.basalt.xeps.muc.user;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;

public class Destroy {
	@TextOnly
	private String reason;
	
	@String2JabberId
	private JabberId jid;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public JabberId getJid() {
		return jid;
	}

	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
}
