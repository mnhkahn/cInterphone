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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.core.CInterphoneEngine;
import com.cyeam.cInterphone.model.Contact;
import com.cyeam.cInterphone.sqlite.DbHelper;

public class FavouriteFragment extends ListFragment {
	private ContactAdapter adapter;
	private DbHelper dbHelper;
	private SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_favourite, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DbHelper(getActivity());
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Contact contact = (Contact) l.getItemAtPosition(position);
		CInterphoneEngine.call_menu(getActivity(), contact.getName());
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// 长按事件
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Contact contact = (Contact) arg0.getItemAtPosition(position);
				db = dbHelper.getWritableDatabase();

				ContentValues values = new ContentValues();

				try {
					db.delete(DbHelper.FAVOURITE_TABLE, DbHelper.C_USER_ID + " = ?", new String[]{contact.getId().toString()});
				} catch (SQLException e) {
					System.out.println(e);
				}
				db.close();
				Toast.makeText(
						getActivity(),
						String.format(
								getResources().getString(
										R.string.toast_del_favourtie_success),
								contact.getName()), Toast.LENGTH_LONG).show();
				// 刷新ListFragment
				adapter = new ContactAdapter(getActivity(), R.layout.contact, getData());
				setListAdapter(adapter);
				return true;
			}
		});

		adapter = new ContactAdapter(getActivity(), R.layout.contact, getData());
		this.setListAdapter(adapter);
		super.onViewCreated(view, savedInstanceState);
	}

	private List<Contact> getData() {
		List<Contact> contacts = new ArrayList<Contact>();

		db = dbHelper.getReadableDatabase();
		Cursor favouriteCursor = db.query(DbHelper.FAVOURITE_TABLE,
				new String[] { DbHelper.C_USER_ID }, null, null, null, null,
				null);
		while (favouriteCursor.moveToNext()) {
			Contact contact = Contact.getContact(getActivity()
					.getContentResolver(), favouriteCursor.getLong(0));
			contacts.add(contact);
		}
		return contacts;
	}
}
