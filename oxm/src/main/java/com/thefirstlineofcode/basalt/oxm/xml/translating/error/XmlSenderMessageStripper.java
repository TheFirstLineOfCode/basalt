package com.thefirstlineofcode.basalt.oxm.xml.translating.error;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.thefirstlineofcode.basalt.oxm.translators.error.ISenderMessageStripper;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Message;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Presence;

public class XmlSenderMessageStripper implements ISenderMessageStripper {
	private static XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();

	@Override
	public String strip(String originalMessage) {
		try {
			return doStrip(originalMessage);
		} catch (Exception e) {
			return null;
		}
	}

	private String doStrip(String originalMessage) throws XMLStreamException {
		XMLStreamReader reader = null;
		
		try {
			reader = xmlInputFactory.createXMLStreamReader(new StringReader(originalMessage));
			
			int eventType = reader.next();
			
			if (eventType != XMLStreamConstants.START_ELEMENT) {
				return null;
			}
			
			Protocol protocol = new Protocol(reader.getNamespaceURI(), reader.getLocalName());
			
			if (!isStanza(protocol))
				return null;
			
			int senderXmlStart = reader.getLocation().getCharacterOffset();
			
			if (reader.getAttributeCount() != 0) {
				for (int i = 0; i < reader.getAttributeCount(); i++) {
					String attributeName = reader.getAttributeLocalName(i);
					String attributeValue =	reader.getAttributeValue(i);
					
					if ("type".equals(attributeName) && "error".equals(attributeValue)) {
						return null;
					}
				}
			}
			
			int senderXmlEnd = senderXmlStart;
			while (true) {
				senderXmlEnd = reader.getLocation().getCharacterOffset();
				
				eventType = reader.next();
				
				if (eventType == XMLStreamConstants.END_ELEMENT &&
						protocol.equals(new Protocol(reader.getNamespaceURI(), reader.getLocalName()))) {
					break;
				}
			}
			
			return originalMessage.substring(senderXmlStart, senderXmlEnd);	
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException e) {
					// ignore
				}
			}
		}
	}

	private boolean isStanza(Protocol protocol) {
		return Presence.PROTOCOL.equals(protocol) ||
			Message.PROTOCOL.equals(protocol) ||
				Iq.PROTOCOL.equals(protocol);
	}

}
