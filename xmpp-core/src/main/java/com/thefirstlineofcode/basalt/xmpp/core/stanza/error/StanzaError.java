package com.thefirstlineofcode.basalt.xmpp.core.stanza.error;

import com.thefirstlineofcode.basalt.xmpp.core.IError;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Message;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Presence;

public class StanzaError extends Stanza implements IError {
	public enum Kind {
		PRESENCE,
		MESSAGE,
		IQ
	}
	
	public enum Type {
		CANCEL,
		CONTINUE,
		MODIFY,
		AUTH,
		WAIT
	}
	
	public static final Protocol PROTOCOL = new Protocol("error");
	public static final String NAMESPACE_STANZA_ERROR_CONTEXT = "urn:ietf:params:xml:ns:xmpp-stanzas";
	public static final Protocol PROTOCOL_ERROR_DEFINED_CONDITION = new Protocol(NAMESPACE_STANZA_ERROR_CONTEXT, "*");
	
	protected StanzaError.Type type;
	protected String definedCondition;
	protected LangText text;
	protected Object applicationSpecificCondition;
	protected String senderMessage;
	protected Kind kind;
	
	public StanzaError() {
		this(null);
	}
	
	public StanzaError(StanzaError.Type type) {
		this(type, null);
	}
	
	public StanzaError(StanzaError.Type type, String definedCondition) {
		this(type, definedCondition, null);
	}
	
	public StanzaError(StanzaError.Type type, String definedCondition, LangText text) {
		this.type = type;
		this.definedCondition = definedCondition;
		this.text = text;
	}
	
	// stanza errors are recoverable(rfc3920 9.3)
	@Override
	public boolean closeStream() {
		return false;
	}
	
	@Override
	public String getDefinedCondition() {
		return definedCondition;
	}
	
	@Override
	public LangText getText() {
		return text;
	}
	
	@Override
	public Object getApplicationSpecificCondition() {
		return applicationSpecificCondition;
	}
	
	@Override
	public void setApplicationSpecificCondition(Object applicationSpecificCondition) {
		this.applicationSpecificCondition = applicationSpecificCondition;
	}
	
	public StanzaError.Type getType() {
		return type;
	}
	
	public void setSenderMessage(String senderMessage) {
		this.senderMessage = senderMessage;
	}
	
	public String getSenderMessage() {
		return senderMessage;
	}
	
	public Kind getKind() {
		return kind;
	}
	
	public void setKind(Kind kind) {
		this.kind = kind;
	}
	
	public void setType(StanzaError.Type type) {
		this.type = type;
	}

	@Override
	public void setDefinedCondition(String definedCondition) {
		this.definedCondition = definedCondition;
	}

	@Override
	public void setText(LangText text) {
		this.text = text;
	}
	
	public static <T extends StanzaError> T create(Stanza stanza, Class<T> type) {
		return create(stanza, type, null);
	}
	
	public static <T extends StanzaError> T create(Stanza stanza, Class<T> type, String text) {
		return create(stanza, type, text, null);
	}
	
	public static <T extends StanzaError> T create(Stanza stanza, Class<T> type, String text, String lang) {
		T error = null;
		try {
			error = type.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(String.format("Can't initialize stanza error. Stanza error type: %s", type.getName()), e);
		}
		
		error.setId(stanza.getId());
		if (stanza instanceof Iq) {
			error.setKind(StanzaError.Kind.IQ);
		} else if (stanza instanceof Message) {
			error.setKind(StanzaError.Kind.MESSAGE);
		} else if (stanza instanceof Presence) {
			error.setKind(StanzaError.Kind.PRESENCE);
		} else {
			throw new RuntimeException(String.format("Unsupported stanza type. Stanza type: %s", stanza.getClass().getName()));
		}
		
		if (stanza.getFrom() != null) {
			error.setTo(stanza.getFrom());
		}
		
		if (text != null) {
			error.setText(new LangText(text, lang));
		}
		
		return error;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Stanza Error[");
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
		
		if (definedCondition != null) {
			sb.append("defined-condition='").append(definedCondition).append("',");
		}
		
		if (text != null) {
			sb.append("text='").append(text).append("',");
		}
		
		if (applicationSpecificCondition != null) {
			sb.append("application-specific-condition=").append(applicationSpecificCondition.toString()).append(",");
		}
		
		if (sb.charAt(sb.length() - 1) == ',') {
			sb.deleteCharAt(sb.length() - 1);
		}
		
		sb.append(']');
		
		return sb.toString();
	}
}
