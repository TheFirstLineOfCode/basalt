package com.thefirstlineofcode.basalt.oxm.conversion.converters;

import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;

public class Int2EnumConverter implements IConverter<String, Object> {

	private Class<?> type;
	
	public Int2EnumConverter(Class<?> type) {
		this.type = type;
	}
	
	@Override
	public Object from(String value) throws ConversionException {
		if (type == null) {
			throw new RuntimeException("IntToEnumConverter needs a 'type' argument.");
		}
		
		int intValue = -1;
		try {
			intValue = Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Not an integer???");
		}
		
		Object[] objects = type.getEnumConstants();
		for (Object object : objects) {
			Enum<?> enumObj = (Enum<?>)object;
			if (enumObj.ordinal() == intValue)
				return enumObj;
		}
		
		throw new ConversionException(String.format("Can't convert %s to enum %s.", intValue, type.getName()));
	}

	@Override
	public String to(Object enumObj) throws ConversionException {
		return Integer.toString(((Enum<?>)enumObj).ordinal());
	}
}
