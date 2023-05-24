package com.thefirstlineofcode.basalt.oxm.translators.convention;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.coc.CocParserFactory;
import com.thefirstlineofcode.basalt.oxm.coc.CocTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.xep.command.TCommand;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TField;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TItem;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TReported;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TXData;
import com.thefirstlineofcode.basalt.xmpp.core.IqProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;

import junit.framework.Assert;

public class NamingConventionTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
				new IqProtocolChain(TCommand.PROTOCOL),
				new CocParserFactory<>(
						TCommand.class)
			);
		
		oxmFactory.register(
				new IqProtocolChain().
					next(TCommand.PROTOCOL).
					next(TXData.PROTOCOL),
				new CocParserFactory<>(
						TXData.class)
			);
		
		oxmFactory.register(
				TCommand.class,
				new CocTranslatorFactory<>(
						TCommand.class)
			);
		
		oxmFactory.register(
				TXData.class,
				new CocTranslatorFactory<>(
						TXData.class)
			);
	}
	
	@Test
	public void translateByNamingConvention() {
		String message = oxmFactory.translate(createIq1());
		Object object = oxmFactory.parse(message);
		assertXDataResult1(object);
		
		message = oxmFactory.translate(createIq2());
		object = oxmFactory.parse(message);
		assertXDataResult2(object);
	}

	private Object createIq1() {
		Iq iq = new Iq(Iq.Type.SET);
		iq.setFrom(JabberId.parse("romeo@montague.net/home"));
		iq.setTo(JabberId.parse("joogle@botster.shakespeare.lit"));
		iq.setLang("en");
		iq.setId("create2");
		
		TCommand command = new TCommand();
		command.setNode("create");
		command.setSessionid("create:20040408T0128Z");
		
		TXData xData = new TXData();
		xData.setType(TXData.Type.SUBMIT);
		
		TField field1 = new TField();
		field1.setType(TField.Type.HIDDEN);
		field1.setVar("form_type");
		field1.getValues().add("chat:bot");
		xData.getFields().add(field1);
		
		TField field2 = new TField();
		field2.setType(TField.Type.TEXT_SINGLE);
		field2.setVar("botname");
		field2.getValues().add("The Chat Google Bot");
		xData.getFields().add(field2);
		
		TField field3 = new TField();
		field3.setType(TField.Type.TEXT_MULTI);
		field3.setVar("description");
		field3.getValues().add("This bot enables you to send requests to");
		field3.getValues().add("Google and receive the search results right");
		field3.getValues().add("in your Jabber client. It&apos; really cool!");
		field3.getValues().add("It even supports Google News!");
		xData.getFields().add(field3);
		
		TField field4 = new TField();
		field4.setType(TField.Type.BOOLEAN);
		field4.setVar("public");
		field4.getValues().add("0");
		xData.getFields().add(field4);
		
		TField field5 = new TField();
		field5.setType(TField.Type.TEXT_PRIVATE);
		field5.setVar("password");
		field5.getValues().add("v3r0na");
		xData.getFields().add(field5);
		
		TField field6 = new TField();
		field6.setType(TField.Type.LIST_MULTI);
		field6.setVar("features");
		field6.getValues().add("news");
		field6.getValues().add("search");
		xData.getFields().add(field6);
		
		TField field7 = new TField();
		field7.setType(TField.Type.LIST_SINGLE);
		field7.setVar("maxsubs");
		field7.getValues().add("50");
		xData.getFields().add(field7);
		
		TField field8 = new TField();
		field8.setType(TField.Type.JID_MULTI);
		field8.setVar("invitelist");
		field8.getValues().add("juliet@capulet.com");
		field8.getValues().add("benvolio@montague.net");
		xData.getFields().add(field8);
		
		command.setXData(xData);
		
		iq.setObject(command);
		
		return iq;
	}

	private Object createIq2() {
		Iq iq = new Iq(Iq.Type.RESULT);
		iq.setFrom(JabberId.parse("joogle@botster.shakespeare.lit"));
		iq.setTo(JabberId.parse("juliet@capulet.com/chamber"));
		iq.setLang("en");
		iq.setId("search2");
		
		TCommand command = new TCommand();
		command.setNode("search");
		command.setStatus("completed");
		
		TXData xData = new TXData();
		xData.setType(TXData.Type.RESULT);
		
		xData.setTitle("Joogle Search: verona");
		
		TReported reported = new TReported();
		
		TField field1 = new TField();
		field1.setVar("name");
		
		reported.getFields().add(field1);
		
		TField field2 = new TField();
		field2.setVar("url");
		
		reported.getFields().add(field2);
		
		xData.setReported(reported);
		
		TItem item1 = new TItem();
		
		TField field3 = new TField();
		field3.setVar("name");
		field3.getValues().add("Comune di Verona - Benvenuti nel sito ufficiale");
		item1.getFields().add(field3);
		
		TField field4 = new TField();
		field4.setVar("url");
		field4.getValues().add("http://www.comune.verona.it/");
		
		item1.getFields().add(field4);
		
		xData.getItems().add(item1);
		
		TItem item2 = new TItem();
		
		TField field5 = new TField();
		field5.setVar("name");
		field5.getValues().add("benvenuto!");
		item2.getFields().add(field5);
		
		TField field6 = new TField();
		field6.setVar("url");
		field6.getValues().add("http://www.hellasverona.it/");
		item2.getFields().add(field6);
		
		xData.getItems().add(item2);
		
		TItem item3 = new TItem();
		
		TField field7 = new TField();
		field7.setVar("name");
		field7.getValues().add("Universita degli Studi di Verona - Home Page!");
		item3.getFields().add(field7);
		
		TField field8 = new TField();
		field8.setVar("url");
		field8.getValues().add("http://www.univr.it/");
		item3.getFields().add(field8);
		
		xData.getItems().add(item3);
		
		TItem item4 = new TItem();
		
		TField field9 = new TField();
		field9.setVar("name");
		field9.getValues().add("Aeroporti del Garda");
		item4.getFields().add(field9);
		
		TField field10 = new TField();
		field10.setVar("url");
		field10.getValues().add("http://www.aeroportoverona.it/");
		item4.getFields().add(field10);
		
		xData.getItems().add(item4);
		
		TItem item5 = new TItem();
		
		TField field11 = new TField();
		field11.setVar("name");
		field11.getValues().add("Veronafiere - fiera di Verona");
		item5.getFields().add(field11);
		
		TField field12 = new TField();
		field12.setVar("url");
		field12.getValues().add("http://www.veronafiere.it/");
		item5.getFields().add(field12);
		
		xData.getItems().add(item5);
		
		command.setXData(xData);
		
		iq.setObject(command);
		
		return iq;
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
