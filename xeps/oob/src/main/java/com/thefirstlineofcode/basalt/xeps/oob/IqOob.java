package com.thefirstlineofcode.basalt.xeps.oob;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="jabber:iq:oob", localName="query")
public class IqOob {
public static final Protocol PROTOCOL = new Protocol("jabber:iq:oob", "query");
	
	@TextOnly
	private String url;
	
	@TextOnly
	private String desc;
	
	private String sid;
	
	public IqOob() {}
	
	public IqOob(String sid, String url) {
		this.sid = sid;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	
}