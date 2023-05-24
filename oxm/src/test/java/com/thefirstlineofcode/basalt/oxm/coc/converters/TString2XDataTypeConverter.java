package com.thefirstlineofcode.basalt.oxm.coc.converters;

import com.thefirstlineofcode.basalt.oxm.conversion.ConversionException;
import com.thefirstlineofcode.basalt.oxm.conversion.IConverter;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TXData;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TXData.Type;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;

public class TString2XDataTypeConverter implements IConverter<String, TXData.Type> {

	@Override
	public Type from(String obj) throws ConversionException {
		try {
			return TXData.Type.valueOf(obj.toUpperCase());
		} catch (Exception e) {
			throw new ProtocolException(new BadRequest(), e);
		}
	}

	@Override
	public String to(Type obj) throws ConversionException {
		return obj.toString();
	}

}
