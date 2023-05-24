package com.thefirstlineofcode.basalt.xeps.address;

import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2Enum;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2JabberId;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.Validate;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.ValidationClass;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;

@ValidationClass
public class Address {
	public enum Type {
		TO,
		CC,
		BCC,
		REPLYTO,
		REPLYROOM,
		NOREPLY
	}
	
	@String2JabberId
	private JabberId jid;
	private String uri;
	private String node;
	private String desc;
	private Boolean delivered;
	@String2Enum(Address.Type.class)
	private Type type;
	
	public JabberId getJid() {
		return jid;
	}
	
	public void setJid(JabberId jid) {
		this.jid = jid;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getNode() {
		return node;
	}
	
	public void setNode(String node) {
		this.node = node;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Boolean getDelivered() {
		return delivered;
	}
	
	public void setDelivered(Boolean delivered) {
		this.delivered = delivered;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	@Validate("/")
	public void validAddress(Address address) {
		if (Boolean.FALSE.equals(delivered)) {
			throw new ProtocolException(new BadRequest("value of attribute 'delivered' must be set to 'true'"));
		}
	}
	
}
