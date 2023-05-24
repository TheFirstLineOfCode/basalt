package com.thefirstlineofcode.basalt.oxm;

import com.thefirstlineofcode.basalt.oxm.parsers.im.MessageParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsers.im.PresenceParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translators.im.MessageTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.translators.im.PresenceTranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Message;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Presence;

public class StandardOxmFactory extends MinimumOxmFactory {
	public StandardOxmFactory(IParsingFactory parsingFactory, ITranslatingFactory translatingFactory) {
		super(parsingFactory, translatingFactory);
		registerPresenceAndMessageProtocols();
	}

	private void registerPresenceAndMessageProtocols() {
		// register parsers
		register(ProtocolChain.first(Presence.PROTOCOL), new PresenceParserFactory());
		register(ProtocolChain.first(Message.PROTOCOL), new MessageParserFactory());
		
		// register translators
		register(Presence.class, new PresenceTranslatorFactory());
		register(Message.class, new MessageTranslatorFactory());
	}
}
