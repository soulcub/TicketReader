package com.ticketreader.mathcore;

public class Range {
	protected String name = "Unknown";
	protected String range = "";
	
	public Range(String name, String range){
		this.range = range;
		if(!(name == null || name.isEmpty())){
			this.name = name;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}
	
}
