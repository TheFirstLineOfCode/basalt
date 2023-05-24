package com.thefirstlineofcode.basalt.oxm;

import com.thefirstlineofcode.basalt.oxm.annotation.AnnotatedParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsers.core.stanza.IqParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsers.core.stream.StreamParser;
import com.thefirstlineofcode.basalt.oxm.parsers.error.ErrorParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsers.error.StanzaErrorDetailsParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsers.error.StreamErrorDetailsParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translators.core.stanza.IqTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.translators.core.stream.StreamTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.translators.error.StanzaErrorTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.translators.error.StreamErrorTranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.IError;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Stream;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.StreamError;

public class MinimumOxmFactory extends StreamOxmFactory {
	public MinimumOxmFactory(IParsingFactory parsingFactory, ITranslatingFactory translatingFactory) {
		super(parsingFactory, translatingFactory);
		registerMinimumProtocols();
	}

	private void registerMinimumProtocols() {
		// register parsers;
		register(ProtocolChain.first(Iq.PROTOCOL), new IqParserFactory());
		register(ProtocolChain.first(Stream.PROTOCOL), new AnnotatedParserFactory<>(StreamParser.class));
		register(StreamError.class, new StreamErrorTranslatorFactory());
		register(ProtocolChain.first(StreamError.PROTOCOL), new ErrorParserFactory<>(IError.Type.STREAM));
		register(ProtocolChain.first(StreamError.PROTOCOL).next(StreamError.PROTOCOL_ERROR_DEFINED_CONDITION),
				new StreamErrorDetailsParserFactory());
		register(ProtocolChain.first(StanzaError.PROTOCOL), new ErrorParserFactory<>(IError.Type.STANZA));
		register(ProtocolChain.first(StanzaError.PROTOCOL).next(StanzaError.PROTOCOL_ERROR_DEFINED_CONDITION),
				new StanzaErrorDetailsParserFactory());
		
		// register translators
		register(Iq.class, new IqTranslatorFactory());
		register(Stream.class, new StreamTranslatorFactory());
		register(StreamError.class, new StreamErrorTranslatorFactory());
		register(StanzaError.class, new StanzaErrorTranslatorFactory());
	}
}
