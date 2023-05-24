package com.thefirstlineofcode.basalt.xmpp.im.roster;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.xmpp.core.JabberId;

public class Item {
	public enum Subscription {
		NONE,
		TO,
		FROM,
		BOTH,
		REMOVE
	}
	
	public enum Ask {
		SUBSCRIBE
	}
	
	private List<String> groups;
	private JabberId jid;
	private String name;
	private Subscription subscription;
	private Ask ask;
	
	public List<String> getGroups() {
		if (groups == null) {
			groups = new ArrayList<>();
		}
		
		return groups;
	}
	
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	
	public JabberId getJid() {
		return jid;
	}
	
	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Subscription getSubscription() {
		return subscription;
	}
	
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
	
	public Ask getAsk() {
		return ask;
	}
	
	public void setAsk(Ask ask) {
		this.ask = ask;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item) {
			if (jid == null) {
				return false;
			} else {
				return jid.equals(((Item)obj).jid);
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		if (jid == null)
			return 0;
		
		return jid.hashCode();
	}
}
