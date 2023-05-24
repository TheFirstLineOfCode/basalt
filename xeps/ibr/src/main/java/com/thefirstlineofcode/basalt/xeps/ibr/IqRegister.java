package com.thefirstlineofcode.basalt.xeps.ibr;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.xeps.oob.XOob;
import com.thefirstlineofcode.basalt.xeps.xdata.XData;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="jabber:iq:register", localName="query")
public class IqRegister {
	public static final Protocol PROTOCOL = new Protocol("jabber:iq:register", "query");
	
	private Object register;
	
	private XData xData;
	private XOob oob;
	
	public Object getRegister() {
		return register;
	}
	
	public void setRegister(Object register) {
		this.register = register;
	}
	
	public XData getXData() {
		return xData;
	}
	
	public void setXData(XData xData) {
		this.xData = xData;
	}
	
	public XOob getOob() {
		return oob;
	}
	
	public void setOob(XOob oob) {
		this.oob = oob;
	}
	
}
