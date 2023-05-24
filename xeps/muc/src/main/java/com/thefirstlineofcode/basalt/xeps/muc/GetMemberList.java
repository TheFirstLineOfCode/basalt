package com.thefirstlineofcode.basalt.xeps.muc;

public class GetMemberList {
	private boolean moderator;
	private boolean participant;
	private boolean visitor;
	
	public GetMemberList() {
		moderator = true;
		participant = true;
		visitor = true;
	}
	
	public boolean isModerator() {
		return moderator;
	}
	
	public void setModerator(boolean moderator) {
		this.moderator = moderator;
	}
	
	public boolean isParticipant() {
		return participant;
	}
	
	public void setParticipant(boolean participant) {
		this.participant = participant;
	}
	
	public boolean isVisitor() {
		return visitor;
	}
	
	public void setVisitor(boolean visitor) {
		this.visitor = visitor;
	}
}
