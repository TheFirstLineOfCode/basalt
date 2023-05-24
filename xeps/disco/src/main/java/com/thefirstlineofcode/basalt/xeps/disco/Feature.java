package com.thefirstlineofcode.basalt.xeps.disco;

import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.NotNull;

public class Feature {
	@NotNull
	private String var;
	
	public Feature() {}
	
	public Feature(String var) {
		this.var = var;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	public int hashCode() {
		if (var == null)
			return 0;
		
		int hash = 7;
		hash += 31 * hash + (var == null ? 0 : var.hashCode());
		
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (obj == this)
			return true;
		
		if (obj instanceof Feature) {
			Feature other = (Feature)obj;
			
			if (var == null) {
				if (other.var != null)
					return false;
			} else {
				if (!var.equals(other.var))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
}
