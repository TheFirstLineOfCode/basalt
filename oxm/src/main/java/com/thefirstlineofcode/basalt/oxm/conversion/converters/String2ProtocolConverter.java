package com.thefirstlineofcode.basalt.oxm.conversion.converters;

import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class String2ProtocolConverter implements IConverter<String, Protocol> {

	@Override
	public Protocol from(String string) throws ConversionException {
		try {
			return Protocol.parse(string);			
		} catch (IllegalArgumentException e) {
			throw new ConversionException("Not a illegal protocol string.");
		}
	}

	@Override
	public String to(Protocol protocol) throws ConversionException {
		return protocol.getProtocolString();
	}

}
