package com.thefirstlineofcode.basalt.oxm.coc.converters;

import org.junit.Assert;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.conversion.converters.String2TimeConverter;
import com.thefirstlineofcode.basalt.xmpp.datetime.Time;

public class String2TimeConverterTest {
	@Test
	public void from() {
		String2TimeConverter converter = new String2TimeConverter();
		Time time = converter.from("13:40:05Z");
		
		Assert.assertEquals(13, time.getHours());
		Assert.assertEquals(40, time.getMinutes());
		Assert.assertEquals(5, time.getSeconds());
		
		time = converter.from("13:40:05");
		
		Assert.assertEquals(13, time.getHours());
		Assert.assertEquals(40, time.getMinutes());
		Assert.assertEquals(5, time.getSeconds());
		
		time = converter.from("21:56:15-05:00");
		
		Assert.assertEquals(21, time.getHours());
		Assert.assertEquals(56, time.getMinutes());
		Assert.assertEquals(15, time.getSeconds());
		
		Assert.assertEquals(5, time.getTimeZoneOffset().getHours());
		Assert.assertEquals(0, time.getTimeZoneOffset().getMinutes());
		Assert.assertFalse(time.getTimeZoneOffset().isNonNegative());
	}
}
