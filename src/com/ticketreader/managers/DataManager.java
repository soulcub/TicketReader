package com.ticketreader.managers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ticketreader.data.RangeInfo;
import com.ticketreader.sqlite.SQLiteHelper;
import com.ticketreader.sqlite.TableInfo;

public class DataManager {
	private static DataManager dataManager = null;
	
	protected SQLiteHelper dbHelper;
	protected List<RangeInfo> ranges;
	
	private DataManager(){
		
	}
	
	public static DataManager instance() {
		if(dataManager != null) {
			dataManager = new DataManager();
		}
		return dataManager;
	}
	
	public void init(Context context) {
		this.dbHelper = new SQLiteHelper(context);
		
		this.ranges = readRanges();  
	}
	
	public List<RangeInfo> readRanges(){
		List<RangeInfo> ranges = new ArrayList<RangeInfo>();
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(TableInfo.TABLE_NAME, null, null, null, null, null, null);
		
		if(cursor.moveToFirst()) {
			RangeInfo rangeInfo;
			
			int idColIndex = cursor.getColumnIndex("id");
			int nameColIndex = cursor.getColumnIndex("name");
			int formulaColIndex = cursor.getColumnIndex("formula");
			int rangeColIndex = cursor.getColumnIndex("range");
			int rangeItemNumColIndex = cursor.getColumnIndex("range_items_num");
			int enableColSize = cursor.getColumnIndex("enable");
			
			do {
				rangeInfo = new RangeInfo();
				
				rangeInfo.setId(cursor.getInt(idColIndex));
				rangeInfo.setName(cursor.getString(nameColIndex));
				rangeInfo.setFormula(cursor.getString(formulaColIndex));
				rangeInfo.setRange(cursor.getString(rangeColIndex));
				rangeInfo.setRangeItemNum(cursor.getInt(rangeItemNumColIndex));
				rangeInfo.setEnable(cursor.getInt(enableColSize) != 0);
				
				ranges.add(rangeInfo);
				
			} while(cursor.moveToNext());
			
		} else {
			cursor.close();
		}
		
		dbHelper.close();
		
		return ranges;
	}
}
