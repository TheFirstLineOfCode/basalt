package com.thefirstlineofcode.basalt.xmpp.core;

import java.util.StringTokenizer;

public final class JabberId {
	private String node;
	private String domain;
	private String resource;
	
	private static final char CHAR_AT = '@';
	private static final char CHAR_SLASH = '/';
	
	public JabberId() {
		
	}
	
	public JabberId(String domain) {
		this(null, domain);
	}
	
	public JabberId(String node, String domain) {
		this(node, domain, null);
	}
	
	public JabberId(String node, String domain, String resource) {
		this.node = node;
		this.domain = domain;
		this.resource = resource;
	}
	
	public String getNode() {
		return node;
	}
	
	public void setNode(String node) {
		this.node = node;
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		
		if (node != null)
			hash += 31 * hash + node.hashCode();
		
		hash += 31 * hash + (domain == null ? 0 : domain.hashCode());
		
		if (resource != null)
			hash += 31 * hash + resource.hashCode();
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this) {
			return true;
		}
		
		if (obj instanceof JabberId) {
			JabberId other = (JabberId)obj;
			
			if (!equalsEvenNull(this.node, other.node))
				return false;
			
			if (this.domain == null || other.domain == null) {
				return false;
			}
			
			if (!this.domain.equals(other.domain)) {
				return false;
			}
			
			if (!equalsEvenNull(this.resource, other.resource))
				return false;
			
			return true;
		}
		
		return false;
	}
	
	private boolean equalsEvenNull(Object obj1, Object obj2) {
		if (obj1 != null) {
			return obj1.equals(obj2);
		} else {
			return obj2 == null;
		}
	}
	
	@Override
	public String toString() {
		validateJid();
		
		StringBuilder sb = new StringBuilder();
		
		if (node != null)
			sb.append(node).append(CHAR_AT);
		sb.append(domain);
		if (resource != null)
			sb.append(CHAR_SLASH).append(resource);
		
		return sb.toString();
	}
	
	public boolean isBareId() {
		return resource == null;
	}
	
	public JabberId getBareId() {
		return new JabberId(node, domain);
	}
	
	public String getBareIdString() {
		if (domain == null)
			throw new IllegalStateException("Null domain");

		if (node == null)
			return domain;
		else
			return String.format("%s%s%s", node, CHAR_AT, domain);
	}
	
	public static JabberId parse(String jidString) {
		JabberId jid = new JabberId();
		jid.fromJidString(jidString);
		
		return jid;
	}
	
	private void validateJid() {
		if (node != null && node.length() > 1023)
			throw new IllegalArgumentException("Name identifier of JID mustn't be more than 1023 bytes in length");
		
		if (domain != null && domain.length() > 1023)
			throw new IllegalArgumentException("Domain identifier of JID mustn't be more than 1023 bytes in length");
		
		if (resource != null && resource.length() > 1023)
			throw new IllegalArgumentException("Resource identifier of JID mustn't be more than 1023 bytes in length");
		
		// TODO JabberId format validation
	}
	
	private void fromJidString(String jid) {
		if (jid == null)
			throw new IllegalArgumentException("Null jid.");
		
		if (jid.indexOf(CHAR_AT) == -1) {
			if (jid.indexOf(CHAR_SLASH) != -1)
				throw new MalformedJidException();
			
			domain = jid;
		} else {
			StringTokenizer atTokenizer = new StringTokenizer(jid, String.valueOf(CHAR_AT));
			if (atTokenizer.countTokens() != 2)
				throw new MalformedJidException();
			
			node = atTokenizer.nextToken();
			String domainAndResource = atTokenizer.nextToken();
			
			if (domainAndResource.indexOf(CHAR_SLASH) == -1) {
				domain = domainAndResource;
			} else {
				StringTokenizer slashTokenizer = new StringTokenizer(domainAndResource, String.valueOf(CHAR_SLASH));
				if (slashTokenizer.countTokens() != 2)
					throw new MalformedJidException();
				
				domain = slashTokenizer.nextToken();
				resource = slashTokenizer.nextToken();
			}
		}
		
		try {
			validateJid();
		} catch (IllegalArgumentException e) {
			throw new MalformedJidException(e);
		}		
	}
	
}
