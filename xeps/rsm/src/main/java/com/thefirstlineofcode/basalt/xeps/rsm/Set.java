package com.thefirstlineofcode.basalt.xeps.rsm;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ProtocolObject(localName="set", namespace="http://jabber.org/protocol/rsm")
public class Set {
	public static final Protocol PROTOCOL = new Protocol("http://jabber.org/protocol/rsm", "set");
	
	private Before before;
	@TextOnly
	private String after;
	@TextOnly
	private Integer count;
	private First first;
	@TextOnly
	private Integer index;
	@TextOnly
	private String last;
	@TextOnly
	private Integer max;
	
	public static Set totalNumber() {
		return limit(0);
	}
	
	public static Set limit(int max) {
		return first(max);
	}
	
	public static Set first(int max) {
		Set set = new Set();
		set.setMax(max);
		
		return set;
	}
	
	public static Set forward(int max, String after) {
		Set set = new Set();
		set.setMax(max);
		set.setAfter(after);
		
		return set;
	}
	
	public static Set backward(int max, String before) {
		Set set = new Set();
		set.setMax(max);
		set.setBefore(new Before(before));
		
		return set;
	}
	
	public static Set last(int max) {
		return backward(max, null);
	}
	
	public static Set index(int max, int index) {
		Set set = new Set();
		set.setMax(max);
		set.setIndex(index);
		
		return set;
	}
	
	public Before getBefore() {
		return before;
	}
	
	public void setBefore(Before before) {
		this.before = before;
	}
	
	public String getAfter() {
		return after;
	}
	
	public void setAfter(String after) {
		this.after = after;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public First getFirst() {
		return first;
	}
	
	public void setFirst(First first) {
		this.first = first;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public String getLast() {
		return last;
	}
	
	public void setLast(String last) {
		this.last = last;
	}
	
	public Integer getMax() {
		return max;
	}
	
	public void setMax(Integer max) {
		this.max = max;
	}
	
}
