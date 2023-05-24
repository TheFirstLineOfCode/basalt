package com.thefirstlineofcode.basalt.oxm.preprocessing;

import java.io.UnsupportedEncodingException;

import com.thefirstlineofcode.basalt.oxm.parsing.BadMessageException;
import com.thefirstlineofcode.basalt.oxm.xml.preprocessing.XmlMessagePreprocessor;
import com.thefirstlineofcode.basalt.xmpp.Constants;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;

public class XmlMessagePreprocessorAdapter implements IMessagePreprocessor {
	private static int DEFAULT_MAX_BUFFER_SIZE = 1024 * 1024;
	
	private ITextMessagePreprocessor messagePreprocessor;
	private String charset;
	
	public XmlMessagePreprocessorAdapter() {
		this(Constants.DEFAULT_CHARSET);
	}
	
	public XmlMessagePreprocessorAdapter(int maxBufferSize) {
		this(Constants.DEFAULT_CHARSET, maxBufferSize);
	}
	
	public XmlMessagePreprocessorAdapter(String charset) {
		this(charset, DEFAULT_MAX_BUFFER_SIZE);
	}
	
	public XmlMessagePreprocessorAdapter(String charset, int maxBufferSize) {
		this.charset = charset;
		messagePreprocessor = new XmlMessagePreprocessor();
		messagePreprocessor.setMaxBufferSize(maxBufferSize);
	}
	
	@Override
	public synchronized String[] process(byte[] bytes) throws OutOfMaxBufferSizeException, ProtocolException {
		char[] chars;
		try {
			chars = new String(bytes, charset).toCharArray();
		} catch (UnsupportedEncodingException e) {
			throw new BadMessageException("Unsupported encoding.", e);
		}
		
		int length = chars.length;
		if (length == 0) {
			return messagePreprocessor.getMessages();
		}
		
		return messagePreprocessor.process(chars, length);
	}

	@Override
	public void setMaxBufferSize(int maxBufferSize) {
		messagePreprocessor.setMaxBufferSize(maxBufferSize);
	}

	@Override
	public int getMaxBufferSize() {
		return messagePreprocessor.getMaxBufferSize();
	}

	@Override
	public void clear() {
		messagePreprocessor.clear();
	}
	
	@Override
	public String[] getMessages() {
		return messagePreprocessor.getMessages();
	}

}
