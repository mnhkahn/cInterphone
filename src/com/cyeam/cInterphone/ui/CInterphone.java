/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * Copyright (C) 2008 Hughes Systique Corporation, USA (http://www.hsc.com)
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.cyeam.cInterphone.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.sipdroid.sipua.R;
import org.sipdroid.sipua.ui.Receiver;
import org.sipdroid.sipua.ui.RegisterService;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog.Calls;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;


/////////////////////////////////////////////////////////////////////
// this the main activity of cInterphone
// for modifying it additional terms according to section 7, GPL apply
// see ADDITIONAL_TERMS.txt
/////////////////////////////////////////////////////////////////////

// 如果想在关闭dialog的时候执行某些程序,那你应该考虑使用DialogInterface.OnDismissListener
public class CInterphone extends FragmentActivity implements OnDismissListener {

	public static final boolean release = true;
	public static final boolean market = false;

	/* Following the menu item constants which will be used for menu creation */
	public static final int FIRST_MENU_ID = Menu.FIRST;
	public static final int CONFIGURE_MENU_ITEM = FIRST_MENU_ID + 1;
	public static final int ABOUT_MENU_ITEM = FIRST_MENU_ID + 2;
	public static final int EXIT_MENU_ITEM = FIRST_MENU_ID + 3;

	private static AlertDialog m_AlertDlg;
	AutoCompleteTextView sip_uri_box, sip_uri_box2;
	Button createButton;

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	// 从starting到running状态 onCreate onStart onResume
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Set default fragment.
		mViewPager.setCurrentItem(2);
		
		// 初始化SIP&注册
		Account[] accounts = AccountManager.get(this).getAccountsByType(
				"com.google");
		if (accounts.length > 0) {
			Settings.DEFAULT_USERNAME = accounts[0].name;
		}
		Receiver.engine(this).StartEngine();
	}

	@Override
	public void onStart() {
		super.onStart();
		Receiver.engine(this).registerMore();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public static class CallsCursor extends CursorWrapper {
		List<String> list;

		public int getCount() {
			return list.size();
		}

		public String getString(int i) {
			return list.get(getPosition());
		}

		public CallsCursor(Cursor cursor) {
			super(cursor);
			list = new ArrayList<String>();
			for (int i = 0; i < cursor.getCount(); i++) {
				moveToPosition(i);
				String phoneNumber = super.getString(1);
				String cachedName = super.getString(2);
				if (cachedName != null && cachedName.trim().length() > 0)
					phoneNumber += " <" + cachedName + ">";
				if (list.contains(phoneNumber))
					continue;
				list.add(phoneNumber);
			}
			moveToFirst();
		}

	}

	public static class CallsAdapter extends CursorAdapter implements
			Filterable {
		public CallsAdapter(Context context, Cursor c) {
			super(context, c);
			mContent = context.getContentResolver();
		}

		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final TextView view = (TextView) inflater.inflate(
					android.R.layout.simple_dropdown_item_1line, parent, false);
			String phoneNumber = cursor.getString(1);
			view.setText(phoneNumber);
			return view;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			String phoneNumber = cursor.getString(1);
			((TextView) view).setText(phoneNumber);
		}

		@Override
		public String convertToString(Cursor cursor) {
			String phoneNumber = cursor.getString(1);
			if (phoneNumber.contains(" <"))
				phoneNumber = phoneNumber.substring(0,
						phoneNumber.indexOf(" <"));
			return phoneNumber;
		}

		@Override
		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
			if (getFilterQueryProvider() != null) {
				return new CallsCursor(getFilterQueryProvider().runQuery(
						constraint));
			}

			StringBuilder buffer;
			String[] args;
			buffer = new StringBuilder();
			buffer.append(Calls.NUMBER);
			buffer.append(" LIKE ? OR ");
			buffer.append(Calls.CACHED_NAME);
			buffer.append(" LIKE ?");
			String arg = "%"
					+ (constraint != null && constraint.length() > 0 ? constraint
							.toString() : "@") + "%";
			args = new String[] { arg, arg };

			return new CallsCursor(mContent.query(Calls.CONTENT_URI,
					PROJECTION, buffer.toString(), args, Calls.NUMBER + " asc"));
		}

		private ContentResolver mContent;
	}

	private static final String[] PROJECTION = new String[] { Calls._ID,
			Calls.NUMBER, Calls.CACHED_NAME };

	public static boolean on(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(Settings.PREF_ON, Settings.DEFAULT_ON);
	}

	public static void on(Context context, boolean on) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(context)
				.edit();
		edit.putBoolean(Settings.PREF_ON, on);
		edit.commit();
		if (on)
			Receiver.engine(context).isRegistered();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);

		MenuItem m;
		m = menu.add(0, CONFIGURE_MENU_ITEM, 0, R.string.menu_settings);
		m.setIcon(android.R.drawable.ic_menu_preferences);
		m.setShowAsAction(m.SHOW_AS_ACTION_IF_ROOM);
		m = menu.add(0, ABOUT_MENU_ITEM, 0, R.string.menu_about);
		m.setIcon(android.R.drawable.ic_menu_info_details);
		m.setShowAsAction(m.SHOW_AS_ACTION_IF_ROOM);
		m = menu.add(0, EXIT_MENU_ITEM, 0, R.string.menu_exit);
		m.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		m.setShowAsAction(m.SHOW_AS_ACTION_IF_ROOM);
		
		return result;
	}

	// 当客户点击菜单当中的某一个选项时，会调用该方法
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = super.onOptionsItemSelected(item);
		Intent intent = null;

		switch (item.getItemId()) {
		case ABOUT_MENU_ITEM:
			if (m_AlertDlg != null) {
				m_AlertDlg.cancel();
			}
			m_AlertDlg = new AlertDialog.Builder(this)
					.setMessage(
							getString(R.string.about).replace("\\n", "\n")
									.replace("${VERSION}", getVersion(this)))
					.setTitle(getString(R.string.menu_about))
					.setIcon(android.R.drawable.ic_menu_info_details).setCancelable(true).show();
			break;

		case EXIT_MENU_ITEM:
			on(this, false);
			Receiver.pos(true);
			Receiver.engine(this).halt();
			Receiver.mSipdroidEngine = null;
			Receiver.reRegister(0);
			stopService(new Intent(this, RegisterService.class));
			finish();
			break;

		case CONFIGURE_MENU_ITEM: {
			try {
//				intent = new Intent(this, com.cyeam.cInterphone.ui.Settings.class);
				intent = new Intent(this, org.sipdroid.sipua.ui.Settings.class);
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
			}
		}
			break;
		}

		return result;
	}

	public static String getVersion() {
		return getVersion(Receiver.mContext);
	}

	public static String getVersion(Context context) {
		final String unknown = "Unknown";

		if (context == null) {
			return unknown;
		}

		try {
			String ret = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			if (ret.contains(" + "))
				ret = ret.substring(0, ret.indexOf(" + ")) + "b";
			return ret;
		} catch (NameNotFoundException ex) {
		}

		return unknown;
	}

	// Dialog关闭后执行的代码
	@Override
	public void onDismiss(DialogInterface dialog) {
		onResume();
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			// Default fragment.
			Fragment fragment = new DummySectionFragment();

			switch (position) {
			case 0:
				fragment = new FavouriteFragment();
				break;
			case 1:
				fragment = new HistoryFragment();
				break;
			case 2:
				fragment = new ContactFragment();
				break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.favourite).toUpperCase(l);
			case 1:
				return getString(R.string.history).toUpperCase(l);
			case 2:
				return getString(R.string.contact).toUpperCase(l);
			}
			return null;
		}
	}
	
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
}
