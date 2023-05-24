package com.thefirstlineofcode.basalt.xeps.disco;

import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.NotNull;

public class Identity {
	@NotNull
	private String category;
	private String name;
	@NotNull
	private String type;
	
	public Identity() {}
	
	public Identity(String category, String type) {
		this.category = category;
		this.type = type;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		
		if (category != null)
			hash += 31 * hash + category.hashCode();
		
		if (name != null)
			hash += 31 * hash + name.hashCode();
		
		if (type != null)
			hash += 31 * hash + type.hashCode();
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (obj instanceof Identity) {
			Identity other = (Identity)obj;
			
			if (category == null) {
				if (other.category != null)
					return false;
			} else {
				if (!category.equals(other.category))
					return false;
			}
			
			if (name == null) {
				if (other.name != null)
					return false;
			} else {
				if (!name.equals(other.name))
					return false;
			}
			
			if (type == null) {
				if (other.type != null)
					return false;
			} else {
				if (!type.equals(other.type))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
}
