package com.thefirstlineofcode.basalt.oxm.coc;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="urn:leps:basalt:test:lora", localName="lora-address")
public class LoraAddress {
	public static final Protocol PROTOCOL = new Protocol("urn:leps:basalt:test:lora", "lora-address");
	
	private byte[] address;
	private byte channel;
	
	public LoraAddress() {}
	
	public LoraAddress(byte[] address, byte channel) {
		this.address = address;
		this.channel = channel;
	}
	
	public byte[] getAddress() {
		return address;
	}
	public void setAddress(byte[] address) {
		this.address = address;
	}
	public byte getChannel() {
		return channel;
	}
	public void setChannel(byte channel) {
		this.channel = channel;
	}
	
	
}
