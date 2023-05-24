package com.thefirstlineofcode.basalt.xeps.xdata;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.ProtocolObject;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.Validate;
import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.ValidationClass;
import com.thefirstlineofcode.basalt.oxm.validation.ValidationException;
import com.thefirstlineofcode.basalt.xmpp.core.Protocol;

@ValidationClass
@ProtocolObject(namespace="jabber:x:data", localName="x")
public class XData {
	public static final Protocol PROTOCOL = new Protocol("jabber:x:data", "x");
	
	public enum Type {
		FORM,
		SUBMIT,
		CANCEL,
		RESULT
	}
	
	@TextOnly
	private String title;
	@String2XDataType
	private Type type;
	@Array(value=String.class, elementName="instructions")
	@TextOnly
	private List<String> instructions;
	private Reported reported;
	@Array(Field.class)
	private List<Field> fields;
	@Array(Item.class)
	private List<Item> items;
	
	public XData() {}
	
	public XData(Type type) {
		this.type = type;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<String> getInstructions() {
		if (instructions == null)
			instructions = new ArrayList<>();
		
		return instructions;
	}
	
	public void setInstructions(List<String> instructions) {
		this.instructions = instructions;
	}
	
	public Reported getReported() {
		return reported;
	}
	
	public void setReported(Reported reported) {
		this.reported = reported;
	}
	
	public List<Field> getFields() {
		if (fields == null) {
			fields = new ArrayList<>();
		}
		
		return fields;
	}
	
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public List<Item> getItems() {
		if (items == null) {
			items = new ArrayList<>();
		}
		
		return items;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@Validate({"/field", "/reported/field", "/item/field"})
	public void validateField(Field field) throws ValidationException {
		if (field.getOptions() != null && field.getOptions().size() > 0) {
			if (field.getType() != Field.Type.LIST_MULTI && field.getType() != Field.Type.LIST_SINGLE)
				throw new ValidationException(String.format("options is only allowed in list_multi or list_single type field," +
						" but this is a %s type field", field.getType()));
		}
	}
}
