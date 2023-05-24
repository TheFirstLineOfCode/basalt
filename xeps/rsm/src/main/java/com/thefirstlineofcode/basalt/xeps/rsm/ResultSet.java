package com.thefirstlineofcode.basalt.xeps.rsm;

public class ResultSet<T> {
	private int count;
	private T[] rows;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public T[] getRows() {
		return rows;
	}

	public void setRows(T[] rows) {
		this.rows = rows;
	}
	
}
