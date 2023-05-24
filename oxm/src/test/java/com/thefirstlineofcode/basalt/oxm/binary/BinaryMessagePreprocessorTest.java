package com.thefirstlineofcode.basalt.oxm.binary;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.TestData;

import junit.framework.Assert;

public class BinaryMessagePreprocessorTest {
	private BinaryMessagePreprocessor preprocessor;
	
	private byte[] shortHeartbeatBytes;
	private byte[] longHeartbeatBytes;
	private byte[] simpleBinaryMessage;
	private byte[] openStreamBinaryMessage;
	private byte[] closeStreamBinaryMessagePart1;
	private byte[] closeStreamBinaryMessagePart2;
	private byte[] uncompletedComplexBinaryMessagePart1;
	private byte[] uncompletedComplexBinaryMessagePart2;
	private byte[] uncompletedComplexBinaryMessagePart3;
	
	private String shortHeartbeatChars;
	private String longHeartbeatChars;
	private String simpleXmlMessage;
	private String openStreamXmlMessage;
	private String closeStreamXmlMessage;
	private String complexXmlMessage;
	
	
	@Before
	public void before() {
		preprocessor = new BinaryMessagePreprocessor(BinaryXmppProtocolConverterFactory.getInstance(), 1024 * 1024);
		
		shortHeartbeatBytes = TestData.toBinaryBytes(TestData.getData(this.getClass(), "shortHeartbeatBytes"));
		longHeartbeatBytes = TestData.toBinaryBytes(TestData.getData(this.getClass(), "longHeartbeatBytes"));
		
		simpleBinaryMessage = TestData.toBinaryBytes(TestData.getData(this.getClass(), "simpleBinaryMessage"));
		openStreamBinaryMessage = TestData.toBinaryBytes(TestData.getData(this.getClass(), "openStreamBinaryMessage"));
		closeStreamBinaryMessagePart1 = TestData.toBinaryBytes(TestData.getData(this.getClass(), "closeStreamBinaryMessagePart1"));
		closeStreamBinaryMessagePart2 = TestData.toBinaryBytes(TestData.getData(this.getClass(), "closeStreamBinaryMessagePart2"));
		uncompletedComplexBinaryMessagePart1 = TestData.toBinaryBytes(TestData.getData(this.getClass(), "uncompletedComplexBinaryMessagePart1"));
		uncompletedComplexBinaryMessagePart2 = TestData.toBinaryBytes(TestData.getData(this.getClass(), "uncompletedComplexBinaryMessagePart2"));
		uncompletedComplexBinaryMessagePart3 = TestData.toBinaryBytes(TestData.getData(this.getClass(), "uncompletedComplexBinaryMessagePart3"));
		
		shortHeartbeatChars = TestData.getData(this.getClass(), "shortHeartbeatChars");
		longHeartbeatChars = TestData.getData(this.getClass(), "longHeartbeatChars");
		simpleXmlMessage  = TestData.getData(this.getClass(), "simpleXmlMessage");
		openStreamXmlMessage  = TestData.getData(this.getClass(), "openStreamXmlMessage");
		closeStreamXmlMessage  = TestData.getData(this.getClass(), "closeStreamXmlMessage");
		complexXmlMessage  = TestData.getData(this.getClass(), "complexXmlMessage");
		
	}

	@Test
	public void parse() throws Exception {
		String[] result = preprocessor.process(merge(longHeartbeatBytes, simpleBinaryMessage, shortHeartbeatBytes, openStreamBinaryMessage, longHeartbeatBytes));
		Assert.assertEquals(5, result.length);
		Assert.assertEquals(longHeartbeatChars, result[0]);
		Assert.assertEquals(simpleXmlMessage, result[1]);
		Assert.assertEquals(shortHeartbeatChars, result[2]);
		Assert.assertEquals(openStreamXmlMessage, result[3]);
		Assert.assertEquals(longHeartbeatChars, result[4]);
		Assert.assertEquals(0, preprocessor.getBuffer().length);
		
		result = preprocessor.process(shortHeartbeatBytes);
		Assert.assertEquals(1, result.length);
		Assert.assertEquals(shortHeartbeatChars, result[0]);
		Assert.assertEquals(0, preprocessor.getBuffer().length);
		
		result = preprocessor.process(merge(longHeartbeatBytes, closeStreamBinaryMessagePart1));
		Assert.assertEquals(1, result.length);
		Assert.assertEquals(longHeartbeatChars, result[0]);
		Assert.assertEquals(closeStreamBinaryMessagePart1.length, preprocessor.getBuffer().length);
		result = preprocessor.process(merge(closeStreamBinaryMessagePart2, longHeartbeatBytes, uncompletedComplexBinaryMessagePart1));
		Assert.assertEquals(2, result.length);
		Assert.assertEquals(closeStreamXmlMessage, result[0]);
		Assert.assertEquals(longHeartbeatChars, result[1]);
		Assert.assertEquals(uncompletedComplexBinaryMessagePart1.length, preprocessor.getBuffer().length);
		result = preprocessor.process(uncompletedComplexBinaryMessagePart2);
		Assert.assertEquals(0, result.length);
		Assert.assertEquals(merge(uncompletedComplexBinaryMessagePart1, uncompletedComplexBinaryMessagePart2).length,
				preprocessor.getBuffer().length);
		result = preprocessor.process(merge(uncompletedComplexBinaryMessagePart3, shortHeartbeatBytes));
		Assert.assertEquals(2, result.length);
		Assert.assertEquals(complexXmlMessage, result[0]);
		Assert.assertEquals(shortHeartbeatChars, result[1]);
		Assert.assertEquals(0, preprocessor.getBuffer().length);
	}
	
	private byte[] merge(byte[]... args) {
		byte[] lastMerged = args[0];
		byte[] finalMerged = null;
		for (int i = 1; i < args.length; i++) {
			finalMerged = new byte[lastMerged.length + args[i].length];
			System.arraycopy(lastMerged, 0, finalMerged, 0, lastMerged.length);
			System.arraycopy(args[i], 0, finalMerged, lastMerged.length, args[i].length);
			
			lastMerged = finalMerged;
		}
		
		return finalMerged;
	}
	
	public void byteAndBytesIncluded() {
		
	}
}
