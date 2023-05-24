package com.thefirstlineofcode.basalt.xeps.muc.user;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="http://jabber.org/protocol/muc#user", localName="x")
public class MucUser {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/muc#user", "x");
	
	private Decline decline;
	private Destroy destroy;
	@Array(Invite.class)
	private List<Invite> invites;
	@Array(Item.class)
	private List<Item> items;
	@TextOnly
	private String password;
	@Array(Status.class)
	private List<Status> statuses;
	
	public Decline getDecline() {
		return decline;
	}
	
	public void setDecline(Decline decline) {
		this.decline = decline;
	}
	
	public Destroy getDestroy() {
		return destroy;
	}
	
	public void setDestroy(Destroy destroy) {
		this.destroy = destroy;
	}
	
	public List<Invite> getInvites() {
		if (invites == null) {
			invites = new ArrayList<>();
		}
		
		return invites;
	}
	
	public void setInvites(List<Invite> invites) {
		this.invites = invites;
	}
	
	public List<Item> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		
		return items;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Status> getStatuses() {
		if (statuses == null) {
			statuses = new ArrayList<>();
		}
		
		return statuses;
	}
	
	public void setStatuses(List<Status> statuses) {
		this.statuses = statuses;
	}
	
}
