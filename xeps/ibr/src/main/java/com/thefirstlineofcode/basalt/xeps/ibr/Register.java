package com.thefirstlineofcode.basalt.xeps.ibr;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Feature;

@ProtocolObject(namespace="http://jabber.org/features/iq-register", localName="register")
public class Register implements Feature {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/features/iq-register", "register");
}
