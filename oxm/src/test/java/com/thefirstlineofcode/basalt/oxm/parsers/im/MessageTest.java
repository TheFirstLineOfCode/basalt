package com.thefirstlineofcode.basalt.oxm.parsers.im;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.TestData;
import com.thefirstlineofcode.basalt.oxm.annotation.AnnotatedParserFactory;
import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessAttributes;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessEmbeddedObject;
import com.thefirstlineofcode.basalt.oxm.coc.CocParserFactory;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2JabberId;
import com.thefirstlineofcode.basalt.oxm.parsing.FlawedProtocolObject;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.MessageProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;
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
		String messageMessage = TestData.getData(this.getClass(), "messageMessage1");
		
		Object obj = oxmFactory.parse(messageMessage);
        Assert.assertTrue(obj instanceof Message);

        Message message = (Message)obj;

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
	}
	
	@Test
	public void checkLang() {
		String messageMessage = TestData.getData(this.getClass(), "messageMessage2");
		
		try {
			oxmFactory.parse(messageMessage);
			Assert.fail("attribute 'xml:lang' must be set");
		} catch (ProtocolException e) {
			// ignore
		}
	}
	
	@Test
	public void flawedStanza() {
		String messageMessage = TestData.getData(this.getClass(), "messageMessage3");
		
		Object object = oxmFactory.parse(messageMessage);
		Assert.assertTrue(object instanceof Message);
		Assert.assertTrue(FlawedProtocolObject.isFlawed(object));
		
		Message message = (Message)object;		
		Assert.assertEquals("juliet@capulet.lit", message.getFrom().toString());
		Assert.assertEquals("romeo@montague.net", message.getTo().toString());
		
		FlawedProtocolObject flawed = message.getObject(FlawedProtocolObject.class);
		Assert.assertEquals(1, flawed.getFlaws().size());
		ProtocolChain flaw = new MessageProtocolChain(new Protocol(
				"http://jabber.org/protocol/pubsub#event", "event"));
		Assert.assertEquals(flaw, flawed.getFlaws().get(0));
		
		oxmFactory.register(new MessageProtocolChain(Event.PROTOCOL),
					new CocParserFactory<>(Event.class));
		messageMessage = TestData.getData(this.getClass(), "messageMessage4");
		
		object = oxmFactory.parse(messageMessage);
		Assert.assertTrue(object instanceof Message);
		Assert.assertTrue(FlawedProtocolObject.isFlawed(object));
		
		message = (Message)object;
		Event event = getEvent(message);		
		Assert.assertEquals("urn:xmpp:avatar:metadata", event.getItems().getNode());
		
		Assert.assertEquals("juliet@capulet.lit", message.getFrom().toString());
		Assert.assertEquals("romeo@montague.lit", message.getTo().toString());
		
		flawed = message.getObject(FlawedProtocolObject.class);
		Assert.assertEquals(2, flawed.getFlaws().size());
		ProtocolChain firstFlaw = new MessageProtocolChain().
				next(new Protocol("http://jabber.org/protocol/pubsub#event", "event")).
				next(new Protocol("urn:xmpp:avatar:metadata", "metadata"));
		Assert.assertEquals(firstFlaw, flawed.getFlaws().get(0));
		
		ProtocolChain secondFlaw = new MessageProtocolChain().next(new Protocol(
				"http://jabber.org/protocol/address", "addresses"));
		Assert.assertEquals(secondFlaw, flawed.getFlaws().get(1));
	}
	
	private Event getEvent(Message message) {
		for (Object protocolObject : message.getObjects()) {
			if (protocolObject instanceof Event) {
				return (Event)protocolObject;
			}
		}
		
		return null;
	}

	@Test
	public void multiEmbeddedObjects() {
		oxmFactory.register(
				new MessageProtocolChain(Event.PROTOCOL),
				new AnnotatedParserFactory<>(EventParser.class));
		oxmFactory.register(
				new MessageProtocolChain().
				next(Event.PROTOCOL).
				next(Metadata.PROTOCOL),
				new CocParserFactory<>(Metadata.class));
		oxmFactory.register(
				new MessageProtocolChain().
				next(Addresses.PROTOCOL),
				new CocParserFactory<>(Addresses.class));
		
		String messageMessage = TestData.getData(this.getClass(), "messageMessage4");
		
		Object object = oxmFactory.parse(messageMessage);
		Assert.assertTrue(object instanceof Message);
		
		Message message = (Message)object;
		
		Assert.assertEquals("juliet@capulet.lit", message.getFrom().toString());
		Assert.assertEquals("romeo@montague.lit", message.getTo().toString());
		
		Event event = (Event)message.getObjects().get(0);
		Assert.assertEquals("urn:xmpp:avatar:metadata", event.getItems().getNode());
		Assert.assertEquals(12345, event.getItems().getItems().get(0).getMetadata().getInfo().getBytes());
		Assert.assertEquals(Event.PROTOCOL, message.getObjectProtocols().get(Event.class));
		
		Addresses addresses = (Addresses)message.getObjects().get(1);
		Assert.assertEquals(JabberId.parse("juliet@capulet.lit/chamber"), addresses.getAddresses().get(0).getJid());
		Assert.assertEquals(Addresses.PROTOCOL, message.getObjectProtocols().get(Addresses.class));
	}
	
	@Parser(namespace="http://jabber.org/protocol/pubsub#event", localName="event", objectType=Event.class)
	public static class EventParser {
		
		@ProcessAttributes("/items")
		public void processItems(IParsingContext<Event> context, List<Attribute> attributes) {
			Items items = new Items();
			for (Attribute attribute : attributes) {
				if ("node".equals(attribute.getName())) {
					items.setNode(attribute.getValue().toString());
				} else {
					throw new ProtocolException(new BadRequest());
				}
			}
			
			context.getObject().setItems(items);
		}
		
		@ProcessAttributes("/items/item")
		public void processItemsArray(IParsingContext<Event> context, List<Attribute> attributes) {
			Item item = new Item();
			
			for (Attribute attribute : attributes) {
				if ("id".equals(attribute.getName())) {
					item.setId(attribute.getValue().toString());
				} else {
					throw new ProtocolException(new BadRequest());
				}
			}
			
			context.getObject().getItems().getItems().add(item);
		}
		
		@ProcessEmbeddedObject
		public void processMetadata(IParsingContext<Event> context, Protocol protocol, Object object) {
			List<Item> items = context.getObject().getItems().getItems();
			Item lastItem = items.get(items.size() - 1);
			lastItem.setMetadata((Metadata)object);
		}
	}
	
	@ProtocolObject(namespace="http://jabber.org/protocol/pubsub#event", localName="event")
	public static class Event {
		public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/pubsub#event", "event");
		
		private Items items;

		public Items getItems() {
			return items;
		}

		public void setItems(Items items) {
			this.items = items;
		}
		
	}
	
	public static class Items {
		private String node;
		@Array(Item.class)
		private List<Item> items;
		
		public String getNode() {
			return node;
		}
		
		public void setNode(String node) {
			this.node = node;
		}
		
		public List<Item> getItems() {
			if (items == null)
				items = new ArrayList<>();
			
			return items;
		}
		
		public void setItems(List<Item> items) {
			this.items = items;
		}
		
	}
	
	public static class Item {
		private String id;
		private Metadata metadata;
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public Metadata getMetadata() {
			return metadata;
		}
		
		public void setMetadata(Metadata metadata) {
			this.metadata = metadata;
		}
		
	}
	
	@ProtocolObject(namespace="urn:xmpp:avatar:metadata", localName="metadata")
	public static class Metadata {
		public static final Protocol PROTOCOL = new Protocol("urn:xmpp:avatar:metadata", "metadata");
		
		private Info info;

		public Info getInfo() {
			return info;
		}

		public void setInfo(Info info) {
			this.info = info;
		}
		
	}
	
	public static class Info {
		private String id;
		private String type;
		private int bytes;
		private int width;
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getType() {
			return type;
		}
		
		public void setType(String type) {
			this.type = type;
		}
		
		public int getBytes() {
			return bytes;
		}
		
		public void setBytes(int bytes) {
			this.bytes = bytes;
		}
		
		public int getWidth() {
			return width;
		}
		
		public void setWidth(int width) {
			this.width = width;
		}
		
	}
	
	@ProtocolObject(namespace="http://jabber.org/protocol/address", localName="addresses")
	public static class Addresses {
		public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/address", "addresses");
		
		@Array(Address.class)
		private List<Address> addresses;

		public List<Address> getAddresses() {
			return addresses;
		}

		public void setAddresses(List<Address> addresses) {
			this.addresses = addresses;
		}
	}
	
	public static class Address {
		private String type;
		@String2JabberId
		private JabberId jid;
		
		public String getType() {
			return type;
		}
		
		public void setType(String type) {
			this.type = type;
		}
		
		public JabberId getJid() {
			return jid;
		}
		
		public void setJid(JabberId jid) {
			this.jid = jid;
		}
		
	}
}
