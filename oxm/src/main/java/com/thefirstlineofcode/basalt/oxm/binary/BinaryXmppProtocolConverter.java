package com.thefirstlineofcode.basalt.oxm.binary;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.thefirstlineofcode.basalt.oxm.binary.Element.NameAndValue;
import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.parsing.ParsingUtils;
import com.thefirstlineofcode.basalt.oxm.xml.IXmlStreamReaderFactory;
import com.thefirstlineofcode.basalt.oxm.xml.XmlStreamReaderFactory;

public class BinaryXmppProtocolConverter extends AbstractBinaryXmppProtocolConverter<XMLStreamReader> {
	
	private IXmlStreamReaderFactory streamReaderFactory;
	
	public BinaryXmppProtocolConverter() {
		streamReaderFactory = createXmlStreamReaderFactory();
	}

	@Override
	protected XMLStreamReader createXmlParser(String message) throws XMLStreamException {
		return streamReaderFactory.createXmlStreamReader(message);
	}

	private IXmlStreamReaderFactory createXmlStreamReaderFactory() {
		return new XmlStreamReaderFactory();
	}

	@Override
	protected Element readDocument(XMLStreamReader reader) {
		try {
			int eventType = reader.next();
			if (eventType != XMLStreamConstants.START_ELEMENT) {
				throw new BadMessageException("No element start tag.");
			}

			Element element = readProtocol(reader, null);
			
			if (reader.next() != XMLStreamConstants.END_DOCUMENT) {
				throw new BadMessageException("End document expected.");
			}
			
			return element;
		} catch (XMLStreamException e) {
			throw new BadMessageException("Can't parse XML document.", e);
		}
	}

	private Element readProtocol(XMLStreamReader reader, Element parent) throws XMLStreamException {
		return readElement(reader, parent);
	}

	@Override
	protected Element readElement(XMLStreamReader reader, Element parent) throws XMLStreamException {
		Element element = new Element();
		element.namespace = reader.getNamespaceURI();
		element.localName = reader.getLocalName();
		if (reader.getPrefix() != null && !reader.getPrefix().equals("")) {
			element.localName = reader.getPrefix() + ":" + element.localName;
		}
		
		element.attributes = readAttributes(reader);
		
		StringBuilder text = null;
		while (true) {
			int eventType = reader.next();
			if (eventType == XMLStreamConstants.CHARACTERS || eventType == XMLStreamConstants.CDATA) {
				text = ParsingUtils.appendText(reader, text);
			} else if (eventType == XMLStreamConstants.END_ELEMENT) {
				element.text = text == null ? null : text.toString();
				
				if (parent != null) {
					parent.children.add(element);
					
					if (parent.namespace != null && parent.namespace.equals(element.namespace))
						element.namespace = null;
				}
				
				break;
			} else if (eventType == XMLStreamConstants.START_ELEMENT) {
				String parentNamespace = null;
				if (parent != null) {
					parentNamespace = element.namespace;
				}
				String namespace = reader.getNamespaceURI();
				
				if (namespace != null && !"".equals(namespace) && !namespace.equals(parentNamespace)) {
					readProtocol(reader, element);
				} else {
					readElement(reader, element);
				}
			} else {
				throw ParsingUtils.newParsingException(reader, "Unsupported XML event type.");
			}
		}
		
		return element;
	}

	private List<NameAndValue> readAttributes(XMLStreamReader reader) {
		if (reader.getAttributeCount() == 0) {
			return new ArrayList<>();
		}
		
		List<NameAndValue> attributes = new ArrayList<>();
		for (int i = 0; i < reader.getAttributeCount(); i++) {
			String prefix = reader.getAttributePrefix(i);
			if ("".equals(prefix)) {
				prefix = null;
			}
			
			attributes.add(new NameAndValue(prefix == null ? reader.getAttributeLocalName(i) : prefix + ":" + reader.getAttributeLocalName(i),
					reader.getAttributeValue(i)));
		}
		
		return attributes;
	}
}
