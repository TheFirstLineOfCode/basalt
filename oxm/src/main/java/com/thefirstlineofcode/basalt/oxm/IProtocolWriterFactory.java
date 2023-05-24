package com.thefirstlineofcode.basalt.oxm;

import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;

public interface IProtocolWriterFactory {
	IProtocolWriter create();
}
