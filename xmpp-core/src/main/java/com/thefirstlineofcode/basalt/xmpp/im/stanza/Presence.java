package com.thefirstlineofcode.basalt.xmpp.im.stanza;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;

public final class Presence extends Stanza {
	public enum Type {
		UNAVAILABLE,
		SUBSCRIBE,
		SUBSCRIBED,
		UNSUBSCRIBE,
		UNSUBSCRIBED,
		PROBE
	}
	
	public enum Show {
		CHAT,
		AWAY,
		DND,
		XA
	}
	
	public static final Protocol PROTOCOL = new Protocol("presence");
	
	private List<LangText> statuses;
	private Integer priority;
	private Presence.Type type;
	private Presence.Show show;
	
	public Presence() {
	}
	
	public Presence(Presence.Type type) {
		this.type = type;
	}
	
	public Presence(Presence.Show show) {
		this.show = show;
	}
	
	public void setShow(Presence.Show show) {
		this.show = show;
	}
	
	public Presence.Show getShow() {
		return show;
	}
	
	public List<LangText> getStatuses() {
		if (statuses == null) {
			statuses = new ArrayList<>();
		}
		
		return statuses;
	}
	
	public void setStatuses(List<LangText> statuses) {
		this.statuses = statuses;
	}
	
	public Integer getPriority() {
		return priority;
	}
	
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public Presence.Type getType() {
		return type;
	}
	
	public void setType(Presence.Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Presence[");
		if (id != null) {
			sb.append("id='").append(id).append("',");
		}
		
		if (type != null) {
			sb.append("type='").append(type.name().toLowerCase()).append("',");
		}
		
		if (priority != null) {
			sb.append("priority=").append(priority.intValue()).append(',');
		}
		
		if (show != null) {
			sb.append("show='").append(show.name().toLowerCase()).append("',");
		}
		
		if (from != null) {
			sb.append("from='").append(from.toString()).append("',");
		}
		
		if (to != null) {
			sb.append("to='").append(to.toString()).append("',");
		}
		
		if (statuses != null && statuses.size() > 0) {
			sb.append("statuses=[");
			sb.append(statuses.get(0).toString());
			if (statuses.size() > 1) {
				sb.append(",...");
			}
			sb.append("],");
		}
		
		if (objects != null && objects.size() != 0) {
			sb.append("objects=[");
			for (Object object : objects) {
				sb.append(object.toString()).append(',');
			}
			
			sb.deleteCharAt(sb.length() - 1);
			sb.append(']');
		}
		
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		sb.append(']');
		
		return sb.toString();
	}
}
