package com.thefirstlineofcode.basalt.oxm.preprocessing;

import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;

public interface IMessagePreprocessor {
	String[] process(byte[] bytes) throws OutOfMaxBufferSizeException, ProtocolException;
	void clear();
	void setMaxBufferSize(int maxBufferSize);
	int getMaxBufferSize();
	String[] getMessages();
}
