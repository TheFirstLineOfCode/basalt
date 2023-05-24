package com.thefirstlineofcode.basalt.xeps.muc;


public class RoomInfo {
	private String roomName;
	private boolean hidden;
	private boolean membersOnly;
	private boolean moderated;
	private boolean persistent;
	private boolean passwordProtected;
	private boolean nonAnonymous;
	private boolean semiAnonymous;
	
	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setPublic(boolean publicc) {
		hidden = !publicc;
	}
	
	public boolean isPublic() {
		return !hidden;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public boolean isMembersOnly() {
		return membersOnly;
	}
	
	public boolean isOpen() {
		return !membersOnly;
	}
	
	public void setMembersOnly(boolean membersOnly) {
		this.membersOnly = membersOnly;
	}
	
	public boolean isModerated() {
		return moderated;
	}
	
	public boolean isUnmoderated() {
		return !moderated;
	}
	
	public void setModerated(boolean moderated) {
		this.moderated = moderated;
	}
	
	public boolean isPersistent() {
		return persistent;
	}
	
	public boolean isTemporary() {
		return !persistent;
	}
	
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}
	
	public boolean isPasswordProtected() {
		return passwordProtected;
	}
	
	public boolean isUnsecure() {
		return !passwordProtected;
	}
	
	public void setPasswordProtected(boolean passwordProtected) {
		this.passwordProtected = passwordProtected;
	}
	
	public boolean isNonAnonymous() {
		return nonAnonymous;
	}
	
	public void setNonAnonymous(boolean nonAnonymous) {
		this.nonAnonymous = nonAnonymous;
	}
	
	public boolean isSemiAnonymous() {
		return semiAnonymous;
	}
	
	public void setSemiAnonymous(boolean semiAnonymous) {
		this.semiAnonymous = semiAnonymous;
	}
	
	public boolean isAnonymous() {
		return !nonAnonymous && !semiAnonymous;
	}
	
}
