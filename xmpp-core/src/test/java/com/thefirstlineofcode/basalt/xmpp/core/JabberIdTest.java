package com.thefirstlineofcode.basalt.xmpp.core;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class JabberIdTest {
	private JabberId jid;
	
	private String jidString1;
	private String jidString2;
	private String jidString3;
	private String jidString4;
	
	@Before
	public void before() throws Exception {
		jidString1 = "dongger@example.com/default";
		jidString2 = "dongger@example.com";
		jidString3 = "example.com";
		jidString4 = "example.com$default";
		jidString4 = "dongger@example@com/default";
		
	}
	
	@Test
	public void parse() {
		jid = JabberId.parse(jidString1);
		Assert.assertEquals("dongger", jid.getNode());
		Assert.assertEquals("example.com", jid.getDomain());
		Assert.assertEquals("default", jid.getResource());
		
		jid =  JabberId.parse(jidString2);
		Assert.assertEquals("dongger", jid.getNode());
		Assert.assertEquals("example.com", jid.getDomain());
		Assert.assertEquals(null, jid.getResource());
		
		jid =  JabberId.parse(jidString3);
		Assert.assertEquals(null, jid.getNode());
		Assert.assertEquals("example.com", jid.getDomain());
		Assert.assertEquals(null, jid.getResource());
		
		try {
			jid = JabberId.parse(jidString4);
			Assert.fail("Should throw an exception");
		} catch (MalformedJidException e) {
			// should run to here
		}
	}
}
