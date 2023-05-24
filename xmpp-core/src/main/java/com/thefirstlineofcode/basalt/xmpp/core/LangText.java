package com.thefirstlineofcode.basalt.xmpp.core;

import com.thefirstlineofcode.basalt.xmpp.Constants;
import com.thefirstlineofcode.basalt.xmpp.HandyUtils;

public final class LangText {
	private String lang;
	private String text;
	
	public static final String PREFIX_LANG_TEXT = Constants.NAMESPACE_XML;
	public static final String LOCAL_NAME_LANG_TEXT = "lang";
	
	public static final String NAME_LANG_TEXT = PREFIX_LANG_TEXT + ":" + LOCAL_NAME_LANG_TEXT;
	
	public LangText() {
		this(null);
	}
	
	public LangText(String text) {
		this.text = text;
	}
	
	public LangText(String text, String lang) {
		this.text = text;
		this.lang = lang;
	}
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		
		if (lang != null)
			hash += 31 * hash + lang.hashCode();
		
		if (text != null)
			hash += 31 * hash + text.hashCode();
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LangText) {
			LangText other = (LangText)obj;
			
			return HandyUtils.equalsEvenNull(lang, other.lang) &&
					HandyUtils.equalsEvenNull(text, other.text);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LangText[");
		if (lang != null)
			sb.append("lang='").append(lang).append("',");
		
		if (text != null) {
			sb.append("text='").append(text).append("'");
		}
		
		if (sb.charAt(sb.length() - 1) == ',')
			sb.deleteCharAt(sb.length() - 1);
		
		sb.append(']');
		
		return sb.toString();
	}
}
