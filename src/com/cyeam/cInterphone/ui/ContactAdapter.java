package com.cyeam.cInterphone.ui;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Contact;
import com.cyeam.cInterphone.util.RoundBitmap;

public class ContactAdapter extends ArrayAdapter<Contact> {

	private int resource;

	public ContactAdapter(Context context, int resource, List<Contact> objects) {
		super(context, resource, objects);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout contactListView = null;

		Contact contact = getItem(position);
		String contactName = contact.getName();

		if (convertView == null) {
			contactListView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(this.resource, contactListView, true);
		} else {
			contactListView = (LinearLayout) convertView;
		}

		TextView nameTextView = (TextView) contactListView
				.findViewById(R.id.contact_name);
		ImageView avatar = (ImageView)contactListView.findViewById(R.id.contact_avatar);
		if (contact.getAvatar() != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getAvatar(), 0, contact.getAvatar().length);
	        avatar.setImageBitmap(RoundBitmap.toRoundCorner(bitmap, Commons.ROUND_DEGREE));
		} else {
			avatar.setImageResource(R.drawable.user_male2);
		}
		nameTextView.setText(contactName);

		return contactListView;
	}

}
