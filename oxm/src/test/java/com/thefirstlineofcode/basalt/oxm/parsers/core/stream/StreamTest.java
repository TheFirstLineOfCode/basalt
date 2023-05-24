package com.thefirstlineofcode.basalt.oxm.parsers.core.stream;

import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.TestData;
import com.thefirstlineofcode.basalt.oxm.annotation.AnnotatedParserFactory;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.stream.Stream;

import junit.framework.Assert;

public class StreamTest {
	
	@Test
	public void testStream() {
		IOxmFactory oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
				ProtocolChain.
					first(Stream.PROTOCOL),
				new AnnotatedParserFactory<>(StreamParser.class)
			);
		
		String openStreamMessage = TestData.getData(this.getClass(), "openStreamMessage");
		
		Stream openStream = (Stream)oxmFactory.parse(openStreamMessage);
		
		Assert.assertEquals("chat.thefirstlineofcode.com", openStream.getTo().toString());
		Assert.assertEquals("en", openStream.getLang());
		Assert.assertEquals("1.0", openStream.getVersion());
		Assert.assertEquals(false, openStream.isClose());
		
		Assert.assertEquals("jabber:client", openStream.getDefaultNamespace());
		
		String closeStreamMessage = TestData.getData(this.getClass(), "closeStreamMessage");
		
		Stream closeStream = (Stream)oxmFactory.parse(closeStreamMessage);
		
		Assert.assertEquals(true, closeStream.isClose());
	}
}
