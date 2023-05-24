package com.thefirstlineofcode.basalt.oxm.parsers.error;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.TestData;
import com.thefirstlineofcode.basalt.oxm.coc.CocParserFactory;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.xmpp.core.IError;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.StreamError;

import junit.framework.Assert;

public class ErrorTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
				ProtocolChain.
					first(StreamError.PROTOCOL).
					next(EscapeYourData.PROTOCOL),
				new CocParserFactory<>(
						EscapeYourData.class)
			);
		
		oxmFactory.register(
				ProtocolChain.
					first(StanzaError.PROTOCOL).
					next(TooManyParameters.PROTOCOL),
				new CocParserFactory<>(
						TooManyParameters.class)
			);
	}
	
	@Test
	public void parseStreamError() {
		String errorMessage = TestData.getData(this.getClass(), "errorMessage1");
		
		IError error = (IError)oxmFactory.parse(errorMessage);
		Assert.assertNotNull(error);
		Assert.assertTrue(error instanceof StreamError);
		
		Assert.assertNotNull(error.getDefinedCondition());
		Assert.assertEquals("not-well-formed", error.getDefinedCondition());
		Assert.assertNotNull(error.getText());
		Assert.assertEquals("en", error.getText().getLang());
		Assert.assertEquals("Some special application diagnostic information!", error.getText().getText());
		Assert.assertTrue(error.getApplicationSpecificCondition() instanceof EscapeYourData);
	}
	
	@ProtocolObject(namespace="application-ns", localName="escape-your-data")
	public static class EscapeYourData {
		public static final Protocol PROTOCOL = new Protocol("application-ns", "escape-your-data");
	}
	
	@Test
	public void parseStanzaError() {
		String senderMessage = TestData.getData(this.getClass(), "senderMessage");
		String errorMessage = TestData.getData(this.getClass(), "errorMessage2");
		
		Object obj = oxmFactory.parse(errorMessage);
		Assert.assertNotNull(obj);
		Assert.assertTrue(obj instanceof StanzaError);
		
		StanzaError error = (StanzaError)obj;
		Assert.assertEquals(senderMessage.replaceAll("\\\\", ""), error.getSenderMessage());
		Assert.assertEquals("some-id", error.getId());
		Assert.assertEquals(StanzaError.Kind.MESSAGE, error.getKind());
		Assert.assertNotNull(error.getDefinedCondition());
		Assert.assertEquals("bad-request", error.getDefinedCondition());
		Assert.assertNull(error.getText());
		Assert.assertTrue(error.getApplicationSpecificCondition() instanceof TooManyParameters);
		
		errorMessage = TestData.getData(this.getClass(), "errorMessage3");
		
		obj = oxmFactory.parse(errorMessage);
		Assert.assertNotNull(obj);
		Assert.assertTrue(obj instanceof StanzaError);
		
		error = (StanzaError)obj;
		Assert.assertEquals("some-id", error.getId());
		Assert.assertEquals(StanzaError.Kind.IQ, error.getKind());
		
		Assert.assertEquals(null, error.getSenderMessage());
		Assert.assertNotNull(error.getDefinedCondition());
		Assert.assertEquals("bad-request", error.getDefinedCondition());
		Assert.assertNull(error.getText());
		Assert.assertTrue(error.getApplicationSpecificCondition() instanceof TooManyParameters);
	}
	
	@ProtocolObject(namespace="application-ns", localName="too-many-parameters")
	public static class TooManyParameters {
		public static final Protocol PROTOCOL = new Protocol("application-ns", "too-many-parameters");
	}
}
