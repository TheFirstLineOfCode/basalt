package com.thefirstlineofcode.basalt.oxm.parsing;

import java.io.StringWriter;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;

public final class ParsingUtils {
	private static final char[] unescaped = {'"', '&', '<', '>', '\''};
	private static final String[] escaped = {"quot", "amp", "lt", "gt", "apos"};
	
	private ParsingUtils() {}
	
	public static String processLangTextAttributes(List<Attribute> attributes) {
		if (attributes.size() == 0)
			return null;
		
		if (attributes.size() != 1)
			throw new BadMessageException("Only allow one attribute 'xml:lang' here.");
		
		Attribute attribute = attributes.get(0);
		if (attribute.getValue().getType() != Value.Type.STRING || !LangText.NAME_LANG_TEXT.equals(attribute.getName())) {
			throw new BadMessageException(String.format("Invalid attribute '%s'",
					attribute.getName()));
		}
		
		return attribute.getValue().toString();
	}
	
	public static BadMessageException newParsingException(XMLStreamReader streamReader, String info) {
		return new BadMessageException(String.format("%s. offset: %d", info, streamReader.
				getLocation().getCharacterOffset()));
	}
	
	public static ProtocolException newStanzaParsingException(XMLStreamReader streamReader, String info) {
		return new ProtocolException(new BadRequest(String.format("%s. offset: %d", info, streamReader.getLocation().getCharacterOffset())));
	}
	
	public static StringBuilder appendText(XMLStreamReader reader, StringBuilder text) {
		String textFragment = reader.getText().trim();
		// if the characters are white space, ignore them
		if ("".equals(textFragment)) {
			return text;
		}
		
		if (text == null) {
			text = new StringBuilder();
		}
		
		text.append(textFragment);
		
		return text;
	}
	
	public static String escapeXml(String string) {
		if (string == null)
			return null;
		
		StringWriter writer = new StringWriter();
loop:
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			for (int j = 0; j < unescaped.length; j++) {
				if (c == unescaped[j]) {
					writer.write('&');
					writer.write(escaped[j]);
					writer.write(';');
					
					continue loop;
				}
			}
			
			writer.write(c);
		}
		
		return writer.toString();
	}
	
	public static String unescapeXml(String string) {
		if (string == null)
			return null;
		
		StringWriter writer = new StringWriter();

		int i, j, k;
loop:
		for (i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c != '&') {
				writer.write(c);
				continue;
			}
			
			for (j = i + 1; j < string.length(); j++) {
				c = string.charAt(j);
				if (c == '&') {
					writer.write(string.substring(i, j));
					i = j - 1;
					continue loop;
				} else if (c == ';') {
					String escapedString = string.substring(i + 1, j);
					for (k = 0; k < escaped.length; k++) {
						if (escaped[k].equals(escapedString)) {
							writer.write(unescaped[k]);
							i = j;
							continue loop;
						}
					}
					
					writer.write(string.substring(i, j + 1));
					i = j;
					continue loop;
				} else {
					continue;
				}
			}
			
			if (i < string.length() - 1) {
				writer.write(string.substring(i));
				i = j;
			}
		}
		
		return writer.toString();
	}
}
