package com.thefirstlineofcode.basalt.oxm.conversion.converters;

import java.text.ParseException;

import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;
import com.thefirstlineofcode.basalt.xmpp.datetime.Date;

public class String2DateConverter implements IConverter<String, Date> {

	@Override
	public Date from(String string) throws ConversionException {
		try {
			return Date.parse(string);
		} catch (ParseException e) {
			throw new ConversionException(String.format("%s isn't a valid date.", string), e);
		}
	}

	@Override
	public String to(Date date) throws ConversionException {
		return date.toString();
	}

}
