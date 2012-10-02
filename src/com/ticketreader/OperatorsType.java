package com.ticketreader;

public enum OperatorsType {
	PLUS("+"),
	MINUS("-"),
	MULTIPLY("*"),
	DIVISION("/"),
	OPEN_BRACE("("),
	CLOSE_BRACE(")"),
	VARIABLE("x");
	
	private String str;
	
	OperatorsType(String str){
		this.str = str;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}
	
	

}
