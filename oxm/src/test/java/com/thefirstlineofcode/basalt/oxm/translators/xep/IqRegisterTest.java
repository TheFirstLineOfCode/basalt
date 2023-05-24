package com.thefirstlineofcode.basalt.oxm.translators.xep;

import org.junit.Before;
import org.junit.Test;

import com.thefirstlineofcode.basalt.oxm.IOxmFactory;
import com.thefirstlineofcode.basalt.oxm.OxmService;
import com.thefirstlineofcode.basalt.oxm.coc.CocParserFactory;
import com.thefirstlineofcode.basalt.oxm.coc.CocTranslatorFactory;
import com.thefirstlineofcode.basalt.oxm.parsers.xep.TIqRegisterParserFactory;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TIqRegister;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRegistrationField;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRegistrationForm;
import com.thefirstlineofcode.basalt.oxm.xep.ibr.TRemove;
import com.thefirstlineofcode.basalt.oxm.xep.oob.TXOob;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TField;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TOption;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TXData;
import com.thefirstlineofcode.basalt.oxm.xep.xdata.TField.Type;
import com.thefirstlineofcode.basalt.xmpp.HandyUtils;
import com.thefirstlineofcode.basalt.xmpp.core.IqProtocolChain;
import com.thefirstlineofcode.basalt.xmpp.core.JabberId;
import com.thefirstlineofcode.basalt.xmpp.core.stanza.Iq;

import junit.framework.Assert;

public class IqRegisterTest {
private IOxmFactory oxmFactory;
	
	@Before
	public void before() {
		oxmFactory = OxmService.createStandardOxmFactory();
		
		oxmFactory.register(
				new IqProtocolChain(TIqRegister.PROTOCOL),
				new TIqRegisterParserFactory()
			);
		
		oxmFactory.register(
				new IqProtocolChain().
					next(TIqRegister.PROTOCOL).
					next(TXData.PROTOCOL),
				new CocParserFactory<>(
						TXData.class)
			);
		
		oxmFactory.register(
				new IqProtocolChain().
					next(TIqRegister.PROTOCOL).
					next(TXOob.PROTOCOL),
				new CocParserFactory<>(
						TXOob.class)
			);
		
		oxmFactory.register(
				TIqRegister.class,
				new TIqRegisterTranslatorFactory()
			);
		
		oxmFactory.register(
				TXData.class,
				new CocTranslatorFactory<>(
						TXData.class)
			);
		
		oxmFactory.register(
				TXOob.class,
				new CocTranslatorFactory<>(
						TXOob.class)
			);
	}
	
	@Test
	public void translateNullRegistration() {
		Iq iq = new Iq();
		iq.setType(Iq.Type.GET);
		iq.setTo(JabberId.parse("shakespeare.lit"));
		iq.setId("reg1");
		
		iq.setObject(new TIqRegister());
		
		String iqRegisterMessage = oxmFactory.translate(iq);
		
		Object obj = oxmFactory.parse(iqRegisterMessage);
		Assert.assertTrue(obj instanceof Iq);
		iq = (Iq)obj;
		Assert.assertEquals(Iq.Type.GET, iq.getType());
		Assert.assertEquals("shakespeare.lit", iq.getTo().toString());
		Assert.assertEquals("reg1", iq.getId());
		
		Assert.assertNotNull(iq.getObject());
		Assert.assertTrue(iq.getObject() instanceof TIqRegister);
		TIqRegister iqRegister = (TIqRegister)iq.getObject();
		Assert.assertNull(iqRegister.getRegister());
		Assert.assertNull(iqRegister.getXData());
		Assert.assertNull(iqRegister.getOob());
	}
	
	@Test
	public void parseReturnedRegistrationForm() {
		String instructions = "Choose a username and password for use with this service. Please also provide your email address.";
		
		Iq iq = new Iq();
		iq.setType(Iq.Type.RESULT);
		iq.setId("reg1");
		TIqRegister iqRegister = new TIqRegister();
		TRegistrationForm registrationForm = new TRegistrationForm();
		registrationForm.getFields().add(new TRegistrationField("instructions", instructions));
		registrationForm.getFields().add(new TRegistrationField("username", null));
		registrationForm.getFields().add(new TRegistrationField("password", null));
		registrationForm.getFields().add(new TRegistrationField("email", null));
		
		iqRegister.setRegister(registrationForm);
		
		iq.setObject(iqRegister);
		
		String iqRegisterMessage = oxmFactory.translate(iq);
		
		Object obj = oxmFactory.parse(iqRegisterMessage);
		Assert.assertTrue(obj instanceof Iq);
		iq = (Iq)obj;
		Assert.assertEquals(Iq.Type.RESULT, iq.getType());
		Assert.assertEquals("reg1", iq.getId());
		
		Assert.assertNotNull(iq.getObject());
		Assert.assertTrue(iq.getObject() instanceof TIqRegister);
		iqRegister = (TIqRegister)iq.getObject();
		Assert.assertNotNull(iqRegister.getRegister());
		Assert.assertTrue(iqRegister.getRegister() instanceof TRegistrationForm);
		
		TRegistrationForm form = (TRegistrationForm)iqRegister.getRegister();
		
		Assert.assertEquals(4, form.getFields().size());
		Assert.assertTrue(checkRegistrationField("instructions", instructions, form));
		Assert.assertTrue(checkRegistrationField("username", null, form));
		Assert.assertTrue(checkRegistrationField("password", null, form));
		Assert.assertTrue(checkRegistrationField("email", null, form));
	}
	
	@Test
	public void parseRegistrationForm() {
		Iq iq = new Iq();
		iq.setType(Iq.Type.RESULT);
		iq.setId("reg1");
		TIqRegister iqRegister = new TIqRegister();
		TRegistrationForm registrationForm = new TRegistrationForm();
		registrationForm.setRegistered(true);
		registrationForm.getFields().add(new TRegistrationField("username", "juliet"));
		registrationForm.getFields().add(new TRegistrationField("password", "R0m30"));
		registrationForm.getFields().add(new TRegistrationField("email", "juliet@capulet.com"));
		
		iqRegister.setRegister(registrationForm);
		
		iq.setObject(iqRegister);
		
		String iqRegisterMessage = oxmFactory.translate(iq);
		
		Object obj = oxmFactory.parse(iqRegisterMessage);
		Assert.assertTrue(obj instanceof Iq);
		iq = (Iq)obj;
		Assert.assertEquals(Iq.Type.RESULT, iq.getType());
		Assert.assertEquals("reg1", iq.getId());
		
		Assert.assertNotNull(iq.getObject());
		Assert.assertTrue(iq.getObject() instanceof TIqRegister);
		iqRegister = (TIqRegister)iq.getObject();
		Assert.assertNotNull(iqRegister.getRegister());
		Assert.assertTrue(iqRegister.getRegister() instanceof TRegistrationForm);
		
		TRegistrationForm form = (TRegistrationForm)iqRegister.getRegister();
		
		Assert.assertEquals(3, form.getFields().size());
		Assert.assertTrue(checkRegistrationField("username", "juliet", form));
		Assert.assertTrue(checkRegistrationField("password", "R0m30", form));
		Assert.assertTrue(checkRegistrationField("email", "juliet@capulet.com", form));
		
		Assert.assertTrue(form.isRegistered());
	}
	
	@Test
	public void parseOob() {
		String instructions = "To register, visit http://www.shakespeare.lit/contests.php";
		
		Iq iq = new Iq();
		iq.setType(Iq.Type.RESULT);
		iq.setId("reg3");
		iq.setFrom(JabberId.parse("contests.shakespeare.lit"));
		iq.setTo(JabberId.parse("juliet@capulet.com/balcony"));
		
		TIqRegister iqRegister = new TIqRegister();
		TRegistrationForm registrationForm = new TRegistrationForm();
		registrationForm.getFields().add(new TRegistrationField("instructions", instructions));
		
		iqRegister.setRegister(registrationForm);
		iqRegister.setOob(new TXOob("http://www.shakespeare.lit/contests.php"));
		
		iq.setObject(iqRegister);
		
		String iqRegisterMessage = oxmFactory.translate(iq);
		
		Object obj = oxmFactory.parse(iqRegisterMessage);
		Assert.assertTrue(obj instanceof Iq);
		iq = (Iq)obj;
		Assert.assertEquals(Iq.Type.RESULT, iq.getType());
		Assert.assertEquals("reg3",iq.getId());
		
		Assert.assertEquals("juliet@capulet.com/balcony", iq.getTo().toString());
		
		Assert.assertNotNull(iq.getObject());
		Assert.assertTrue(iq.getObject() instanceof TIqRegister);
		
		iqRegister = (TIqRegister)iq.getObject();
		
		Assert.assertNotNull(iqRegister.getRegister());
		Assert.assertTrue(iqRegister.getRegister() instanceof TRegistrationForm);
		TRegistrationForm form = (TRegistrationForm)iqRegister.getRegister();
		Assert.assertEquals(1, form.getFields().size());
		Assert.assertTrue(checkRegistrationField("instructions", instructions, form));
		
		Assert.assertNotNull(iqRegister.getOob());
		
		TXOob oob = iqRegister.getOob();
		
		Assert.assertEquals("http://www.shakespeare.lit/contests.php", oob.getUrl());
	}
	
	@Test
	public void parseRemove() {
		Iq iq = new Iq();
		iq.setType(Iq.Type.RESULT);
		iq.setId("reg1");
		
		TIqRegister iqRegister = new TIqRegister();
		iqRegister.setRegister(new TRemove());
		
		iq.setObject(iqRegister);
		
		String iqRegisterMessage = oxmFactory.translate(iq);
		
		Object obj = oxmFactory.parse(iqRegisterMessage);
		Assert.assertTrue(obj instanceof Iq);
		iq = (Iq)obj;
		Assert.assertEquals(Iq.Type.RESULT, iq.getType());
		Assert.assertEquals("reg1", iq.getId());
		
		Assert.assertNotNull(iq.getObject());
		Assert.assertTrue(iq.getObject() instanceof TIqRegister);
		iqRegister = (TIqRegister)iq.getObject();
		Assert.assertNotNull(iqRegister.getRegister());
		Assert.assertTrue(iqRegister.getRegister() instanceof TRemove);
	}

	private boolean checkRegistrationField(String name, String value, TRegistrationForm form) {
		for (TRegistrationField field : form.getFields()) {
			if (name.equals(field.getName())) {
				if (value == null) {
					return field.getValue() == null;
				} else {
					return value.equals(field.getValue());
				}
			}
		}
		
		return false;
	}
	
	@Test
	public void parseReturnedXDataRegistrationForm() {
		String instructions1 = "Use the enclosed form to register. If your Jabber client does not support Data Forms, visit http://www.shakespeare.lit/contests.php";
		String instructions2 = "Please provide the following information to sign up for our special contests!";
		
		Iq iq = new Iq();
		iq.setType(Iq.Type.RESULT);
		iq.setId("reg3");
		iq.setFrom(JabberId.parse("contests.shakespeare.lit"));
		iq.setTo(JabberId.parse("juliet@capulet.com/balcony"));
		
		TIqRegister iqRegister = new TIqRegister();
		TRegistrationForm form = new TRegistrationForm();
		form.getFields().add(new TRegistrationField("instructions", instructions1));
		
		iqRegister.setRegister(form);
		
		TXData xData = new TXData();
		
		xData.setType(TXData.Type.FORM);
		xData.setTitle("Contest Registration");
		
		xData.getInstructions().add(instructions2);
		
		TField field1 = new TField();
		field1.setType(TField.Type.HIDDEN);
		field1.setVar("form_type");
		field1.getValues().add("jabber:iq:register");
		xData.getFields().add(field1);
		
		TField field2 = new TField();
		field2.setType(TField.Type.TEXT_SINGLE);
		field2.setLabel("Given Name");
		field2.setVar("first");
		field2.setRequired(true);
		xData.getFields().add(field2);
		
		TField field3 = new TField();
		field3.setType(TField.Type.TEXT_SINGLE);
		field3.setLabel("Family Name");
		field3.setVar("last");
		field3.setRequired(true);
		xData.getFields().add(field3);
		
		TField field4 = new TField();
		field4.setType(TField.Type.TEXT_SINGLE);
		field4.setLabel("Email Address");
		field4.setVar("email");
		field4.setRequired(true);
		xData.getFields().add(field4);
		
		TField field5 = new TField();
		field5.setType(TField.Type.LIST_SINGLE);
		field5.setLabel("Gender");
		field5.setVar("x-gender");
		field5.getOptions().add(new TOption("Male", "M"));
		field5.getOptions().add(new TOption("Female", "F"));
		xData.getFields().add(field5);
		
		iqRegister.setXData(xData);
		
		iq.setObject(iqRegister);
		
		String iqRegisterMessage = oxmFactory.translate(iq);
		
		Object obj = oxmFactory.parse(iqRegisterMessage);
		Assert.assertTrue(obj instanceof Iq);
		iq = (Iq)obj;
		Assert.assertEquals(Iq.Type.RESULT, iq.getType());
		Assert.assertEquals("reg3",iq.getId());
		
		Assert.assertEquals("juliet@capulet.com/balcony", iq.getTo().toString());
		
		Assert.assertNotNull(iq.getObject());
		Assert.assertTrue(iq.getObject() instanceof TIqRegister);
		
		iqRegister = (TIqRegister)iq.getObject();
		
		Assert.assertNull(iqRegister.getOob());
		
		Assert.assertTrue(iqRegister.getRegister() instanceof TRegistrationForm);
		form = (TRegistrationForm)iqRegister.getRegister();
		
		Assert.assertEquals(1, form.getFields().size());
		
		checkRegistrationField("instructions", instructions1, form);
		
		Assert.assertNotNull(iqRegister.getXData());
		
		xData = iqRegister.getXData();
		
		Assert.assertEquals(1, xData.getInstructions().size());
		Assert.assertEquals(instructions2, xData.getInstructions().get(0));
		
		Assert.assertEquals(5, xData.getFields().size());
		
		Assert.assertTrue(checkXDataHiddenOrTextSingleField(TField.Type.HIDDEN, null, "form_type", false, new String[] {"jabber:iq:register"}, xData.getFields().get(0)));
		Assert.assertTrue(checkXDataHiddenOrTextSingleField(TField.Type.TEXT_SINGLE, "Given Name", "first", true, new String[0], xData.getFields().get(1)));
		Assert.assertTrue(checkXDataHiddenOrTextSingleField(TField.Type.TEXT_SINGLE, "Family Name", "last", true, new String[0], xData.getFields().get(2)));
		Assert.assertTrue(checkXDataHiddenOrTextSingleField(TField.Type.TEXT_SINGLE, "Email Address", "email", true, new String[0], xData.getFields().get(3)));
		
		Assert.assertTrue(checkXDataListSingleField("Gender", "x-gender", new TOption[] {new TOption("Male", "M"), new TOption("Female", "F")}, xData.getFields().get(4)));		
	}

	private boolean checkXDataListSingleField(String label, String var, TOption[] options, TField field) {
		if (!HandyUtils.equalsEvenNull(label, field.getLabel()))
			return false;
		
		if (!HandyUtils.equalsEvenNull(var, field.getVar()))
			return false;
			
		if (options.length == 0 && (field.getOptions() != null || field.getOptions().size() != 0))
			return false;
			
		if (options.length != 0 && (field.getOptions() == null || field.getOptions().size() != options.length))
			return false;
			
		for (int i = 0; i < options.length; i++) {
			TOption option = options[i];
			TOption fOption = field.getOptions().get(i);
				
			if (!HandyUtils.equalsEvenNull(option.getLabel(), fOption.getLabel()))
				return false;
		
			if (!HandyUtils.equalsEvenNull(option.getValue(), fOption.getValue()))
				return false;
		}
		
		return true;
	}

	private boolean checkXDataHiddenOrTextSingleField(Type type, String label, String var, boolean required, String[] values, TField field) {
		if (field.getType() != type)
			return false;
			
		if (!HandyUtils.equalsEvenNull(field.getLabel(), label))
			return false;
			
		if (!HandyUtils.equalsEvenNull(field.getVar(), var))
			return false;
			
		if (field.isRequired() != required)
			return false;
			
		if (values.length == 0 && field.getValues().size() != 0)
			return false;
			
		if (values.length != 0 && (field.getValues().size() == 0 || values.length != field.getValues().size()))
			return false;
			
		for (int i = 0; i < values.length; i++) {
			if (!values[i].equals(field.getValues().get(i)))
				return false;
		}
		
		return true;
	}
}
