package com.thefirstlineofcode.basalt.oxm.conversion.converters;

import java.util.Date;

import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;

public class String2UtilDateConverter implements IConverter<String, Date> {

	@Override
	public Date from(String string) throws ConversionException {
		return new Date(Long.parseLong(string));
	}

	@Override
	public String to(Date date) throws ConversionException {
		return Long.toString(date.getTime());
	}

}
