package com.thefirstlineofcode.basalt.oxm.binary;

import com.thefirstlineofcode.basalt.oxm.preprocessing.IMessagePreprocessor;

public interface IBinaryXmppProtocolFactory {
	public IBinaryXmppProtocolConverter createConverter();
	public IMessagePreprocessor createPreprocessor();
}
