package com.thefirstlineofcode.basalt.xeps.muc.user;

import com.thefirstlineofcode.basalt.oxm.coc.validation.annotations.IntRange;

public class Status {
	@IntRange(min=100, max=999)
	private int code;
	
	public Status() {}
	
	public Status(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Status) {
			return code == ((Status)obj).code;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return code;
	}
}
