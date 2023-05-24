package com.thefirstlineofcode.basalt.oxm.parsers.core.stanza;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.TestData;
import com.thefirstlineofcode.basalt.oxm.coc.CocParserFactory;
import com.thefirstlineofcode.basalt.oxm.parsing.FlawedProtocolObject;
import com.thefirstlineofcode.basalt.oxm.xep.ibb.TMessageData;
import com.thefirstlineofcode.basalt.xmpp.core.IqProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.StanzaError;
import com.thefirstlineofcode.basalt.xmpp.core.stream.error.UnsupportedStanzaType;

import junit.framework.Assert;

public class IqTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
				new IqProtocolChain(TMessageData.PROTOCOL),
				new CocParserFactory<>(
						TMessageData.class)
			);
	}
	
	@Test
	public void parseNoEmbeddedObjectFound() {
		String noEmbeddedObjectFoundIqMessage = TestData.getData(this.getClass(), "noEmbeddedObjectFoundIqMessage");
		try {
			oxmFactory.parse(noEmbeddedObjectFoundIqMessage);
			Assert.fail();
		} catch (ProtocolException e) {
			// should run to here
			Assert.assertTrue(e.getError() instanceof BadRequest);
			Assert.assertTrue(e.getError() instanceof StanzaError);
			Assert.assertEquals("some-id", ((StanzaError)e.getError()).getId());
			Assert.assertTrue("No embedded object found.".equals(e.getError().getText().getText()));
		}
		
		oxmFactory.unregister(new IqProtocolChain(TMessageData.PROTOCOL));
		String ibbIqMessage = TestData.getData(this.getClass(), "ibbIqMessage");
		
		Object obj = oxmFactory.parse(ibbIqMessage);
		Assert.assertTrue(FlawedProtocolObject.isFlawed(obj));
	}
	
	@Test
	public void parseEmbedded() {
		String dataText = TestData.getData(this.getClass(), "dataText");
		String ibbIqMessage = TestData.getData(this.getClass(), "ibbIqMessage");
		
		Object obj = oxmFactory.parse(ibbIqMessage);
		Assert.assertTrue(obj instanceof Iq);
		
		Iq iq = (Iq)obj;
		Assert.assertEquals("romeo@montague.net/orchard", iq.getFrom().toString());
		Assert.assertEquals("juliet@capulet.com/balcony", iq.getTo().toString());
		Assert.assertEquals("dsw71gj3", iq.getId());
		
		Assert.assertNotNull(iq.getObject());
		Assert.assertTrue(iq.getObject() instanceof TMessageData);
		TMessageData data = (TMessageData)iq.getObject();
		Assert.assertEquals("i781hf64", data.getSid());
		Assert.assertEquals(0, data.getSeq());
		Assert.assertEquals(dataText, data.getText());
	}
	
	@Test
	public void parseInvalid() {
		String invalidIqMessage = TestData.getData(this.getClass(), "invalidIqMessage");
		try {
			oxmFactory.parse(invalidIqMessage);
			Assert.fail();
		} catch (ProtocolException e) {
			// should run to here
			Assert.assertTrue(e.getError() instanceof BadRequest);
			Assert.assertTrue("Invalid stanza attribute: from-to.".equals(e.getError().getText().getText()));
		}
	}
	
	@Test
	public void parseUnsupportedStanzaTypeMessage() {
		String unsupportedStanzaTypeMessage = TestData.getData(this.getClass(), "unsupportedStanzaTypeMessage");
		try {
			oxmFactory.parse(unsupportedStanzaTypeMessage);
			Assert.fail();
		} catch (ProtocolException e) {
			// should run to here
			Assert.assertTrue(e.getError() instanceof UnsupportedStanzaType);
			Assert.assertTrue("Unsupported stanza type: IQ.".equals(e.getError().getText().getText()));
		}
	}
	
	@Test
	public void parseInvalidXmlMessage() {
		String invalidXmlMessage = TestData.getData(this.getClass(), "invalidXmlMessage");
		try {
			oxmFactory.parse(invalidXmlMessage);
			Assert.fail();
		} catch (ProtocolException e) {
			// should run to here
			Assert.assertTrue(e.getError() instanceof BadRequest);
		}
	}
}
