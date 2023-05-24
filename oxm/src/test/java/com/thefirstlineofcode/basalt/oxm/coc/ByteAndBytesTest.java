package com.thefirstlineofcode.basalt.oxm.coc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.xmpp.core.IqProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;

public class ByteAndBytesTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
			new IqProtocolChain(LoraAddress.PROTOCOL),
			new CocParserFactory<>(LoraAddress.class)
		);
		oxmFactory.register(
			LoraAddress.class,
			new CocTranslatorFactory<>(LoraAddress.class)
		);
	}
	
	@Test
	public void testByteAndBase642Bytes() {
		byte[] address = new byte[] {(byte)0x00, (byte)0xef};
		byte channel = 0x1f;
		LoraAddress loraAddress = new LoraAddress(address, channel);
		
		String xml = oxmFactory.translate(new Iq(Iq.Type.SET, loraAddress));
		
		Iq parsed = (Iq)oxmFactory.parse(xml);
		loraAddress = parsed.getObject();
		
		assertArrayEquals(address, loraAddress.getAddress());
		assertEquals(channel, loraAddress.getChannel());
	}
}
