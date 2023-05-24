package com.thefirstlineofcode.basalt.oxm.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.TestData;
import com.thefirstlineofcode.basalt.oxm.coc.CocParserFactory;
import com.thefirstlineofcode.basalt.oxm.coc.CocTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.coc.LoraAddress;
import com.thefirstlineofcode.basalt.xmpp.core.IqProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;

import junit.framework.Assert;

public class BinaryXmppProtocolConverterTest {
	private IBinaryXmppProtocolConverter bxmppProtocolConverter;
	private String openStreamXmlMessage;
	private byte[] openStreamBinaryMessage;
	private String closeStreamXmlMessage;
	private byte[] closeStreamBinaryMessage;
	private String complexXmlMessage;
	private byte[] complexBinaryMessage;
	private String simpleXmlMessage;
	private byte[] simpleBinaryMessage;
	private byte[] byteAndBytesIncludedBinaryMessage;
	
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() throws ReduplicateBxmppReplacementException {
		bxmppProtocolConverter = BinaryXmppProtocolConverterFactory.getInstance();
		
		openStreamXmlMessage = TestData.getData(BinaryXmppProtocolConverterTest.class, "openStreamXmlMessage");
		String openStreamBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "openStreamBinaryMessage");
		openStreamBinaryMessage = TestData.toBinaryBytes(openStreamBinaryMessageString);
		
		closeStreamXmlMessage = TestData.getData(BinaryXmppProtocolConverterTest.class, "closeStreamXmlMessage");
		String closeStreamBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "closeStreamBinaryMessage");
		closeStreamBinaryMessage = TestData.toBinaryBytes(closeStreamBinaryMessageString);
		
		complexXmlMessage = TestData.getData(BinaryXmppProtocolConverterTest.class, "complexXmlMessage");
		String complexBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "complexBinaryMessage");
		complexBinaryMessage = TestData.toBinaryBytes(complexBinaryMessageString);
		
		simpleXmlMessage = TestData.getData(BinaryXmppProtocolConverterTest.class, "simpleXmlMessage");
		String simpleBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "simpleBinaryMessage");
		simpleBinaryMessage = TestData.toBinaryBytes(simpleBinaryMessageString);
		
		String byteAndBytesIncludedBinaryMessageString = TestData.getData(BinaryXmppProtocolConverterTest.class, "byteAndBytesIncludedBinaryMessage");
		byteAndBytesIncludedBinaryMessage = TestData.toBinaryBytes(byteAndBytesIncludedBinaryMessageString);
		
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
	public void toXml() throws BxmppConversionException {
		Assert.assertEquals(complexXmlMessage, bxmppProtocolConverter.toXml(complexBinaryMessage));
		Assert.assertEquals(simpleXmlMessage, bxmppProtocolConverter.toXml(simpleBinaryMessage));
		Assert.assertEquals(openStreamXmlMessage, bxmppProtocolConverter.toXml(openStreamBinaryMessage));
		Assert.assertEquals(closeStreamXmlMessage, bxmppProtocolConverter.toXml(closeStreamBinaryMessage));
	}
	
	@Test
	public void toBinary() throws UnsupportedEncodingException, BxmppConversionException {
		Assert.assertTrue(Arrays.equals(complexBinaryMessage, bxmppProtocolConverter.toBinary(complexXmlMessage)));
		Assert.assertTrue(Arrays.equals(simpleBinaryMessage, bxmppProtocolConverter.toBinary(simpleXmlMessage)));
		Assert.assertTrue(Arrays.equals(openStreamBinaryMessage, bxmppProtocolConverter.toBinary(openStreamXmlMessage)));
		Assert.assertTrue(Arrays.equals(closeStreamBinaryMessage, bxmppProtocolConverter.toBinary(closeStreamXmlMessage)));
	}
	
	@Test
	public void byteAndBytesIncludedBinaryMessage() throws BxmppConversionException {
		String byteAndBytesIncludedXmlMessage = bxmppProtocolConverter.toXml(byteAndBytesIncludedBinaryMessage);
		
		Iq iq = (Iq)oxmFactory.parse(byteAndBytesIncludedXmlMessage);
		assertEquals(1, iq.getObjects().size());
		assertEquals(LoraAddress.class, iq.getObject().getClass());
		LoraAddress loraAddress = iq.getObject();
		assertArrayEquals(new byte[] {0x00, (byte)0xef}, loraAddress.getAddress());
		assertEquals((byte)0x1f, loraAddress.getChannel());
		
		assertEquals(byteAndBytesIncludedXmlMessage, oxmFactory.translate(iq));		
		assertArrayEquals(byteAndBytesIncludedBinaryMessage, bxmppProtocolConverter.toBinary(byteAndBytesIncludedXmlMessage));
	}
	
}
