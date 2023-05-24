package com.thefirstlineofcode.basalt.oxm.translators.core.stream;

import com.thefirstlineofcode.basalt.oxm.Attributes;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.Constants;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Stream;

public class StreamTranslatorFactory implements ITranslatorFactory<Stream> {
	private static final ITranslator<Stream> translator = new StreamTranslator();

	@Override
	public Class<Stream> getType() {
		return Stream.class;
	}

	@Override
	public ITranslator<Stream> create() {
		return translator;
	}

	private static class StreamTranslator implements ITranslator<Stream> {
		private static final String XML_CLOSE_STREAM = "</stream:stream>";

		@Override
		public Protocol getProtocol() {
			return Stream.PROTOCOL;
		}

		@Override
		public String translate(Stream stream, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			if (stream.isClose()) {
				return XML_CLOSE_STREAM;
			}

			writer.writeProtocolBegin(Stream.PROTOCOL);

			String defaultNamespace = stream.getDefaultNamespace();
			if (defaultNamespace == null) {
				defaultNamespace = Constants.C2S_DEFAULT_NAMESPACE;
			}

			writer.writeAttributes(new Attributes().add("xmlns", defaultNamespace).add("from", stream.getFrom())
					.add("to", stream.getTo()).add("id", stream.getId()).add("version", stream.getVersion())
					.add("lang", stream.getLang()).get());

			String message = writer.getDocument();

			return message + ">";
		}

	}
}
