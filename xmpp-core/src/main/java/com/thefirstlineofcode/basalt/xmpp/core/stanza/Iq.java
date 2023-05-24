package com.thefirstlineofcode.basalt.xmpp.core.stanza;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;


public final class Iq extends Stanza {
	public enum Type {
		RESULT,
		SET,
		GET
	}
	
	public static final Protocol PROTOCOL = new Protocol("iq");
	
	private Iq.Type type;
	
	public Iq() {
		this(null);
	}
	
	public Iq(Iq.Type type) {
		this(type, null , null);
	}
	
	public Iq(Iq.Type type, String id) {
		this(type, null, id);
	}
	
	public Iq(Iq.Type type, Object object) {
		this(type, object, null);
	}
	
	public Iq(Iq.Type type, Object object, String id) {
		this.type = type;	
		if (this.type == null) {
			this.type = Iq.Type.GET;
		}
		
		if (object != null)
			getObjects().add(object);
		
		if (id != null) {
			setId(id);
		} else {
			setId(Stanza.generateId());
		}
	}
	
	public Iq.Type getType() {
		return type;
	}
	
	public void setType(Iq.Type type) {
		this.type = type;
	}
	
	public static Iq createResult(Iq iq) {
		return createResult(iq, null);
	}
	
	public static Iq createResult(Iq iq, Object object) {
		Iq result = new Iq(Iq.Type.RESULT);
		
		result.setId(iq.getId());
		result.setFrom(iq.getTo());
		result.setTo(iq.getFrom());
		
		if (object != null)
			result.setObject(object);
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IQ[");
		if (id != null) {
			sb.append("id='").append(id).append("',");
		}
		
		if (type != null) {
			sb.append("type='").append(type.name().toLowerCase()).append("',");
		}
		
		if (from != null) {
			sb.append("from='").append(from.toString()).append("',");
		}
		
		if (to != null) {
			sb.append("to='").append(to.toString()).append("',");
		}
		
		if (objects != null && objects.size() != 0) {
			sb.append("objects=[");
			for (Object object : objects) {
				sb.append(object.toString()).append(',');
			}
			
			sb.deleteCharAt(sb.length() - 1);
			sb.append("],");
		}
		
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		sb.append(']');
		
		return sb.toString();
	}
}
