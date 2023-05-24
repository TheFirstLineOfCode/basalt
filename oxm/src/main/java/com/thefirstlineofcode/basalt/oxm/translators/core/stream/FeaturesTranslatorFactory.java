package com.thefirstlineofcode.basalt.oxm.translators.core.stream;

import com.thefirstlineofcode.basalt.oxm.parsing.FlawedProtocolObject;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatorFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Feature;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Features;

public class FeaturesTranslatorFactory implements ITranslatorFactory<Features> {
	private static final ITranslator<Features> translator = new FeaturesTranslator();

	@Override
	public Class<Features> getType() {
		return Features.class;
	}

	@Override
	public ITranslator<Features> create() {
		return translator;
	}
	
	private static class FeaturesTranslator implements ITranslator<Features> {

		@Override
		public Protocol getProtocol() {
			return Features.PROTOCOL;
		}

		@Override
		public String translate(Features features, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
			writer.writeProtocolBegin(Features.PROTOCOL);
			
			for (Feature feature : features.getFeatures()) {
				if (feature instanceof FlawedProtocolObject)
					continue;
				
				writer.writeString(translatingFactory.translate(feature));
			}
			
			writer.writeProtocolEnd();
			
			return writer.getDocument();
		}
		
	}

}
