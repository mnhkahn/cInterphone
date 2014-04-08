package com.cyeam.cInterphone.ui;

import java.util.ArrayList;
import java.util.List;

import org.sipdroid.sipua.ui.Receiver;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Contact;

public class ContactFragment extends ListFragment {
	private ContactAdapter adapter;
	private static AlertDialog m_AlertDlg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ContactAdapter(getActivity(), R.layout.contact, getData());
		this.setListAdapter(adapter);
	}

	private List<Contact> getData() {
		ContentResolver resolver = getActivity().getContentResolver();

		List<Contact> contacts = new ArrayList<Contact>();
		// 获取手机联系人
		String[] columns = new String[] { ContactsContract.Contacts._ID,
				PhoneLookup.DISPLAY_NAME };
		// 查询联系人ID和联系人名称两列
		Uri URI = ContactsContract.Contacts.CONTENT_URI;
		Cursor contactCursor = resolver.query(URI, columns,
				PhoneLookup.HAS_PHONE_NUMBER + "=1", null, null);
		// 限定只返回有号码的联系人
		while (contactCursor.moveToNext()) {
			Cursor phoneCursor = resolver
					.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ "=" + contactCursor.getLong(0), null,
							null);
			// 因为号码与联系人不存在一个表中,一个联系人可能存在多个号码,所以根据联系人ID查找号码,存在phoneNum中
			Contact contact = new Contact();
			contact.setName(contactCursor.getString(1));
			if (phoneCursor.moveToNext()) {
				if (contact.getPhones() == null) {
					List<String> lists = new ArrayList<String>();
					contact.setPhones(lists);
				}
				contact.getPhones().add(phoneCursor.getString(0));
			}
			contacts.add(contact);
			phoneCursor.close();
		}
		contactCursor.close();

		return contacts;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Contact contact = (Contact) l.getItemAtPosition(position);
		call_menu("mint");
	}

	// 呼叫请求
	void call_menu(String target) {
		if (m_AlertDlg != null) {
			m_AlertDlg.cancel();
		}
		if (target.length() == 0)
			m_AlertDlg = new AlertDialog.Builder(getActivity())
					.setMessage(R.string.empty).setTitle(R.string.app_name)
					.setIcon(R.drawable.icon22).setCancelable(true).show();
		else if (!Receiver.engine(getActivity()).call(target, true))
			m_AlertDlg = new AlertDialog.Builder(getActivity())
					.setMessage(R.string.notfast).setTitle(R.string.app_name)
					.setIcon(R.drawable.icon22).setCancelable(true).show();
	}
}
