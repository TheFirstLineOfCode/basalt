package com.thefirstlineofcode.basalt.xeps.xdata;

import java.util.ArrayList;
import java.util.List;

import com.thefirstlineofcode.basalt.oxm.coc.annotations.Array;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.BooleanOnly;
import com.thefirstlineofcode.basalt.oxm.coc.annotations.TextOnly;
import com.thefirstlineofcode.basalt.oxm.coc.conversion.annotations.String2Enum;

public class Field {
	public enum Type {
		BOOLEAN,
		FIXED,
		HIDDEN,
		JID_MULTI,
		JID_SINGLE,
		LIST_MULTI,
		LIST_SINGLE,
		TEXT_MULTI,
		TEXT_PRIVATE,
		TEXT_SINGLE
	}
	
	private String desc;
	@BooleanOnly
	private boolean required;
	@Array(value=String.class, elementName="value")
	@TextOnly
	private List<String> values;
	@String2Enum(Field.Type.class)
	private Type type;
	private String label;
	@Array(Option.class)
	private List<Option> options;
	private String var;
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public boolean isRequired() {
		return required;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public List<String> getValues() {
		if (values == null)
			values = new ArrayList<>();
		
		return values;
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public List<Option> getOptions() {
		if (options == null) {
			options = new ArrayList<>();
		}
		
		return options;
	}
	
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	
	public String getVar() {
		return var;
	}
	
	public void setVar(String var) {
		this.var = var;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
