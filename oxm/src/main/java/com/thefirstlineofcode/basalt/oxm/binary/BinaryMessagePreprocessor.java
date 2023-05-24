package com.thefirstlineofcode.basalt.oxm.binary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.preprocessing.IMessagePreprocessor;
import com.thefirstlineofcode.basalt.oxm.preprocessing.NonWhitespaceFindingResult;
import com.thefirstlineofcode.basalt.oxm.preprocessing.OutOfMaxBufferSizeException;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.XmlNotWellFormed;

public class BinaryMessagePreprocessor implements IMessagePreprocessor {
	private static final char CHAR_HEART_BEAT = ' ';
	private static final byte BYTE_HEART_BEAT = (byte)CHAR_HEART_BEAT;
	
	private static int DEFAULT_MAX_BUFFER_SIZE = 1024 * 1024;
	
	private int maxBufferSize;
	private byte[] buffer = new byte[0];
	private List<String> messages = new ArrayList<>();
	
	private int lastFoundMessageEnd = 0;
	
	private int index = 0;
	private boolean found = false;
	
	private IBinaryXmppProtocolConverter bxmppProtocolConverter;
	
	public BinaryMessagePreprocessor(IBinaryXmppProtocolConverter bxmppProtocolConverter) {
		this(bxmppProtocolConverter, DEFAULT_MAX_BUFFER_SIZE);
	}
	
	public BinaryMessagePreprocessor(IBinaryXmppProtocolConverter bxmppProtocolConverter, int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
		this.bxmppProtocolConverter = bxmppProtocolConverter;
	}
	
	@Override
	public String[] process(byte[] bytes) throws OutOfMaxBufferSizeException, ProtocolException {
		resetBuffer();
		
		if (buffer.length > maxBufferSize) {
			throw new OutOfMaxBufferSizeException("Message is so big. Stop to process it.");
		}
		
		buffer = appendBytesToBuffer(bytes, bytes.length);
		
		if (buffer.length == 1 && buffer[0] == BYTE_HEART_BEAT) {
			messages.add(String.valueOf(CHAR_HEART_BEAT));
			return getMessages();
		}
		
		while (true) {
			if (index == buffer.length - 1)
				return getMessages();
			
			if (!findNextMessageStart()) {
				found = false;
				return getMessages();
			}
			
			int docEnd = findDocument();
			if (docEnd == -1) {
				found = false;
				return getMessages();
			}
			
			String message = null;
			try {				
				if (lastFoundMessageEnd == 0 && (buffer[0] != BYTE_HEART_BEAT)) {
					message = bxmppProtocolConverter.toXml(Arrays.copyOfRange(buffer, 0, docEnd + 1));
				} else {
					message = bxmppProtocolConverter.toXml(Arrays.copyOfRange(buffer, lastFoundMessageEnd + 1, docEnd + 1));
				}
			} catch (BxmppConversionException e) {
				throw new ProtocolException(new BadRequest("Failed to convert BXMPP data to XML."), e);
			}
			messages.add(message);
			
			found = true;
			lastFoundMessageEnd = docEnd;
		}
	}

	private boolean findNextMessageStart() {
		NonWhitespaceFindingResult result = null;
		if (lastFoundMessageEnd == 0) {				
			result = findNextNonWhitespaceByte(false);
		} else {
			result = findNextNonWhitespaceByte();
		}
		
		if (result.prefixWhitespaceLength > 0) {
			messages.add(createKeepAliveMessage(result.prefixWhitespaceLength));
			if (lastFoundMessageEnd == 0) {
				lastFoundMessageEnd += (result.prefixWhitespaceLength - 1);
			} else {
				lastFoundMessageEnd += result.prefixWhitespaceLength;
			}
		}
		
		return result.found;
	}
	
	private String createKeepAliveMessage(int length) {
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = BYTE_HEART_BEAT;
		}
		
		return String.valueOf(chars);
	}
	
	@Override
	public String[] getMessages() {
		String[] messagesArray = messages.toArray(new String[messages.size()]);
		
		messages.clear();
		resetBuffer();
		
		return messagesArray;
	}
	
	protected NonWhitespaceFindingResult findNextNonWhitespaceByte() {
		return findNextNonWhitespaceByte(true);
	}
	
	protected NonWhitespaceFindingResult findNextNonWhitespaceByte(boolean ignoreCurrent) {
		if (!inRange())
			return new NonWhitespaceFindingResult();
		
		if (ignoreCurrent)
			index++;
		
		int prefixWhitespaceLength = 0;
		while (inRange() && Character.isWhitespace(buffer[index])) {
			prefixWhitespaceLength++;
			index++;
		}
		
		boolean found = !Character.isWhitespace(buffer[index]);
		if (!found) {
			prefixWhitespaceLength++;
			index++;
		}
		
		return new NonWhitespaceFindingResult(found, found ? prefixWhitespaceLength : prefixWhitespaceLength++);
	}
	
	protected boolean inRange() {
		if (buffer.length == 0)
			return false;
		
		return index < buffer.length - 1;
	}

	private int findDocument() {
		if (index > buffer.length - 1) {
			throw new IllegalStateException("Unexpected end of document.");
		}
		
		if (buffer[index] != BinaryConstants.FLAG_DOC_BEGINNING_END) {
			throw newXmlNotWellFormedException();
		}
		index++;
		
		while (index <= buffer.length - 1) {
			if (buffer[index] == BinaryConstants.FLAG_DOC_BEGINNING_END) {
				if ((buffer[index - 1] !=  BinaryConstants.FLAG_ESCAPE) ||
						(index > 2 && buffer[index - 2] != BinaryConstants.FLAG_ESCAPE))
					return index;
			}
			
			index++;
		}
		
		return -1;
	}

	protected ProtocolException newXmlNotWellFormedException() {
		return new ProtocolException(new XmlNotWellFormed(String.format("Not a valid binary xmpp message. Error position: %d.", index)));
	}

	private byte[] appendBytesToBuffer(byte[] bytes, int length) {
		byte[] newBuffer = new byte[buffer.length + length];
		
		System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
		System.arraycopy(bytes, 0, newBuffer, buffer.length, length);
		
		return newBuffer;
	}

	private void resetBuffer() {
		if (buffer.length > 0 && lastFoundMessageEnd != 0) {
			if (found && lastFoundMessageEnd == index) {
				buffer = new byte[0];
			} else {
				buffer = Arrays.copyOfRange(buffer, lastFoundMessageEnd + 1, buffer.length);
			}
		}
		
		index = lastFoundMessageEnd = 0;
		found = false;
	}

	@Override
	public void clear() {
		lastFoundMessageEnd = 0;
		buffer = new byte[0];
		messages = new ArrayList<>();
		index = 0;
		found = false;
	}

	@Override
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	@Override
	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	public byte[] getBuffer() {
		return buffer;
	}

}
