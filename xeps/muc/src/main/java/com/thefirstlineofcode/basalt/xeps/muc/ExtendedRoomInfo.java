package com.thefirstlineofcode.basalt.xeps.muc;

import com.thefirstlineofcode.basalt.xmpp.core.JabberId;

public class ExtendedRoomInfo extends RoomInfo {
	private int maxHistoryFetch;
	private JabberId[] contactJid;
	private String roomDesc;
	private String lang;
	private String ldapGroup;
	private String logs;
	private int occupants;
	private String subject;
	private boolean subjectMod;
	
	public int getMaxHistoryFetch() {
		return maxHistoryFetch;
	}

	public void setMaxHistoryFetch(int maxHistoryFetch) {
		this.maxHistoryFetch = maxHistoryFetch;
	}

	public JabberId[] getContactJid() {
		return contactJid;
	}

	public void setContactJid(JabberId[] contactJid) {
		this.contactJid = contactJid;
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

	public String getLdapGroup() {
		return ldapGroup;
	}

	public void setLdapGroup(String ldapGroup) {
		this.ldapGroup = ldapGroup;
	}

	public String getLogs() {
		return logs;
	}

	public void setLogs(String logs) {
		this.logs = logs;
	}

	public int getOccupants() {
		return occupants;
	}

	public void setOccupants(int occupants) {
		this.occupants = occupants;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isSubjectMod() {
		return subjectMod;
	}

	public void setSubjectMod(boolean subjectMod) {
		this.subjectMod = subjectMod;
	}
}
