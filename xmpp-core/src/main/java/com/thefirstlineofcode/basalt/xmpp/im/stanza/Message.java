package com.thefirstlineofcode.basalt.xmpp.im.stanza;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;

public final class Message extends Stanza {
	public enum Type {
		NORMAL,
		CHAT,
		GROUPCHAT,
		HEADLINE
	}
	
	public static final Protocol PROTOCOL = new Protocol("message");
	
	private List<LangText> subjects;
	private List<LangText> bodies;
	private String thread;
	private Message.Type type;
	
	public Message() {
		this(null);
	}
	
	public Message(String message) {
		this(null, message);
	}
	
	public Message(JabberId to, String message) {
		this.to = to;
		subjects = new ArrayList<>();
		bodies = new ArrayList<>();
		
		if (message != null) {
			bodies.add(new LangText(message));
		}
	}
	
	public List<LangText> getSubjects() {
		if (subjects == null)
			subjects = new ArrayList<>();
		
		return subjects;
	}

	public List<LangText> getBodies() {
		if (bodies == null)
			bodies = new ArrayList<>();
		
		return bodies;
	}

	public void setBodies(List<LangText> bodies) {
		this.bodies = bodies;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public Message.Type getType() {
		return type;
	}

	public void setType(Message.Type type) {
		this.type = type;
	}

	public void setSubjects(List<LangText> subjects) {
		this.subjects = subjects;
	}
	
	public String getText() {
		if (getBodies().isEmpty()) {
			return null;
		}
		
		return getBodies().get(0).getText();
	}
	
	public void setSubject(String subject) {
		getSubjects().add(new LangText(subject));
	}
	
	public String getSubject() {
		if (getSubjects().isEmpty()) {
			return null;
		}
		
		return getSubjects().get(0).getText();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Message[");
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
		
		if (subjects != null && subjects.size() > 0) {
			sb.append("subjects=[");
			sb.append(subjects.get(0).toString());
			if (subjects.size() > 1) {
				sb.append(",...");
			}
			sb.append("],");
		}
		
		if (bodies != null && bodies.size() > 0) {
			sb.append("bodies=[");
			sb.append(bodies.get(0).toString());
			if (bodies.size() > 1) {
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
