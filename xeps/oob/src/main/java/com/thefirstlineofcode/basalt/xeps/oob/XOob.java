package com.thefirstlineofcode.basalt.xeps.oob;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="jabber:x:oob", localName="x")
public class XOob {
public static final Protocol PROTOCOL = new Protocol("jabber:x:oob", "x");
	
	@TextOnly
	private String url;
	
	@TextOnly
	private String desc;
	
	public XOob() {}
	
	public XOob(String url) {
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
	
}
