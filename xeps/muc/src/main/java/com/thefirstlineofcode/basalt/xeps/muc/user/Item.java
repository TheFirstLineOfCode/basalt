package com.thefirstlineofcode.basalt.xeps.muc.user;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.BindTo;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2Enum;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2JabberId;
import com.thefirstlineofcode.basalt.xeps.muc.Affiliation;
import com.thefirstlineofcode.basalt.xeps.muc.Role;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;

public class Item {
	private Actor actor;
	@BindTo("continue")
	private Continue continuee;
	@TextOnly
	private String reason;
	@String2Enum(Affiliation.class)
	private Affiliation affiliation;
	@String2JabberId
	private JabberId jid;
	private String nick;
	@String2Enum(Role.class)
	private Role role;
	
	public Actor getActor() {
		return actor;
	}
	
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	public Continue getContinuee() {
		return continuee;
	}
	
	public void setContinuee(Continue continuee) {
		this.continuee = continuee;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Affiliation getAffiliation() {
		return affiliation;
	}
	
	public void setAffiliation(Affiliation affiliation) {
		this.affiliation = affiliation;
	}
	
	public JabberId getJid() {
		return jid;
	}
	
	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
}
