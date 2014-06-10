package com.cyeam.cInterphone.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.TableLayout;

public class DbHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "cInterphone.db";
	public static final int DB_VERSION = 1;
	public static final String PROCESS_TABLE = "process";
	
	public static final String C_ID = BaseColumns._ID;
	public static final String C_CONTENT = "content";
	public static final String C_DURATION = "duration";
	
	Context context;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_process_sql = "create table " + PROCESS_TABLE + "(" + C_ID + " INTEGER primary key AUTOINCREMENT, " + C_CONTENT + " text, " + C_DURATION + " bigint)";
		
		db.execSQL(create_process_sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
