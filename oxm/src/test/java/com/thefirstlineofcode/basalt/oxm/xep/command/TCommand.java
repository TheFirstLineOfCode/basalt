package com.thefirstlineofcode.basalt.oxm.xep.command;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TXData;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="http://jabber.org/protocol/commands", localName="command")
public class TCommand {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/commands", "command");
	
	private String node;
	private String status;
	private String sessionid;
	
	private TXData xData;
	
	public String getNode() {
		return node;
	}
	
	public void setNode(String node) {
		this.node = node;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getSessionid() {
		return sessionid;
	}
	
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public TXData getXData() {
		return xData;
	}

	public void setXData(TXData xData) {
		this.xData = xData;
	}
	
}
