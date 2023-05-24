package com.thefirstlineofcode.basalt.oxm.coc.converters;

import org.junit.Assert;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.conversion.converters.String2DateTimeConverter;
import com.thefirstlineofcode.basalt.xmpp.datetime.Date;
import com.thefirstlineofcode.basalt.xmpp.datetime.DateTime;
import com.thefirstlineofcode.basalt.xmpp.datetime.Time;
import com.thefirstlineofcode.basalt.xmpp.datetime.TimeZoneOffset;

public class String2DateTimeConverterTest {
	@Test
	public void from() {
		String2DateTimeConverter converter = new String2DateTimeConverter();
		DateTime dateTime = converter.from("1970-01-01T13:40:05Z");
		
		Assert.assertEquals(1970, dateTime.getDate().getYear());
		Assert.assertEquals(0, dateTime.getDate().getMonth());
		Assert.assertEquals(1, dateTime.getDate().getDate());
		
		Assert.assertEquals(13, dateTime.getTime().getHours());
		Assert.assertEquals(40, dateTime.getTime().getMinutes());
		Assert.assertEquals(5, dateTime.getTime().getSeconds());
		
		Assert.assertTrue(dateTime.getTime().getTimeZoneOffset().isUTC());
		
		dateTime = converter.from("1969-07-20T21:56:15-05:00");
		
		Assert.assertEquals(1969, dateTime.getDate().getYear());
		Assert.assertEquals(6, dateTime.getDate().getMonth());
		Assert.assertEquals(20, dateTime.getDate().getDate());
		
		Assert.assertEquals(21, dateTime.getTime().getHours());
		Assert.assertEquals(56, dateTime.getTime().getMinutes());
		Assert.assertEquals(15, dateTime.getTime().getSeconds());
		
		Assert.assertEquals(5, dateTime.getTime().getTimeZoneOffset().getHours());
		Assert.assertEquals(0, dateTime.getTime().getTimeZoneOffset().getMinutes());
		Assert.assertFalse(dateTime.getTime().getTimeZoneOffset().isNonNegative());
		
		dateTime = converter.from("1969-07-20T21:56:15.005-05:00");
		
		Assert.assertEquals(1969, dateTime.getDate().getYear());
		Assert.assertEquals(6, dateTime.getDate().getMonth());
		Assert.assertEquals(20, dateTime.getDate().getDate());
		
		Assert.assertEquals(21, dateTime.getTime().getHours());
		Assert.assertEquals(56, dateTime.getTime().getMinutes());
		Assert.assertEquals(15, dateTime.getTime().getSeconds());
		Assert.assertEquals(5, dateTime.getTime().getMilliSeconds());
		
		Assert.assertEquals(5, dateTime.getTime().getTimeZoneOffset().getHours());
		Assert.assertEquals(0, dateTime.getTime().getTimeZoneOffset().getMinutes());
		Assert.assertFalse(dateTime.getTime().getTimeZoneOffset().isNonNegative());
	}
	
	@Test
	public void to() {
		DateTime dateTime = new DateTime(new Date(1969, 6, 20), new Time(21, 56, 15, 0, new TimeZoneOffset(5, 0, false)));
		Assert.assertEquals("1969-07-20T21:56:15.000-05:00", dateTime.toString());
		
		dateTime = new DateTime(new Date(1969, 6, 20), new Time(21, 56, 15, 0));
		Assert.assertEquals("1969-07-20T21:56:15.000Z", dateTime.toString());
	}
}
