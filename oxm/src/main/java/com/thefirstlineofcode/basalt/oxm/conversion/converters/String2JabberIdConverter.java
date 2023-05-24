package com.thefirstlineofcode.basalt.oxm.conversion.converters;

import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.MalformedJidException;

public class String2JabberIdConverter implements IConverter<String, JabberId> {

	@Override
	public JabberId from(String jidString) throws ConversionException {
		try {
			return JabberId.parse(jidString);
		} catch (MalformedJidException e) {
			throw new ConversionException("Not a valid jid.", e);
		}
	}

	@Override
	public String to(JabberId jid) throws ConversionException {
		return jid.toString();
	}

}
