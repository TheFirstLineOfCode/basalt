package com.thefirstlineofcode.basalt.xmpp.core.stream;

import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

public class Stream {
	public static final Protocol PROTOCOL = new Protocol("http://etherx.jabber.org/streams", "stream");
	
	private JabberId from;
	private JabberId to;
	private String id;
	private String version;
	private String lang;
	private boolean close;
	private String defaultNamespace;
	
	public Stream() {
		this(false);
	}
	
	public Stream(boolean close) {
		this.close = close;
	}
	
	public JabberId getFrom() {
		return from;
	}
	
	public void setFrom(JabberId from) {
		this.from = from;
	}
	
	public JabberId getTo() {
		return to;
	}
	
	public void setTo(JabberId to) {
		this.to = to;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public void setClose(boolean close) {
		this.close = close;
	}
	
	public boolean getClose() {
		return close;
	}
	
	public boolean isClose() {
		return close;
	}

	public String getDefaultNamespace() {
		return defaultNamespace;
	}

	public void setDefaultNamespace(String defaultNamespace) {
		this.defaultNamespace = defaultNamespace;
	}
	
}
