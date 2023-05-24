package com.thefirstlineofcode.basalt.xeps.muc;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.xmpp.core.JabberId;

public class RoomConfig {
	public enum AllowPm {
		ANYONE,
		PARTICIPANTS,
		MODERATORS,
		NONE
	}
	
	public enum WhoIs {
		MODERATORS,
		ANYONE,
		NONE
	}
	
	private String roomName;
	private String roomDesc;
	private String lang;
	private boolean enableLogging;
	private boolean changeSubject;
	private boolean allowInvites;
	private AllowPm allowPm;
	private int maxUsers;
	private PresenceBroadcast presenceBroadcast;
	private GetMemberList getMemberList;
	private boolean publicRoom;
	private boolean persistentRoom;
	private boolean moderatedRoom;
	private boolean membersOnly;
	private boolean passwordProtectedRoom;
	private String roomSecret;
	private WhoIs whoIs;
	private int maxHistoryFetch;
	private String pubSub;
	private List<JabberId> admins;
	private List<JabberId> owners;
	
	public RoomConfig() {
		roomName = null;
		roomDesc = null;
		lang = null;
		enableLogging = false;
		changeSubject = false;
		allowInvites = false;
		allowPm = AllowPm.ANYONE;
		maxUsers = 20;
		publicRoom = true;
		persistentRoom = true;
		moderatedRoom = false;
		membersOnly = false;
		passwordProtectedRoom = false;
		roomSecret = null;
		whoIs = WhoIs.MODERATORS;
		maxHistoryFetch = 50;
		pubSub = null;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getRoomDesc() {
		return roomDesc;
	}
	
	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public boolean isEnableLogging() {
		return enableLogging;
	}
	
	public void setEnableLogging(boolean enableLogging) {
		this.enableLogging = enableLogging;
	}
	
	public boolean isChangeSubject() {
		return changeSubject;
	}
	
	public void setChangeSubject(boolean changeSubject) {
		this.changeSubject = changeSubject;
	}
	
	public boolean isAllowInvites() {
		return allowInvites;
	}
	
	public void setAllowInvites(boolean allowInvites) {
		this.allowInvites = allowInvites;
	}
	
	public AllowPm getAllowPm() {
		return allowPm;
	}
	
	public void setAllowPm(AllowPm allowPm) {
		this.allowPm = allowPm;
	}
	
	public int getMaxUsers() {
		return maxUsers;
	}
	
	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}
	
	public PresenceBroadcast getPresenceBroadcast() {
		return presenceBroadcast;
	}
	
	public void setPresenceBroadcast(PresenceBroadcast presenceBroadcast) {
		this.presenceBroadcast = presenceBroadcast;
	}
	
	public GetMemberList getGetMemberList() {
		return getMemberList;
	}
	
	public void setGetMemberList(GetMemberList getMemberList) {
		this.getMemberList = getMemberList;
	}
	
	public boolean isPublicRoom() {
		return publicRoom;
	}
	
	public void setPublicRoom(boolean publicRoom) {
		this.publicRoom = publicRoom;
	}
	
	public boolean isPersistentRoom() {
		return persistentRoom;
	}
	
	public void setPersistentRoom(boolean persistentRoom) {
		this.persistentRoom = persistentRoom;
	}
	
	public boolean isModeratedRoom() {
		return moderatedRoom;
	}
	
	public void setModeratedRoom(boolean moderatedRoom) {
		this.moderatedRoom = moderatedRoom;
	}
	
	public boolean isMembersOnly() {
		return membersOnly;
	}
	
	public void setMembersOnly(boolean membersOnly) {
		this.membersOnly = membersOnly;
	}
	
	public boolean isPasswordProtectedRoom() {
		return passwordProtectedRoom;
	}
	
	public void setPasswordProtectedRoom(boolean passwordProtectedRoom) {
		this.passwordProtectedRoom = passwordProtectedRoom;
	}
	
	public String getRoomSecret() {
		return roomSecret;
	}
	
	public void setRoomSecret(String roomSecret) {
		this.roomSecret = roomSecret;
	}
	
	public WhoIs getWhoIs() {
		return whoIs;
	}
	
	public void setWhoIs(WhoIs whoIs) {
		this.whoIs = whoIs;
	}
	
	public int getMaxHistoryFetch() {
		return maxHistoryFetch;
	}
	
	public void setMaxHistoryFetch(int maxHistoryFetch) {
		this.maxHistoryFetch = maxHistoryFetch;
	}
	
	public String getPubSub() {
		return pubSub;
	}
	
	public void setPubSub(String pubSub) {
		this.pubSub = pubSub;
	}

	public List<JabberId> getAdmins() {
		if (admins == null) {
			admins = new ArrayList<>();
		}
		
		return admins;
	}

	public void setAdmins(List<JabberId> admins) {
		this.admins = admins;
	}

	public List<JabberId> getOwners() {
		if (owners == null) {
			owners = new ArrayList<>();
		}
		
		return owners;
	}

	public void setOwners(List<JabberId> owners) {
		this.owners = owners;
	}
	
}
