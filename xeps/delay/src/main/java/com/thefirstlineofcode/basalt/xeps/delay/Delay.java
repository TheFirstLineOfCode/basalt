package com.thefirstlineofcode.basalt.xeps.delay;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2DateTime;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.datetime.DateTime;

@ProtocolObject(namespace="urn:xmpp:delay", localName="delay")
public class Delay {
	public static final Protocol PROTOCOL = new Protocol("urn:xmpp:delay", "delay");
	
	@String2JabberId
	private JabberId from;
	@String2DateTime
	private DateTime stamp;
	
	public Delay() {}
	
	public Delay(JabberId from, DateTime stamp) {
		this.from = from;
		this.stamp = stamp;
	}
	
	public JabberId getFrom() {
		return from;
	}
	
	public void setFrom(JabberId from) {
		this.from = from;
	}
	
	public DateTime getStamp() {
		return stamp;
	}
	
	public void setStamp(DateTime stamp) {
		this.stamp = stamp;
	}

}
