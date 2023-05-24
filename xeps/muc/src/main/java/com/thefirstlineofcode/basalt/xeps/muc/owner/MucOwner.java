package com.thefirstlineofcode.basalt.xeps.muc.owner;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.Validate;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.ValidationClass;
import com.thefirstlineofcode.basalt.xeps.muc.user.Destroy;
import com.thefirstlineofcode.basalt.xeps.xdata.XData;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;

@ProtocolObject(namespace="http://jabber.org/protocol/muc#owner", localName="query")
@ValidationClass
public class MucOwner {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/muc#owner", "query");
	
	private XData xData;
	private Destroy destroy;
	
	public XData getXData() {
		return xData;
	}
	
	public void setXData(XData xData) {
		this.xData = xData;
	}
	
	public Destroy getDestroy() {
		return destroy;
	}
	
	public void setDestroy(Destroy destroy) {
		this.destroy = destroy;
	}
	
	@Validate("/")
	public void validateMucOwner(MucOwner mucOwner) {
		if (mucOwner.getXData() != null && mucOwner.getDestroy() != null) {
			throw new ProtocolException(new BadRequest("only one child element('xdata' or 'destroy') allowed"));
		}
	}
	
}
