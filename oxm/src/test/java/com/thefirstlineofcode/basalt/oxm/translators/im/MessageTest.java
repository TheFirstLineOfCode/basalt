package com.thefirstlineofcode.basalt.oxm.translators.im;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.LangText;
import com.thefirstlineofcode.basalt.xmpp.im.stanza.Message;

import junit.framework.Assert;

public class MessageTest {
	private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
	}
	
	@Test
	public void parseMessage() {
		Message message = new Message();
		message.setFrom(JabberId.parse("juliet@example.com/balcony"));
		message.setTo(JabberId.parse("romeo@example.net"));
		message.setType(Message.Type.CHAT);
		message.setLang("en");
		
		message.getSubjects().add(new LangText("Hi"));
		message.getSubjects().add(new LangText("嗨！", "zh"));
		message.getBodies().add(new LangText("Hello, world!"));
		message.getBodies().add(new LangText("哈罗，世界！", "zh"));
		
		String messageMessage = oxmFactory.translate(message);
		
		Object obj = oxmFactory.parse(messageMessage);
        Assert.assertTrue(obj instanceof Message);

        message = (Message)obj;

        Assert.assertEquals("romeo@example.net", message.getTo().toString());
        Assert.assertEquals("juliet@example.com/balcony", message.getFrom().toString());
        Assert.assertEquals(Message.Type.CHAT, message.getType());
        Assert.assertEquals("en", message.getLang());
        
        Assert.assertEquals(2, message.getSubjects().size());
        Assert.assertEquals(null, message.getSubjects().get(0).getLang());
        
        Assert.assertEquals("Hi", message.getSubjects().get(0).getText());
        Assert.assertEquals("zh", message.getSubjects().get(1).getLang());
        Assert.assertEquals("嗨！", message.getSubjects().get(1).getText());
        
        Assert.assertEquals(2, message.getBodies().size());
        Assert.assertEquals(null, message.getBodies().get(0).getLang());
        Assert.assertEquals("Hello, world!", message.getBodies().get(0).getText());
        Assert.assertEquals("zh", message.getBodies().get(1).getLang());
        Assert.assertEquals("哈罗，世界！", message.getBodies().get(1).getText());
        
        message = new Message();
		message.setFrom(JabberId.parse("juliet@example.com/balcony"));
		message.setTo(JabberId.parse("romeo@example.net"));
		message.setType(Message.Type.CHAT);
		message.setLang("en");
		
		message.getBodies().add(new LangText("'\"\\/&<>"));

		messageMessage = oxmFactory.translate(message);
		
		obj = oxmFactory.parse(messageMessage);
        Assert.assertTrue(obj instanceof Message);

        message = (Message)obj;

        Assert.assertEquals("romeo@example.net", message.getTo().toString());
        Assert.assertEquals("juliet@example.com/balcony", message.getFrom().toString());
        Assert.assertEquals(Message.Type.CHAT, message.getType());
        Assert.assertEquals("en", message.getLang());
		
        Assert.assertEquals(1, message.getBodies().size());
        Assert.assertEquals("'\"\\/&<>", message.getBodies().get(0).getText());
	}
}
