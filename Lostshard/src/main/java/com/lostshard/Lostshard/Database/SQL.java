package com.lostshard.Lostshard.Database;

public enum SQL {

	SELECT("SELECT");
	
	private String sql;
	
	private SQL(String sql) {
		this.sql = sql;
	}
	
	@Override
	public String toString() {
		return this.sql;
	}
	
}
