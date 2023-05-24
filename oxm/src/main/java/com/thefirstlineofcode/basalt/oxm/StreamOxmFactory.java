package com.thefirstlineofcode.basalt.oxm;

import com.thefirstlineofcode.basalt.oxm.annotation.AnnotatedParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsers.core.stream.StreamParser;
import com.thefirstlineofcode.basalt.oxm.parsers.error.ErrorParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsers.error.StreamErrorDetailsParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translators.core.stream.StreamTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.translators.error.StreamErrorTranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.IError;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Stream;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.StreamError;

public class StreamOxmFactory extends OxmFactory {

	public StreamOxmFactory(IParsingFactory parsingFactory, ITranslatingFactory translatingFactory) {
		super(parsingFactory, translatingFactory);
		registerStreamNegotiationProtocols();
	}

	private void registerStreamNegotiationProtocols() {
		// register parsers;
		register(ProtocolChain.first(Stream.PROTOCOL), new AnnotatedParserFactory<>(StreamParser.class));
		register(ProtocolChain.first(StreamError.PROTOCOL), new ErrorParserFactory<>(IError.Type.STREAM));
		register(ProtocolChain.first(StreamError.PROTOCOL).next(StreamError.PROTOCOL_ERROR_DEFINED_CONDITION),
				new StreamErrorDetailsParserFactory());
		
		// register translators
		register(Stream.class, new StreamTranslatorFactory());
		register(StreamError.class, new StreamErrorTranslatorFactory());
	}

}
