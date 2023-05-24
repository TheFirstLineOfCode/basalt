package com.thefirstlineofcode.basalt.xmpp.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date {
	private int year;
	private int month;
	private int date;
	
	public Date() {
		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		date = calendar.get(Calendar.DATE);
	}
	
	public Date(int year, int month, int date) {
		this.year = year;
		this.month = month;
		this.date = date;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getDate() {
		return date;
	}
	
	public void setDate(int date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, date);
		
		return df.format(calendar.getTime());
	}
	
	public static Date parse(String string) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calendar;
		calendar = Calendar.getInstance();
		calendar.setTime(df.parse(string));
		
		return new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Date) {
			Date other = (Date)obj;
			return this.year == other.year && this.month == other.month && this.date == other.date;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		
		hash += 31 * hash + year;
		hash += 31 * hash + month;
		hash += 31 * hash + date;
		
		return hash;
	}
	
}
