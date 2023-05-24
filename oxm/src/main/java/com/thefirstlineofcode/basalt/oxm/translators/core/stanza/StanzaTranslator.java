package com.thefirstlineofcode.basalt.oxm.translators.core.stanza;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Attributes;
import com.thefirstlineofcode.basalt.oxm.parsing.FlawedProtocolObject;
import com.thefirstlineofcode.basalt.oxm.translating.IProtocolWriter;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslatingFactory;
import com.thefirstlineofcode.basalt.oxm.translating.ITranslator;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;


public abstract class StanzaTranslator<T extends Stanza> implements ITranslator<T> {

	@Override
	public String translate(T stanza, IProtocolWriter writer, ITranslatingFactory translatingFactory) {
		writer.writeProtocolBegin(getProtocol());
		writer.writeAttributes(readCommonAttributes(stanza));
		
		translateSpecific(stanza, writer, translatingFactory);
		
		for (Object object : stanza.getObjects()) {
			if (object instanceof FlawedProtocolObject)
				continue;
			
			String objectString = translatingFactory.translate(object);
			
			if (objectString != null)
				writer.writeString(objectString);				
		}
		
		writer.writeProtocolEnd();
		
		return writer.toString();
	}

	private List<Attribute> readCommonAttributes(T stanza) {
		return new Attributes().
			add("from", stanza.getFrom()).
			add("to", stanza.getTo()).
			add("id", stanza.getId()).
			add(LangText.PREFIX_LANG_TEXT, LangText.LOCAL_NAME_LANG_TEXT, stanza.getLang()).
			add("type", getType(stanza)).
			get();
	}
	
	protected abstract String getType(T stanza);
	protected abstract void translateSpecific(T stanza, IProtocolWriter writer, ITranslatingFactory translatingFactory);
}
