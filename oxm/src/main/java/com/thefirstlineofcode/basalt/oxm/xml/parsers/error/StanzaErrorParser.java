package com.thefirstlineofcode.basalt.oxm.xml.parsers.error;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingFactory;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Stanza;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Message;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Presence;

public class StanzaErrorParser {
	public StanzaError parse(IParsingFactory parsingFactory, IParsingContext<?> context,
			XMLStreamReader reader) throws XMLStreamException {
		return doParse(parsingFactory, context, reader);
	}

	private StanzaError doParse(IParsingFactory parsingFactory, IParsingContext<?> context,
			XMLStreamReader reader) throws XMLStreamException {
		String senderXml = null, errorXml;
		
		int senderXmlStart = reader.getLocation().getCharacterOffset();
		
		int eventType = reader.next();
		
		if (eventType != XMLStreamConstants.START_ELEMENT) {
			throw new BadMessageException("Should be a start tag here.");
		}
		
		Protocol protocol = new Protocol(reader.getNamespaceURI(), reader.getLocalName());		
		
		int errorXmlStart = senderXmlStart;
		
		while (!StanzaError.PROTOCOL.equals(protocol)) {
			// sender message found
			senderXml = parseSenderMessage(reader, protocol, context.getMessage(), senderXmlStart);
			
			errorXmlStart = reader.getLocation().getCharacterOffset();
			
			eventType = reader.next();
			if (eventType != XMLStreamConstants.START_ELEMENT) {
				throw new BadMessageException("Start tag expected.");
			}
			
			protocol = new Protocol(reader.getNamespaceURI(), reader.getLocalName());
		}
		
		errorXml = getErrorXml(reader, context.getMessage(), errorXmlStart);
		
		eventType = reader.next();
		
		Protocol startTagProtocol = context.getProtocolChain().get(context.getProtocolChain().size() - 1);
		Protocol endTagProtocol = new Protocol(reader.getNamespaceURI(), reader.getLocalName());
		if (eventType != XMLStreamConstants.END_ELEMENT || !startTagProtocol.equals(endTagProtocol)) {
			throw new BadMessageException(String.format("End tag of protocol %s expected.", startTagProtocol));
		}
		
		Object object = parsingFactory.parse(errorXml);
		if (!(object instanceof StanzaError)) {
			throw new BadMessageException("Not a valid stanza error.");
		}
		
		Stanza stanza = (Stanza)context.getObject();
		StanzaError error = (StanzaError)object;
		if (stanza instanceof Presence) {
			error.setKind(StanzaError.Kind.PRESENCE);
		} else if (stanza instanceof Message) {
			error.setKind(StanzaError.Kind.MESSAGE);
		} else if (stanza instanceof Iq) {
			error.setKind(StanzaError.Kind.IQ);
		} else {
			throw new BadMessageException("Invalid stanza kind.");
		}
		
		error.setFrom(stanza.getFrom());
		error.setTo(stanza.getTo());
		error.setId(stanza.getId());
		error.setLang(stanza.getLang());
		
		error.setSenderMessage(senderXml);
		
		return error;
	}

	private String getErrorXml(XMLStreamReader reader, String message, int errorXmlStart) throws XMLStreamException {
		while (true) {
			int eventType = reader.next();
			
			if (eventType == XMLStreamConstants.END_ELEMENT) {
				if (StanzaError.PROTOCOL.equals(new Protocol(reader.getNamespaceURI(), reader.getLocalName()))) {
					int errorXmlEnd = reader.getLocation().getCharacterOffset();
					
					return message.substring(errorXmlStart, errorXmlEnd);
				}
			}
		}
	}

	private String parseSenderMessage(XMLStreamReader reader, Protocol protocol,
			String message, int senderXmlStart) throws XMLStreamException {
		while (true) {
			int eventType = reader.next();
			
			if (eventType == XMLStreamConstants.END_ELEMENT) {
				if (protocol.equals(new Protocol(reader.getNamespaceURI(), reader.getLocalName()))) {
					int senderXmlEnd = reader.getLocation().getCharacterOffset();
					
					return message.substring(senderXmlStart, senderXmlEnd);
				}
			}
		}
	}
}
