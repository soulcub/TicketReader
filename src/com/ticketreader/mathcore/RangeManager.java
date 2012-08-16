package com.ticketreader.mathcore;

import java.math.BigInteger;
import java.util.ArrayList;

public class RangeManager {
	protected ArrayList<Range> RangeList = new ArrayList<Range>();
	
	public RangeManager(){
		//TODO Put range generator in this place
		String str = "";
		BigInteger num = new BigInteger("1");
		for(int i=0; i<100; ++i){
			num = num.shiftLeft(1);
			str += num.toString();
		}
		
		RangeList.add(new Range("The range powers of two",str));
	}

	public ArrayList<Range> getRangeList() {
		return RangeList;
	}	
}
