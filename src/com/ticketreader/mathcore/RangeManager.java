package com.ticketreader.mathcore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ticketreader.OperatorsType;

public class RangeManager {
	protected ArrayList<Range> RangeList = new ArrayList<Range>();
	
	public RangeManager(){
		//TODO Put range generator in this place
		
		
	}

	public ArrayList<Range> getRangeList() {
		return RangeList;
	}	
	
	private Range generate(String formula, String name, int numItems) {
		String rangString = "";
		List<String> polishFormula = null;
		try {
			polishFormula = infToPolishConvertor(formula);
		} catch(Exception e) {
			//TODO show message
		}
		
		for(int i = 0; i < numItems; ++i) {
			rangString += process(polishFormula, i);
		}
		
		if(name == null || name.isEmpty()) {
			name = "Unknown";
		}
		return new Range(name, rangString);
	}
	
	
	private String process(List<String> formula, int argument) {
		LinkedList<BigDecimal> stek = new LinkedList<BigDecimal>();
		
		BigDecimal a, b;
		for(int i = 0; i < formula.size(); ++i) {
			switch(OperatorsType.valueOf(formula.get(i))){
			case VARIABLE:
				stek.push(new BigDecimal(Double.toString(argument)));
				break;
				
			case PLUS:
				b = stek.pop();
				a = stek.pop();
				a.add(b);
				stek.push(a);
				break;
				
			case MINUS:
				b = stek.pop();
				a = stek.pop();
				a.subtract(b);
				stek.push(a);
				break;
				
			case MULTIPLY:
				b = stek.pop();
				a = stek.pop();
				a.multiply(b);
				stek.push(a);
				break;
				
			case DIVISION:
				b = stek.pop();
				a = stek.pop();
				a.divide(b);
				stek.push(a);
				break;				
				
			//By default 
			default:
				stek.push(new BigDecimal(formula.get(i)));
				break;
			}
		}
		return stek.pop().toString();
	}
	
	/** 
	 * 
	 * Work with +, -, *, /, ()
	 * @throws Exception 
	 * 
	 * */
	private List<String> infToPolishConvertor(String formula) throws Exception {
		LinkedList<String> stek = new LinkedList<String>();
		List<String> result = new ArrayList<String>();
		int start = 0, end = 0;
		
		int length = formula.length();
		String str;
		while(end < length) {
			end = getIndexOfOperator(formula, start);
			//if last number ...
			if(end == -1) {
				str = formula.substring(start);
				result.add(str);
				break;
			}
			//read number
			str = formula.substring(start, end);
			result.add(str);
			
			//read operator
			start = end++;
			str = formula.substring(start, end);
			
			switch(OperatorsType.valueOf(str)) {
			case PLUS:
			case MINUS:
				if(stek.isEmpty()){
					stek.push(str);
				}
				else{
					String s = stek.pop();
					while(stek.size() > 0) {
						if(s.equals("+") || s.equals("-") || s.equals("*") ||s.equals("/")) {
							result.add(s);
						}
						else{
							break;
						}
					}
					stek.push(str);
				}
				break;
				
			case MULTIPLY:
			case DIVISION:
				if(stek.isEmpty()) {
					stek.push(str);
				}
				else{
					String s = stek.pop();
					while(stek.size() > 0) {
						if(s.equals("+") || s.equals("-")) {
							result.add(s);
						}
						else{
							break;
						}
					}
					stek.push(str);
				}
				break;
				
			case OPEN_BRACE:
				stek.push(str);
				break;
				
			case CLOSE_BRACE:
				String s = "";
				
				do {
					if(stek.isEmpty()) {
						//TODO Change exception
						throw new Exception();
					}
					
					s = stek.pop();
					if(s.equals("(")) {
						if(!stek.isEmpty()) {
							result.add(stek.pop());
						}
						break;
					}
					else {
						result.add(s);
					}
					
				} while(true);
				break;
				
			default:
				//TODO Change exception
				throw new Exception();								
			}
			
			
		}		
		
		return result;
	}
	
	/** 
	 * 
	 * Available symbols +, -, *, -, ~, ^, !(, ); 
	 * 
	 * {"+", "-", "*", "/", "~", "!", "(", ")"};
	 * */
	private int getIndexOfOperator(String formula, int start){
		int result = -1;
		int[] array = new int [9];
		
		// +
		array[0] = formula.indexOf("+", start);
		// -
		array[1] = formula.indexOf("-", start);
		// *
		array[2] = formula.indexOf("*", start);
		// /
		array[3] = formula.indexOf("/", start);
		// ~
		array[4] = formula.indexOf("~", start);
		// ^
		array[5] = formula.indexOf("^", start);
		// !
		array[6] = formula.indexOf("!", start);
		// (
		array[7] = formula.indexOf("(", start);
		// )
		array[8] = formula.indexOf(")", start);
		
		boolean flag = true;
		for(int i: array) {
			if(i > -1) {
				if(flag) {
					result = i;
					flag = false;
					continue;
				}
				
				if(i < result) {
					result = i;
				}
			}
		}
		
		return result;
	}
}
