package com.thefirstlineofcode.basalt.xmpp.im.roster;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class Roster {
	public static final Protocol PROTOCOL = new Protocol("jabber:iq:roster", "query");
	
	private List<Item> items;

	public Item[] getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		
		return items.toArray(new Item[items.size()]);
	}

	public synchronized void setItems(List<Item> items) {
		this.items = items;
	}
	
	public synchronized boolean exists(JabberId contact) {
		if (items == null)
			return false;
		
		for (Item item : items) {
			if (item.getJid().getBareId().equals(contact.getBareId()))
				return true;
		}
		
		return false;
	}
	
	public synchronized Item getItem(JabberId contact) {
		if (items == null)
			return null;
		
		for (Item item : items) {
			if (item.getJid().getBareId().equals(contact.getBareId()))
				return item;
		}
		
		return null;
	}
	
	public synchronized void addOrUpdate(Item item) {
		if (items == null) {
			items = new ArrayList<>();
		}
		
		if (exists(item.getJid())) {
			Item local = getItem(item.getJid());
			local.setName(item.getName());
			local.setSubscription(item.getSubscription());
			local.setAsk(item.getAsk());
			local.setGroups(item.getGroups());
		} else {
			items.add(item);
		}
	}
	
}
