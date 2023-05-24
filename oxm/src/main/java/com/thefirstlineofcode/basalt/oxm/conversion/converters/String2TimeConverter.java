package com.thefirstlineofcode.basalt.oxm.conversion.converters;

import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;
import com.thefirstlineofcode.basalt.xmpp.datetime.Time;

public class String2TimeConverter implements IConverter<String, Time> {

	@Override
	public Time from(String string) throws ConversionException {
		try {
			return Time.parse(string);
		} catch (Exception e) {
			throw new ConversionException(String.format("%s isn't a valid time.", string), e);
		}
	}

	@Override
	public String to(Time time) throws ConversionException {
		return time.toString();
	}

}
