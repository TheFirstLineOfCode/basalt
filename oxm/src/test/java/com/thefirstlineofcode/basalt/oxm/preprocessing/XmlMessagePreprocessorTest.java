package com.thefirstlineofcode.basalt.oxm.preprocessing;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.TestData;
import com.thefirstlineofcode.basalt.oxm.xml.preprocessing.XmlMessagePreprocessor;

import junit.framework.Assert;

public class XmlMessagePreprocessorTest {
	private ITextMessagePreprocessor preprocessor;
	
	private String shortHeartbeatChars = TestData.getData(this.getClass(), "shortHeartbeatChars");
	private String longHeartbeatChars = TestData.getData(this.getClass(), "longHeartbeatChars");
	private String complexMessage = TestData.getData(this.getClass(), "complexMessage");
	private String simpleMessage = TestData.getData(this.getClass(), "simpleMessage");
	private String openStreamMessage = TestData.getData(this.getClass(), "openStreamMessage");
	private String closeStreamMessage = TestData.getData(this.getClass(), "closeStreamMessage");
	private String uncompletedMessagePart1 = TestData.getData(this.getClass(), "uncompletedMessagePart1");
	private String uncompletedMessagePart2 = TestData.getData(this.getClass(), "uncompletedMessagePart2");
	private String anotherSimpleMessage = TestData.getData(this.getClass(), "anotherSimpleMessage");
	private String firstCharBrokenMessagePart1 = TestData.getData(this.getClass(), "firstCharBrokenMessagePart1");
	private String firstCharBrokenMessagePart2 = TestData.getData(this.getClass(), "firstCharBrokenMessagePart2");
	private String invalidMessage = TestData.getData(this.getClass(), "invalidMessage");
	private String oneCharMoreMessage = TestData.getData(this.getClass(), "oneCharMoreMessage");
	private String oneCharLessMessage = TestData.getData(this.getClass(), "oneCharLessMessage");
	private String oneCharTextElementMessage = TestData.getData(this.getClass(), "oneCharTextElementMessage");
	private String uncompletedMessagePart3 = TestData.getData(this.getClass(), "uncompletedMessagePart3");
	private String uncompletedMessagePart4 = TestData.getData(this.getClass(), "uncompletedMessagePart4");
	
	@Before
	public void before() {
		preprocessor = new XmlMessagePreprocessor(1024 * 1024);
	}
	
	@Test
	public void parse() throws Exception {
		String message = longHeartbeatChars + complexMessage + shortHeartbeatChars + simpleMessage + longHeartbeatChars;
		
		char[] chars = message.toCharArray();
		String[] result = preprocessor.process(chars, chars.length);
		
		Assert.assertEquals(5, result.length);
		Assert.assertEquals(longHeartbeatChars, result[0]);
		Assert.assertEquals(complexMessage.trim(), result[1]);
		Assert.assertEquals(shortHeartbeatChars, result[2]);
		Assert.assertEquals(simpleMessage.trim(), result[3]);
		Assert.assertEquals(longHeartbeatChars, result[4]);
		
		message = longHeartbeatChars + complexMessage + shortHeartbeatChars + openStreamMessage +
				longHeartbeatChars + simpleMessage + shortHeartbeatChars + closeStreamMessage +
				longHeartbeatChars + uncompletedMessagePart1;
		
		chars = message.toCharArray();
		result = preprocessor.process(chars, chars.length);

		Assert.assertEquals(9, result.length);
		Assert.assertEquals(longHeartbeatChars, result[0]);
		Assert.assertEquals(complexMessage.trim(), result[1]);
		Assert.assertEquals(openStreamMessage.trim().substring(22, openStreamMessage.length()), result[3]);
		Assert.assertEquals(simpleMessage.trim(), result[5]);
		Assert.assertEquals(closeStreamMessage.trim(), result[7]);
		Assert.assertEquals(longHeartbeatChars, result[8]);
		Assert.assertEquals(uncompletedMessagePart1, new String(preprocessor.getBuffer()));
		
		message = uncompletedMessagePart2 + anotherSimpleMessage;
		
		chars = message.toCharArray();
		result = preprocessor.process(chars, chars.length);
		
		Assert.assertEquals(2, result.length);
		Assert.assertEquals((uncompletedMessagePart1 + uncompletedMessagePart2).trim(), result[0]);
		Assert.assertEquals(anotherSimpleMessage.trim(), result[1]);
		Assert.assertEquals(0, preprocessor.getBuffer().length);
		
		chars = firstCharBrokenMessagePart1.toCharArray();
		result = preprocessor.process(chars, chars.length);
		
		Assert.assertEquals(0, result.length);
		
		chars = firstCharBrokenMessagePart2.toCharArray();
		result = preprocessor.process(chars, chars.length);
		
		Assert.assertEquals(1, result.length);
		Assert.assertEquals(firstCharBrokenMessagePart1+ firstCharBrokenMessagePart2, result[0]);
		
		message = invalidMessage + anotherSimpleMessage;
		
		chars = message.toCharArray();
		try {
			result = preprocessor.process(chars, chars.length);
			Assert.fail();
		} catch (Exception e) {
			// should run to here
			preprocessor.clear();
		}
		
		message = oneCharMoreMessage;
		chars = message.toCharArray();
		
		result = preprocessor.process(chars, chars.length);
		Assert.assertEquals(1, result.length);
		Assert.assertEquals(1, preprocessor.getBuffer().length);
		
		message = oneCharLessMessage;
		chars = message.toCharArray();
		
		result = preprocessor.process(chars, chars.length);
		Assert.assertEquals(1, result.length);
		Assert.assertEquals(0, preprocessor.getBuffer().length);
		
		message = oneCharTextElementMessage;
		chars = message.toCharArray();
		
		result = preprocessor.process(chars, chars.length);
		Assert.assertEquals(1, result.length);
		Assert.assertEquals(0, preprocessor.getBuffer().length);
		
		message = uncompletedMessagePart3;
		chars = message.toCharArray();
		
		result = preprocessor.process(chars, chars.length);
		Assert.assertEquals(0, result.length);
		Assert.assertEquals(message.length(), preprocessor.getBuffer().length);
		
		message = uncompletedMessagePart4;
		chars = message.toCharArray();
		
		result = preprocessor.process(chars, chars.length);
		Assert.assertEquals(1, result.length);
		Assert.assertEquals(uncompletedMessagePart3 + uncompletedMessagePart4, result[0]);
		Assert.assertEquals(0, preprocessor.getBuffer().length);
	}
}
