package com.thefirstlineofcode.basalt.xeps.muc.xconference;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.BindTo;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="jabber:x:conference", localName="x")
public class XConference {
	public static final Protocol PROTOCOL = new Protocol("jabber:x:conference", "x");
	
	@BindTo("continue")
	private boolean continuee;
	@String2JabberId
	private JabberId jid;
	private String password;
	private String reason;
	private String thread;
	
	public boolean isContinue() {
		return continuee;
	}
	
	public void setContinue(boolean continuee) {
		this.continuee = continuee;
	}
	
	public JabberId getJid() {
		return jid;
	}
	
	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getThread() {
		return thread;
	}
	
	public void setThread(String thread) {
		this.thread = thread;
	}
	
}
