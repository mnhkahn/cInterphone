package com.cyeam.cInterphone.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Contact;
import com.cyeam.cInterphone.sqlite.DbHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryFragment extends ListFragment {
	private ContactAdapter adapter;
	private DbHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		db = dbHelper.getWritableDatabase();
//
//		ContentValues values = new ContentValues();
//
//		try {
//			values.put(DbHelper.C_ID, 1);
//			values.put(DbHelper.C_USER_ID, 420);
//			values.put(DbHelper.C_DATE, new Date().toString());
//			values.put(DbHelper.C_CALL_TYPE, 0);
//			db.insertOrThrow(DbHelper.HISTORY_TABLE, null, values);
//			
//			values.put(DbHelper.C_ID, 2);
//			values.put(DbHelper.C_USER_ID, 421);
//			values.put(DbHelper.C_DATE, new Date().toString());
//			values.put(DbHelper.C_CALL_TYPE, 0);
//			db.insertOrThrow(DbHelper.HISTORY_TABLE, null, values);
//			
//			values.put(DbHelper.C_ID, 3);
//			values.put(DbHelper.C_USER_ID, 422);
//			values.put(DbHelper.C_DATE, new Date().toString());
//			values.put(DbHelper.C_CALL_TYPE, 0);
//			db.insertOrThrow(DbHelper.HISTORY_TABLE, null, values);
//		} catch (SQLException e) {
//			System.out.println(e);
//		}
//		db.close();

		return inflater.inflate(R.layout.fragment_history, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DbHelper(getActivity());
		adapter = new ContactAdapter(getActivity(), R.layout.contact, getData());
		this.setListAdapter(adapter);
	}

	private List<Contact> getData() {
		List<Contact> contacts = new ArrayList<Contact>();
		
//		db = dbHelper.getReadableDatabase();
////		Cursor historyCursor = db.query(DbHelper.HISTORY_TABLE, new String[]{DbHelper.C_USER_ID, DbHelper.C_DATE, DbHelper.C_CALL_TYPE}, null, null, null, null, null);
//		while (historyCursor.moveToNext()) {
//			Contact contact = Contact.getContact(getActivity().getContentResolver(), historyCursor.getLong(0));
//			contact.setDate(new Date(historyCursor.getString(1)));
//			contacts.add(contact);
//		}
		return contacts;
	}
}
