package com.thefirstlineofcode.basalt.oxm.xml.preprocessing;

import com.thefirstlineofcode.basalt.oxm.preprocessing.AbstractTextMessagePreprocessor;

public class XmlMessagePreprocessor extends AbstractTextMessagePreprocessor {
	private static final char[] ELEMENT_NAME_END_FLAGS = new char[] {' ', '>', '/'};
	private static final char[] ATTRIBUTE_NAME_END_FLAGS = new char[] {'=', ' '};
	private static final char[] TEXT_END_FLAGS = new char[] {'<'};
	private static final char[] CDATA_START_CHARS = "<![CDATA[".toCharArray();
	private static final char[] OPEN_STREAM_START_CHARS = "<stream:stream".toCharArray();
	private static final char[] CLOSE_STREAM_CHARS = "</stream:stream>".toCharArray();
	
	private static final char[] XML_DECLARATION_START_CHARS = "<?xml ".toCharArray();
	
	private int level = 0;
	
	public XmlMessagePreprocessor() {
		super();
	}
	
	public XmlMessagePreprocessor(int maxBufferSize) {
		super();
		
		this.maxBufferSize = maxBufferSize;
	}
	
	private boolean parseElement() {
		if (!findNextNonWhitespaceChar(false).found) {
			return false;
		}
		
		if (buffer[index] != '<') {
			throw newXmlNotWellFormedException();
		}
		
		level++;
		
		int elementNameStart = index + 1;
		if (!parseElementName())
			return false;
		
		String elementName = new String(buffer, elementNameStart, index - elementNameStart);
		
		if (!parseAttributes())
			return false;
		
		if (buffer[index] == '/') {
			if (!parseEmptyElementTail())
				return false;
			
			level--;
			
			if (level == 0) {
				messageFound();
			}
			
			return true;
		} else { // buffer[index] == '>'
			if (!findNextNonWhitespaceChar().found) {
				return false;
			}
			
			while (true) {
				if (buffer[index] == '<') {
					if (!inRange())
						return false;
					
					if (buffer[index + 1] == '!') {
						index++;
						
						if (!parseCData())
							return false;
						
						if (!findNextNonWhitespaceChar().found) {
							return false;
						}
						
						continue;
					}
					
					if (buffer[index + 1] != '/') {
						if (!parseElement())
							return false;
						
						if (!findNextNonWhitespaceChar().found) {
							return false;
						}
						
						continue;
					}
					
					if (!parseElementEndTag(elementName))
						return false;
					
					level--;
					
					if (level == 0) {
						messageFound();
					}
					
					return true;
				} else {
					// text found
					if (!parseText()) {
						return false;
					}
				}
			}
		}
	}

	private boolean isXmlDeclaration() {
		int oldIndex = index;
		
		for (int i = 1; i < XML_DECLARATION_START_CHARS.length; i++) {
			if (!inRange()) {
				index = oldIndex;
				return false;
			}
			
			index++;
			if (buffer[index] != XML_DECLARATION_START_CHARS[i]) {
				index = oldIndex;
				return false;
			}
		}
		
		while (true) {
			index++;
			
			if (!inRange()) {
				index = oldIndex;
				return false;
			}
			
			if (buffer[index] == '?') {
				index++;
				
				if (buffer[index] != '>') {
					index = oldIndex;
					return false;
				}
				
				return true;
			}
		}
	}

	private boolean isCloseStream() {
		int oldIndex = index;
		
		for (int i = 1; i < CLOSE_STREAM_CHARS.length; i++) {
			if (!inRange()) {
				index = oldIndex;
				return false;
			}
			
			index++;
			if (buffer[index] != CLOSE_STREAM_CHARS[i]) {
				index = oldIndex;
				return false;
			}
		}
		
		return true;
	}

	private boolean isOpenStream() {
		int oldIndex = index;
		
		for (int i = 1; i < OPEN_STREAM_START_CHARS.length; i++) {
			if (!inRange()) {
				index = oldIndex;
				return false;
			}
			
			index++;
			if (buffer[index] != OPEN_STREAM_START_CHARS[i]) {
				index = oldIndex;
				return false;
			}
		}
		
		if (!inRange()) {
			index = oldIndex;
			return false;
		}
		
		index++;
		
		if (buffer[index] != ' ') {
			index = oldIndex;
			return false;
		}
		
		while (true) {
			if (!inRange()) {
				index = oldIndex;
				return false;
			}
			
			index++;
			if (buffer[index] == '>')
				return true;
		}
	}

	private boolean parseCData() {
		if (!checkChars(CDATA_START_CHARS, 2))
			return false;
		
		while (true) {
			if (!inRange()) {
				return false;
			}
			
			index++;
			if (buffer[index] == ']') {
				if (!inRange())
					return false;
				
				index++;
				if (buffer[index] != ']')
					continue;
				
				if (!inRange())
					return false;
				
				index++;
				if (buffer[index] != '>')
					continue;
				
				return true;
			}
		}
	}

	private boolean parseEmptyElementTail() {
		if (!inRange())
			return false;
		
		index++;
		
		if (buffer[index] != '>') {
			throw newXmlNotWellFormedException();
		}
		
		return true;
	}

	private boolean parseText() {
		return parseString(false, TEXT_END_FLAGS);
	}

	private boolean parseElementEndTag(String elementName) {
		if (!inRange())
			return false;
		
		index++;
		
		if (!checkChars(elementName.toCharArray(), 0))
			return false;
		
		if (!findNextNonWhitespaceChar().found)
			return false;
		
		if (buffer[index] != '>')
			throw newXmlNotWellFormedException();
		
		return true;
	}

	private boolean parseAttributes() {
		if (!findNextNonWhitespaceChar(false).found)
			return false;
		
		for (; ;) {
			if (buffer[index] == '/' || buffer[index] == '>') {
				return true;
			}
			
			if (!parseAttributeName()) {
				return false;
			}
			
			if (buffer[index] == ' ') {
				if (!findNextNonWhitespaceChar(true).found)
					return false;
				
				if (buffer[index] != '=') {
					throw newXmlNotWellFormedException();
				}
			}
			
			if (!findNextNonWhitespaceChar().found)
				return false;
			
			if (buffer[index] != '"' && buffer[index] != '\'')
				throw newXmlNotWellFormedException();
			
			if (!parseAttributeValue())
				return false;
			
			if (!findNextNonWhitespaceChar().found) {
				return false;
			}
		}
	}

	private boolean parseAttributeValue() {
		if (!inRange())
			return false;
		
		index++;
		int oldIndex = index;
		for (; ;) {
			if (buffer[index] == '"' || buffer[index] == '\'') {
				if (nullStringFound(false, oldIndex)) {
					throw newXmlNotWellFormedException();
				}
				
				return true;
			}
			
			if (!inRange()) {
				return false;
			}
			
			index++;
		}
	}

	private boolean parseAttributeName() {
		return parseString(false, ATTRIBUTE_NAME_END_FLAGS);
	}

	private boolean parseElementName() {
		return parseString(false, ELEMENT_NAME_END_FLAGS);
	}

	private boolean parseString(boolean ignoreCurrent, char[] endFlags) {
		int oldIndex = index;
		
		if (!inRange())
			return false;
		
		if (ignoreCurrent)
			index++;
		
		for (; ;) {
			for (char endFlag : endFlags) {
				if (buffer[index] == endFlag) {
					if (nullStringFound(ignoreCurrent, oldIndex)) {
						throw newXmlNotWellFormedException();
					}
					
					return true;
				}
			}
			
			if (!inRange())
				return false;
			
			index++;
		}
	}

	private boolean nullStringFound(boolean ignoreCurrent, int oldIndex) {
		return (ignoreCurrent && ((oldIndex + 1) == index)) || (!ignoreCurrent && (oldIndex == index));
	}

	@Override
	protected boolean parseMessage() {
		if (isXmlDeclaration()) {
			messageFound();
			messages.remove(messages.size() - 1);
			
			index++;
			if (!inRange()) {
				return true;
			}
			
			if (!findNextNonWhitespaceChar(false).found)
				return false;
			
			if (isOpenStream()) {
				messageFound();
				
				return true;
			}
			
			return false;
		}
		
		if (isOpenStream() || isCloseStream()) {
			messageFound();
			
			return true;
		}
		
		return parseElement();
	}
	
	@Override
	public String[] getMessages() {
		level = 0;
		
		return super.getMessages();
	}

	@Override
	protected void doResetBuffer() {
		level = 0;
	}
}
