package com.thefirstlineofcode.basalt.xmpp.datetime;

import java.text.ParseException;
import java.util.Calendar;

import com.thefirstlineofcode.basalt.xmpp.HandyUtils;

public class DateTime {
	private Date date;
	private Time time;
	
	public DateTime() {
		this(new Date(), new Time());
	}
	
	public DateTime(Date date, Time time) {
		this.date = date;
		this.time = time;
	}
	
	public DateTime(java.util.Date javaDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(javaDate);
		
		date = new Date(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DATE));
		time = new Time(calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
					calendar.get(Calendar.SECOND),
						calendar.get(Calendar.MILLISECOND));
	}
	
	public java.util.Date getJavaDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(
				this.date.getYear(),
				this.date.getMonth(),
					this.date.getDate(),
						this.time.getHours(),
							this.time.getMinutes(),
								this.time.getSeconds()
		);
		calendar.set(Calendar.MILLISECOND, time.getMilliSeconds());
		
		return calendar.getTime();
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Time getTime() {
		return time;
	}
	
	public void setTime(Time time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return String.format("%sT%s", date.toString(), time.toString());
	}
	
	public static DateTime parse(String string) throws ParseException {
		int timeSeperatorIndex = string.indexOf('T');
		if (timeSeperatorIndex == -1 || timeSeperatorIndex == string.length() - 1) {
			throw new ParseException("invalid datetime", 0);
		}
		
		String dateString = string.substring(0, timeSeperatorIndex);
		String timeString = string.substring(timeSeperatorIndex + 1, string.length());
		
		return new DateTime(Date.parse(dateString), Time.parse(timeString));
	}
	
	@Override
	public int hashCode() {
		return date.hashCode() + time.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateTime) {
			DateTime other = (DateTime)obj;
			
			return HandyUtils.equalsExceptNull(this.getDate(), other.getDate()) &&
					HandyUtils.equalsExceptNull(this.getTime(), other.getTime());
		}
		
		return false;
	}
}
