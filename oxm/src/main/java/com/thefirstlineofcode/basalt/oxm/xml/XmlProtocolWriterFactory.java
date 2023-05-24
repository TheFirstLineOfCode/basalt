package com.thefirstlineofcode.basalt.oxm.xml;

import com.thefirstlineofcode.basalt.oxm.IProtocolWriterFactory;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;

public class XmlProtocolWriterFactory implements IProtocolWriterFactory {

	@Override
	public IProtocolWriter create() {
		return new XmlProtocolWriter();
	}

}
