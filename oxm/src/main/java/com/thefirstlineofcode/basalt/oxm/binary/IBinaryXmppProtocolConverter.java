package com.thefirstlineofcode.basalt.oxm.binary;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public interface IBinaryXmppProtocolConverter {
	byte[] toBinary(String message) throws BxmppConversionException;
	String toXml(byte[] message) throws BxmppConversionException;
	Protocol readProtocol(byte[] data);
}
