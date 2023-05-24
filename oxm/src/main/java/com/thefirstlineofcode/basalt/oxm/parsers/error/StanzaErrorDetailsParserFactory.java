package com.thefirstlineofcode.basalt.oxm.parsers.error;

import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError;

public class StanzaErrorDetailsParserFactory extends ErrorDetailsParserFactory {

	@Override
	public Protocol getProtocol() {
		return StanzaError.PROTOCOL;
	}

}
