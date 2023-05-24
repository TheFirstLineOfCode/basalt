package com.thefirstlineofcode.basalt.xmpp.im.roster;

import java.util.List;

import com.thefirstlineofcode.basalt.oxm.Attribute;
import com.thefirstlineofcode.basalt.oxm.Value;
import com.thefirstlineofcode.basalt.oxm.annotations.Parser;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessAttributes;
import com.thefirstlineofcode.basalt.oxm.annotations.ProcessText;
import com.thefirstlineofcode.basalt.oxm.parsing.IParsingContext;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.ProtocolException;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.error.BadRequest;
import com.thefirstlineofcode.basalt.xmpp.im.roster.Item.Ask;
import com.thefirstlineofcode.basalt.xmpp.im.roster.Item.Subscription;

@Parser(namespace="query", localName="jabber:iq:roster", objectType=Roster.class)
public class RosterParser {
	@ProcessAttributes("/item")
	public void processItemAttributes(IParsingContext<Roster> context, List<Attribute> attributes) {
		Item item = new Item();
		
		for (Attribute attribute : attributes) {
			if ("jid".equals(attribute.getName())) {
				item.setJid(JabberId.parse(attribute.getValue().toString()));
			} else if ("name".equals(attribute.getName())) {
				item.setName(attribute.getValue().toString());
			} else if ("subscription".equals(attribute.getName())) {
				try {
					item.setSubscription(Subscription.valueOf(attribute.getValue().toString().toUpperCase()));
				} catch (Exception e) {
					throw new ProtocolException(new BadRequest(String.format("Invalid value '%s' for attribute 'subscription'.",
							attribute.getValue().toString())), e);
				}
			} else if ("ask".equals(attribute.getName())) {
				if ("subscribe".equals(attribute.getValue().toString())) {
					item.setAsk(Ask.SUBSCRIBE);
				} else {
					throw new ProtocolException(new BadRequest(String.format("Invalid value '%s' for attribute 'ask'.",
							attribute.getValue().toString())));
				}
			} else {
				throw new ProtocolException(new BadRequest(String.format("Unknown attribute %s.", attribute.getName())));
			}
		}
		
		context.getObject().addOrUpdate(item);
	}
	
	@ProcessText("/item/group")
	public void processGroup(IParsingContext<Roster> context, Value<?> text) {
		Item[] items = context.getObject().getItems();
		Item current = items[items.length - 1];
		
		current.getGroups().add(text.getString());
	}
}
