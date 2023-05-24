package com.thefirstlineofcode.basalt.xmpp.core.stanza;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class Stanza {
	
	private static final String STRING_DASH = "-";
	private static final String STRING_UNDERLINE = "_";
	protected JabberId from;
	protected JabberId to;
	protected String id;
	protected String lang;
	protected List<Object> objects;
	protected Map<Class<?>, Protocol> objectProtocols;
	protected String originalMessage;
	
	public Stanza() {
	}
	
	public JabberId getFrom() {
		return from;
	}

	public String getId() {
		return id;
	}

	public JabberId getTo() {
		return to;
	}

	public String getLang() {
		return lang;
	}

	public void setFrom(JabberId from) {
		this.from = from;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTo(JabberId to) {
		this.to = to;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setObjects(List<Object> objects) {
		this.objects = objects;
	}
	
	public List<Object> getObjects() {
		if (objects == null) {
			objects = new ArrayList<>(0);
		}
		
		return objects;
	}
	
	public void setObject(Object object) {
		if (objects != null) {
			objects.clear();
		} else {
			objects = new ArrayList<>(1);
		}
		
		objects.add(object);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject() {
		if (objects == null || objects.isEmpty())
			return null;
		
		return (T)objects.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<T> clazz) {
		if (objects == null || objects.isEmpty()) {
			return null;
		}
		
		for (Object object : objects) {
			if (object.getClass() == clazz) {
				return (T)object;
			}
		}
		
		return null;
	}
	
	public void setOriginalMessage(String originalMessage) {
		this.originalMessage = originalMessage;
	}
	
	public String getOriginalMessage() {
		return originalMessage;
	}
	
	public void setObjectProtocols(Map<Class<?>, Protocol> objectProtocols) {
		this.objectProtocols = objectProtocols;
	}
	
	public Map<Class<?>, Protocol> getObjectProtocols() {
		if (objectProtocols == null) {
			objectProtocols = new HashMap<>();
		}
		
		return objectProtocols;
	}
	
	public Protocol getObjectProtocol(Class<?> clazz) {
		if (objectProtocols == null)
			return null;
		
		return objectProtocols.get(clazz);
	}
	
	public static String generateId() {
		return generateId(null);
	}
	
	public static String generateId(String prefix) {
		return generateId(prefix, 12);
	}
	
	public static String generateId(String prefix, int uuidLength) {
		if (uuidLength <= 0) {
			throw new IllegalArgumentException("length mustn't be negative or zero");
		}
		
		if (prefix != null) {
			if (prefix.endsWith(STRING_UNDERLINE) || prefix.endsWith(STRING_DASH)) {
				return prefix + getUUIDString(uuidLength);
			}
			
			return String.format("%s_%s", prefix, getUUIDString(uuidLength));
		}
		
		return getUUIDString(uuidLength);
	}

	private static String getUUIDString(int length) {
		if (length <= 16) {
			return String.format("%016X", java.util.UUID.randomUUID().getLeastSignificantBits()).substring(16 - length, 16);
		}
		
		if (length > 32) {
			length = 32;
		}
		
		UUID uuid = UUID.randomUUID();
		String uuidHexString = String.format("%016X%016X", uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
				
		return uuidHexString.substring(32 - length, 32); 
	}

}
