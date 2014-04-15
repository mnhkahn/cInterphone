package com.cyeam.cInterphone.ui;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.ListFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Contact;
import com.cyeam.cInterphone.sqlite.DbHelper;

public class FavouriteFragment extends ListFragment {
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
//			db.insertOrThrow(DbHelper.FAVOURITE_TABLE, null, values);
//			
//			values.put(DbHelper.C_ID, 2);
//			values.put(DbHelper.C_USER_ID, 421);
//			db.insertOrThrow(DbHelper.FAVOURITE_TABLE, null, values);
//			
//			values.put(DbHelper.C_ID, 3);
//			values.put(DbHelper.C_USER_ID, 422);
//			db.insertOrThrow(DbHelper.FAVOURITE_TABLE, null, values);
//		} catch (SQLException e) {
//			System.out.println(e);
//		}
//		db.close();
		

		return inflater.inflate(R.layout.fragment_favourite, container, false);
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
		
		db = dbHelper.getReadableDatabase();
		Cursor favouriteCursor = db.query(DbHelper.FAVOURITE_TABLE, new String[]{DbHelper.C_USER_ID}, null, null, null, null, null);
		while (favouriteCursor.moveToNext()) {
			Contact contact = Contact.getContact(getActivity().getContentResolver(), favouriteCursor.getLong(0));
			contacts.add(contact);
		}
		return contacts;
	}
}
