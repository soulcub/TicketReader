package com.ticketreader.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	final String TABLE_NAME = "ticketreader_db";
	
	public SQLiteHelper(Context context) {
		super(context, "", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
//		Log.d(LOG_TAG, "--- onCreate database ---");
		db.execSQL("create table " + TABLE_NAME +" ("
				+ "id integer primary key autoincrement,"
				+ "name text,"
				+ "formula text" 
				+ "rage text" 
				+ ");");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
}
