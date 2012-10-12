package com.ticketreader.data;

public class RangeInfo {
	protected int id;
	protected String name;
	protected String formula;
	protected String range;
	protected int rangeItemNum;
	protected boolean enable;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public int getRangeItemNum() {
		return rangeItemNum;
	}
	public void setRangeItemNum(int rangeItemNum) {
		this.rangeItemNum = rangeItemNum;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
}
