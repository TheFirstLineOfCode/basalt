package com.thefirstlineofcode.basalt.oxm.xml;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XmlStreamReaderFactory implements IXmlStreamReaderFactory {
	private XMLInputFactory xmlInputFactory;
	
	static {
		System.setProperty("javax.xml.stream.XMLInputFactory", "com.sun.xml.internal.stream.XMLInputFactoryImpl");
	}
	
	public XmlStreamReaderFactory() {
		xmlInputFactory = createXmlInputFactory();
	}
	
	private XMLInputFactory createXmlInputFactory() {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		
	    xmlInputFactory.setProperty("javax.xml.stream.isCoalescing", true);
	    xmlInputFactory.setProperty("javax.xml.stream.isValidating", false);
	    
	    return xmlInputFactory;
	}
	
	@Override
	public XMLStreamReader createXmlStreamReader(String message)throws XMLStreamException {
		return xmlInputFactory.createXMLStreamReader(new StringReader(message));
	}
}
