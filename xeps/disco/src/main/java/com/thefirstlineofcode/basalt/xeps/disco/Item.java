package com.thefirstlineofcode.basalt.xeps.disco;

import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2JabberId;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.NotNull;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;

public class Item {
	@NotNull
	@String2JabberId
	private JabberId jid;
	private String name;
	private String node;
	
	public Item() {}
	
	public Item(JabberId jid) {
		this.jid = jid;
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
	
	public String getNode() {
		return node;
	}
	
	public void setNode(String node) {
		this.node = node;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		
		hash += 31 * hash + (jid == null ? 0 : jid.hashCode());
		hash += 31 * hash + (name == null ? 0 : name.hashCode());
		hash += 31 * hash + (node == null ? 0 : node.hashCode());
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (obj instanceof Item) {
			Item other = (Item)obj;
			
			if (jid == null) {
				if (other.jid != null)
					return false;
			} else {
				if (!jid.equals(other.jid))
					return false;
			}
			
			if (name == null) {
				if (other.name != null)
					return false;
			} else {
				if (!name.equals(other.name))
					return false;
			}
			
			if (node == null) {
				if (other.node != null)
					return false;
			} else {
				if (!node.equals(other.node))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
}
