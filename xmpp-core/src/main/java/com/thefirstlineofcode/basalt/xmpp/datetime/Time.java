package com.thefirstlineofcode.basalt.xmpp.datetime;

import java.text.ParseException;
import java.util.Calendar;


public class Time {
	private int hours;
	private int minutes;
	private int seconds;
	private int milliSeconds;
	private TimeZoneOffset timeZoneOffset;
	
	public Time() {
		Calendar calendar = Calendar.getInstance();
		hours = calendar.get(Calendar.HOUR_OF_DAY);
		minutes = calendar.get(Calendar.MINUTE);
		seconds = calendar.get(Calendar.MINUTE);
		milliSeconds = calendar.get(Calendar.MILLISECOND);
	}
	
	public Time(int hours, int minutes, int seconds, int milliSeconds) {
		this(hours, minutes, seconds, milliSeconds, null);
	}
	
	public Time(int hours, int minutes, int seconds, int milliSeconds, TimeZoneOffset offset) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		this.milliSeconds = milliSeconds;
		
		this.timeZoneOffset = (offset == null ? new TimeZoneOffset() : offset);
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
	
	public int getSeconds() {
		return seconds;
	}
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public int getMilliSeconds() {
		return milliSeconds;
	}
	
	public void setMilliSeconds(int milliSeconds) {
		this.milliSeconds = milliSeconds;
	}

	public TimeZoneOffset getTimeZoneOffset() {
		if (timeZoneOffset == null)
			timeZoneOffset = new TimeZoneOffset();
		
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(TimeZoneOffset offset) {
		this.timeZoneOffset = offset;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%02d", hours)).
			append(':').
			append(String.format("%02d", minutes)).
			append(':').
			append(String.format("%02d", seconds)).
			append('.').
			append(String.format("%03d", milliSeconds));
		
		if (timeZoneOffset == null || timeZoneOffset.isUTC()) {
			sb.append('Z');
		} else {
			sb.append(timeZoneOffset.isNonNegative() ? '+' : '-');
			sb.append(String.format("%02d", timeZoneOffset.getHours())).
				append(':').
				append(String.format("%02d", timeZoneOffset.getMinutes()));
		}
		
		return sb.toString();
	}
	
	public static Time parse(String string) throws ParseException {
		if (string.length() != 8 && string.length() != 9 &&
				string.length() != 12 && string.length() != 13 &&
					string.length() != 14 && string.length() != 18)
			throw new ParseException("invalid time", 0);
		
		int index = 2;
		int hours = Integer.parseInt(string.substring(index - 2, index));
		checkMaxValue(hours, 23, index);
		checkColonChar(string, index);
		
		index += 3;
		int minutes = Integer.parseInt(string.substring(index - 2, index));
		checkMaxValue(minutes, 59, index);
		checkColonChar(string, index);
		
		index += 2;
		int seconds = Integer.parseInt(string.substring(index - 1, index + 1));
		checkMaxValue(seconds, 59, index);
		
		if (string.length() == 8 || (string.length() == 9 && string.charAt(8) == 'Z')) {
			return new Time(hours, minutes, seconds, 0);
		}
		
		index++;
		int milliSeconds = 0;
		if (string.charAt(index) == '.') {
			index += 3;
			milliSeconds = Integer.parseInt(string.substring(index - 2, index + 1));
			checkMaxValue(milliSeconds, 999, index);
			
			if (string.length() == 12 || (string.length() == 13 && string.charAt(12) == 'Z')) {
				return new Time(hours, minutes, seconds, milliSeconds);
			}
			
			index++;
		}
		
		boolean isNonNegative;
		if (string.charAt(index) == '+') {
			isNonNegative = true;
		} else if (string.charAt(index) == '-') {
			isNonNegative = false;
		} else {
			throw new ParseException("invalid time", index);
		}
		
		index += 3;
		int tzdHours = Integer.parseInt(string.substring(index - 2, index));
		checkMaxValue(tzdHours, 23, index);
		checkColonChar(string, index);
		
		index += 2;
		int tzdMinutes = Integer.parseInt(string.substring(index - 1, index + 1));
		checkMaxValue(tzdMinutes, 59, index);
		
		return new Time(hours, minutes, seconds, milliSeconds, new TimeZoneOffset(tzdHours, tzdMinutes, isNonNegative));
	}

	private static void checkMaxValue(int value, int max, int index) throws ParseException {
		if (value > max) {
			throw new ParseException("invalid time", index);
		}
		
	}

	private static void checkColonChar(String string, int index) throws ParseException {
		if (string.charAt(index) != ':') {
			throw new ParseException("invalid time", index);
		}
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		
		hash += 31 * hash + hours;
		hash += 31 * hash + minutes;
		hash += 31 * hash + seconds;
		hash += 31 * hash + milliSeconds;
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Time) {
			Time other = (Time)obj;
			return this.hours == other.hours && this.minutes == other.minutes &&
					this.seconds == other.seconds && this.milliSeconds == other.milliSeconds;
		}
		
		return false;
	}
	
}
