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
	public static final String FAVOURITE_TABLE = "favourite";
	public static final String HISTORY_TABLE = "history";
	
	public static final String C_ID = BaseColumns._ID;
	public static final String C_USER_ID = "user_id";
	public static final String C_DATE = "date";
	public static final String C_CALL_TYPE = "type";
	
	Context context;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_favourite_sql = "create table " + FAVOURITE_TABLE + " if not exists (" + C_ID + " int primary key AUTO_INCREMENT, " + C_USER_ID + " int)";
		String create_history_sql = "create table " + HISTORY_TABLE + " if not exists (" + C_ID + " int primary key AUTO_INCREMENT, " + C_USER_ID + " int, " + C_DATE + " int, " + C_CALL_TYPE + " int)";
		
		db.execSQL(create_favourite_sql);
		db.execSQL(create_history_sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
