package com.thefirstlineofcode.basalt.xmpp.core;

public abstract class AbstractError implements IError {
	protected String definedCondition;
	protected LangText text;
	protected Object applicationSpecificCondition;
	
	public AbstractError() {
	}
	
	public AbstractError(String definedCondition) {
		this(definedCondition, null);
	}
	
	public AbstractError(String definedCondition, LangText text) {
		this.definedCondition = definedCondition;
		this.text = text;
	}
	
	@Override
	public String getDefinedCondition() {
		return definedCondition;
	}
	@Override
	public void setDefinedCondition(String definedCondition) {
		this.definedCondition = definedCondition;
	}

	@Override
	public LangText getText() {
		return text;
	}
	
	@Override
	public void setText(LangText text) {
		this.text = text;
	}

	@Override
	public Object getApplicationSpecificCondition() {
		return applicationSpecificCondition;
	}
	
	@Override
	public void setApplicationSpecificCondition(Object applicationSpecificCondition) {
		this.applicationSpecificCondition = applicationSpecificCondition;
	}
}
