package com.thefirstlineofcode.basalt.xmpp.datetime;

public class TimeZoneOffset {
	private boolean nonNegative;
	private int hours;
	private int minutes;
	
	public TimeZoneOffset() {
		nonNegative = true;
		hours = 0;
		minutes = 0;
	}
	
	public TimeZoneOffset(int hours, int minutes, boolean nonNegative) {
		this.hours = hours;
		this.minutes = minutes;
		this.nonNegative = nonNegative;
	}

	public boolean isNonNegative() {
		return nonNegative;
	}
	
	public void setNonNegative(boolean nonNegative) {
		this.nonNegative = nonNegative;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	public boolean isUTC() {
		return (hours == 0) && (minutes == 0);
	}
	
}
