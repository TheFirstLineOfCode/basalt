package com.thefirstlineofcode.basalt.oxm.xep.oob;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.CustomValidator;
import com.thefirstlineofcode.basalt.oxm.conversion.validators.TUrlValidator;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="jabber:x:oob", localName="x")
public class TXOob {
	public static final Protocol PROTOCOL = new Protocol("jabber:x:oob", "x");
	
	@CustomValidator(TUrlValidator.class)
	@TextOnly
	private String url;
	
	@TextOnly
	private String desc;
	
	public TXOob() {}
	
	public TXOob(String url) {
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
