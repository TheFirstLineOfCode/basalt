package com.thefirstlineofcode.basalt.xeps.address;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(namespace="http://jabber.org/protocol/address", localName="addresses")
public class Addresses {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/address", "addresses");
	
	@Array(Address.class)
	private List<Address> addresses;

	public List<Address> getAddresses() {
		if (addresses == null) {
			addresses = new ArrayList<>();
		}
		
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

}
