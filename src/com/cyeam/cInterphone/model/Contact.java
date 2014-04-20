package com.cyeam.cInterphone.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class Contact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8663290597835403021L;
	private Long id;
	private String name;
	private byte[] avatar;
	// sqlite 不支持date类型，使用string代替
	private Date date;
	private int type = -1;
	private int isFavourite = 0;

	private List<String> phones;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(int isFavourite) {
		this.isFavourite = isFavourite;
	}

	public static Contact getContact(ContentResolver resolver, Long id) {
		Contact contact = new Contact();
		contact.setId(id);

		Cursor contactCursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] {PhoneLookup.DISPLAY_NAME },
				ContactsContract.Contacts._ID + "=" + id, null, null);
		contactCursor.moveToNext();
		contact.setName(contactCursor.getString(0));
		
		Cursor phoneCursor = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
				null, null);
		if (phoneCursor.moveToNext()) {
			if (contact.getPhones() == null) {
				List<String> lists = new ArrayList<String>();
				contact.setPhones(lists);
			}
			contact.getPhones().add(phoneCursor.getString(0));
		}

		Cursor avatarCursor = resolver.query(
				ContactsContract.Contacts.CONTENT_URI,
				null,
				ContactsContract.Contacts._ID + " = "
						+ id, null, null);
		if (avatarCursor.getCount() > 0) {
			avatarCursor.moveToFirst();
			String photo_id = avatarCursor.getString(avatarCursor
					.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
			contact.setAvatar(getPhoto(resolver, photo_id));
		}
		return contact;
	}
	
	public static byte[] getPhoto(ContentResolver resolver, String photo_id) {
		String selection = null;
		if (photo_id == null) {
			return null;
		} else {
			selection = ContactsContract.Data._ID + " = " + photo_id;
		}

		String[] projection = new String[] { ContactsContract.Data.DATA15 };
		Cursor cur = resolver.query(
				ContactsContract.Data.CONTENT_URI, projection, selection, null,
				null);
		cur.moveToFirst();
		byte[] contactIcon = cur.getBlob(0);
		if (contactIcon == null) {
			return null;
		} else {
			return contactIcon;
		}
	}
}
