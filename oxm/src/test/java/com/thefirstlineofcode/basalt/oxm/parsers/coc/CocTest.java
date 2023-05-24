package com.thefirstlineofcode.basalt.oxm.parsers.coc;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.TestData;
import com.thefirstlineofcode.basalt.oxm.coc.CocParserFactory;
import com.thefirstlineofcode.basalt.oxm.xep.command.TCommand;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TField;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TXData;
import com.thefirstlineofcode.basalt.xmpp.core.IqProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;

import junit.framework.Assert;

public class CocTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
				new IqProtocolChain(TCommand.PROTOCOL),
				new CocParserFactory<>(
						TCommand.class)
			);
		
		ProtocolChain protocolChain = new IqProtocolChain(TCommand.PROTOCOL);
		protocolChain.next(null);
		new IqProtocolChain(TCommand.PROTOCOL).next(null);
		
		oxmFactory.register(
				ProtocolChain.first(Iq.PROTOCOL).
					next(TCommand.PROTOCOL).
					next(TXData.PROTOCOL),
				new CocParserFactory<>(
						TXData.class)
			);
	}
	
	@Test
	public void parseByNamingConvention() {
		String xDataMessage = TestData.getData(this.getClass(), "xDataMessage1");
		assertXDataResult1(oxmFactory.parse(xDataMessage));
		
		xDataMessage =TestData.getData(this.getClass(), "xDataMessage2");
		assertXDataResult1(oxmFactory.parse(xDataMessage));
		
		xDataMessage = TestData.getData(this.getClass(), "xDataMessage3");
		assertXDataResult2(oxmFactory.parse(xDataMessage));
	}
	
	private void assertXDataResult1(Object object) {
		Assert.assertTrue(object instanceof Iq);
		Iq iq = (Iq)object;
		Assert.assertEquals("romeo@montague.net/home", iq.getFrom().toString());
		Assert.assertEquals(Iq.Type.SET, iq.getType());
		Assert.assertEquals("en", iq.getLang());
		Assert.assertTrue(iq.getObject() instanceof TCommand);
		TCommand command = (TCommand)iq.getObject();
		Assert.assertEquals("create", command.getNode());
		Assert.assertEquals("create:20040408T0128Z", command.getSessionid());
		Assert.assertNotNull(command.getXData());
		TXData xData = command.getXData();
		Assert.assertEquals(TXData.Type.SUBMIT, xData.getType());
		Assert.assertEquals(8, xData.getFields().size());
		
		TField field = xData.getFields().get(0);
		Assert.assertEquals(TField.Type.HIDDEN, field.getType());
		Assert.assertEquals("form_type", field.getVar());
		Assert.assertEquals(1, field.getValues().size());
		Assert.assertEquals("chat:bot", field.getValues().get(0));
		
		field = xData.getFields().get(3);
		Assert.assertEquals(TField.Type.BOOLEAN, field.getType());
		Assert.assertEquals("public", field.getVar());
		Assert.assertEquals(1, field.getValues().size());
		Assert.assertEquals("0", field.getValues().get(0));
		
		field = xData.getFields().get(7);
		Assert.assertEquals(TField.Type.JID_MULTI, field.getType());
		Assert.assertEquals("invitelist", field.getVar());
		Assert.assertEquals(2, field.getValues().size());
		Assert.assertEquals("juliet@capulet.com", field.getValues().get(0));
		Assert.assertEquals("benvolio@montague.net", field.getValues().get(1));
	}
	
	private void assertXDataResult2(Object object) {
		Assert.assertTrue(object instanceof Iq);
		Iq iq = (Iq)object;
		Assert.assertEquals("joogle@botster.shakespeare.lit", iq.getFrom().toString());
		Assert.assertEquals("juliet@capulet.com/chamber", iq.getTo().toString());
		Assert.assertEquals(Iq.Type.RESULT, iq.getType());
		Assert.assertEquals("en", iq.getLang());
		Assert.assertEquals("search2", iq.getId());
		Assert.assertTrue(iq.getObject() instanceof TCommand);
		TCommand command = (TCommand)iq.getObject();
		Assert.assertEquals("search", command.getNode());
		Assert.assertEquals("completed", command.getStatus());
		Assert.assertNotNull(command.getXData());
		TXData xData = command.getXData();
		Assert.assertEquals(TXData.Type.RESULT, xData.getType());
		Assert.assertEquals("Joogle Search: verona", xData.getTitle());
		
		Assert.assertEquals(2, xData.getReported().getFields().size());
		
		TField field = xData.getReported().getFields().get(0);
		Assert.assertEquals("name", field.getVar());
		field = xData.getReported().getFields().get(1);
		Assert.assertEquals("url", field.getVar());
		
		Assert.assertEquals(5, xData.getItems().size());
		
		for (int i = 0; i < 5; i++) {
			Assert.assertEquals(2, xData.getItems().get(i).getFields().size());
		}
		
		field = xData.getItems().get(0).getFields().get(0);
		Assert.assertEquals("name", field.getVar());
		Assert.assertEquals(1, field.getValues().size());
		Assert.assertEquals("Comune di Verona - Benvenuti nel sito ufficiale", field.getValues().get(0));
		
		field = xData.getItems().get(0).getFields().get(1);
		Assert.assertEquals("url", field.getVar());
		Assert.assertEquals(1, field.getValues().size());
		Assert.assertEquals("http://www.comune.verona.it/", field.getValues().get(0));
		
		field = xData.getItems().get(4).getFields().get(0);
		Assert.assertEquals("name", field.getVar());
		Assert.assertEquals(1, field.getValues().size());
		Assert.assertEquals("Veronafiere - fiera di Verona", field.getValues().get(0));
		
		field = xData.getItems().get(4).getFields().get(1);
		Assert.assertEquals("url", field.getVar());
		Assert.assertEquals(1, field.getValues().size());
		Assert.assertEquals("http://www.veronafiere.it/", field.getValues().get(0));
	}
}
