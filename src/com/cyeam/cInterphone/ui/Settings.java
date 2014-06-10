/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.sipdroid.codecs.Codecs;
import org.sipdroid.sipua.ui.InstantAutoCompleteTextView;
import org.sipdroid.sipua.ui.Receiver;
import org.zoolu.sip.provider.SipStack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.core.SipdroidEngine;

public class Settings extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, OnClickListener {
	// Current settings handler
	private static SharedPreferences settings;
	// Context definition
	private Settings context = null;

	// Path where to store all profiles - !!!should be replaced by some system
	// variable!!!
	private final static String profilePath = "/sdcard/Sipdroid/";
	// Path where is stored the shared preference file - !!!should be replaced
	// by some system variable!!!
	private final String sharedPrefsPath = "/data/data/org.sipdroid.sipua/shared_prefs/";
	// Shared preference file name - !!!should be replaced by some system
	// variable!!!
	private final String sharedPrefsFile = "org.sipdroid.sipua_preferences";
	// List of profile files available on the SD card
	private String[] profileFiles = null;
	// Which profile file to delete
	private int profileToDelete;

	// All possible values of the PREF_PREF preference (see bellow)
	public static final String VAL_PREF_PSTN = "PSTN";
	public static final String VAL_PREF_SIP = "SIP";
	public static final String VAL_PREF_SIPONLY = "SIPONLY";
	public static final String VAL_PREF_ASK = "ASK";

	/*-
	 * ****************************************
	 * **** HOW TO USE SHARED PREFERENCES *****
	 * ****************************************
	 * 
	 * If you need to check the existence of the preference key
	 *   in this class:		contains(PREF_USERNAME)
	 *   in other classes:	PreferenceManager.getDefaultSharedPreferences(Receiver.mContext).contains(Settings.PREF_USERNAME) 
	 * If you need to check the existence of the key or check the value of the preference
	 *   in this class:		getString(PREF_USERNAME, "").equals("")
	 *   in other classes:	PreferenceManager.getDefaultSharedPreferences(Receiver.mContext).getString(Settings.PREF_USERNAME, "").equals("")
	 * If you need to get the value of the preference
	 *   in this class:		getString(PREF_USERNAME, DEFAULT_USERNAME)
	 *   in other classes:	PreferenceManager.getDefaultSharedPreferences(Receiver.mContext).getString(Settings.PREF_USERNAME, Settings.DEFAULT_USERNAME)
	 */

	// Name of the keys in the Preferences XML file
	public static final String PREF_ROLE = "role";
	public static final String PREF_USERNAME = "username";
	public static final String PREF_SERVER = "server";
	public static final String PREF_PORT = "port";
	public static final String PREF_VQUALITY = "vquality";

	// Default values of the preferences
	public static final String DEFAULT_ROLE = "默认";
	public static final String DEFAULT_USERNAME = "";
	public static final String DEFAULT_SERVER = "192.168.1.102";
	public static final String DEFAULT_PORT = "" + SipStack.default_port;
	public static final String DEFAULT_VQUALITY = "low";

	public static final String PREF_PASSWORD = "password";
	public static final String PREF_DOMAIN = "domain";
	public static final String PREF_FROMUSER = "fromuser";
	public static final String PREF_PROTOCOL = "protocol";
	public static final String PREF_WLAN = "wlan";
	public static final String PREF_3G = "3g";
	public static final String PREF_EDGE = "edge";
	public static final String PREF_VPN = "vpn";
	public static final String PREF_PREF = "pref";
	public static final String PREF_AUTO_ON = "auto_on";
	public static final String PREF_AUTO_ONDEMAND = "auto_on_demand";
	public static final String PREF_AUTO_HEADSET = "auto_headset";
	public static final String PREF_MWI_ENABLED = "MWI_enabled";
	public static final String PREF_REGISTRATION = "registration";
	public static final String PREF_NOTIFY = "notify";
	public static final String PREF_NODATA = "nodata";
	public static final String PREF_SIPRINGTONE = "sipringtone";
	public static final String PREF_SEARCH = "search";
	public static final String PREF_EXCLUDEPAT = "excludepat";
	public static final String PREF_EARGAIN = "eargain";
	public static final String PREF_MICGAIN = "micgain";
	public static final String PREF_HEARGAIN = "heargain";
	public static final String PREF_HMICGAIN = "hmicgain";
	public static final String PREF_OWNWIFI = "ownwifi";
	public static final String PREF_STUN = "stun";
	public static final String PREF_STUN_SERVER = "stun_server";
	public static final String PREF_STUN_SERVER_PORT = "stun_server_port";

	// MMTel configurations (added by mandrajg)
	public static final String PREF_MMTEL = "mmtel";
	public static final String PREF_MMTEL_QVALUE = "mmtel_qvalue";

	// Call recording preferences.
	public static final String PREF_CALLRECORD = "callrecord";

	public static final String PREF_PAR = "par";
	public static final String PREF_IMPROVE = "improve";
	public static final String PREF_POSURL = "posurl";
	public static final String PREF_POS = "pos";
	public static final String PREF_CALLBACK = "callback";
	public static final String PREF_CALLTHRU = "callthru";
	public static final String PREF_CALLTHRU2 = "callthru2";
	public static final String PREF_CODECS = "codecs_new";
	public static final String PREF_DNS = "dns";
	public static final String PREF_MESSAGE = "vmessage";
	public static final String PREF_BLUETOOTH = "bluetooth";
	public static final String PREF_KEEPON = "keepon";
	public static final String PREF_SELECTWIFI = "selectwifi";
	public static final String PREF_ACCOUNT = "account";

	public static final String DEFAULT_PASSWORD = "";
	public static final String DEFAULT_DOMAIN = "";
	public static final String DEFAULT_FROMUSER = "";
	public static final String DEFAULT_PROTOCOL = "tcp";
	public static final boolean DEFAULT_WLAN = true;
	public static final boolean DEFAULT_3G = false;
	public static final boolean DEFAULT_EDGE = false;
	public static final boolean DEFAULT_VPN = false;
	public static final String DEFAULT_PREF = VAL_PREF_SIP;
	public static final boolean DEFAULT_AUTO_ON = false;
	public static final boolean DEFAULT_AUTO_ONDEMAND = false;
	public static final boolean DEFAULT_AUTO_HEADSET = false;
	public static final boolean DEFAULT_MWI_ENABLED = true;
	public static final boolean DEFAULT_REGISTRATION = true;
	public static final boolean DEFAULT_NOTIFY = false;
	public static final boolean DEFAULT_NODATA = false;
	public static final String DEFAULT_SIPRINGTONE = "";
	public static final String DEFAULT_SEARCH = "";
	public static final String DEFAULT_EXCLUDEPAT = "";
	public static final float DEFAULT_EARGAIN = (float) 0.25;
	public static final float DEFAULT_MICGAIN = (float) 0.25;
	public static final float DEFAULT_HEARGAIN = (float) 0.25;
	public static final float DEFAULT_HMICGAIN = (float) 1.0;
	public static final boolean DEFAULT_OWNWIFI = false;
	public static final boolean DEFAULT_STUN = false;
	public static final String DEFAULT_STUN_SERVER = "stun.ekiga.net";
	public static final String DEFAULT_STUN_SERVER_PORT = "3478";

	// MMTel configuration (added by mandrajg)
	public static final boolean DEFAULT_MMTEL = false;
	public static final String DEFAULT_MMTEL_QVALUE = "1.00";

	// Call recording preferences.
	public static final boolean DEFAULT_CALLRECORD = false;

	public static final boolean DEFAULT_PAR = false;
	public static final boolean DEFAULT_IMPROVE = false;
	public static final String DEFAULT_POSURL = "";
	public static final boolean DEFAULT_POS = false;
	public static final boolean DEFAULT_CALLBACK = false;
	public static final boolean DEFAULT_CALLTHRU = false;
	public static final String DEFAULT_CALLTHRU2 = "";
	public static final String DEFAULT_CODECS = null;
	public static final String DEFAULT_DNS = "";
	public static final boolean DEFAULT_MESSAGE = false;
	public static final boolean DEFAULT_BLUETOOTH = false;
	public static final boolean DEFAULT_KEEPON = false;
	public static final boolean DEFAULT_SELECTWIFI = false;
	public static final int DEFAULT_ACCOUNT = 0;

	// An other preference keys (not in the Preferences XML file)
	public static final String PREF_OLDVALID = "oldvalid";
	public static final String PREF_SETMODE = "setmode";
	public static final String PREF_OLDVIBRATE = "oldvibrate";
	public static final String PREF_OLDVIBRATE2 = "oldvibrate2";
	public static final String PREF_OLDPOLICY = "oldpolicy";
	public static final String PREF_OLDRING = "oldring";
	public static final String PREF_AUTO_DEMAND = "auto_demand";
	public static final String PREF_WIFI_DISABLED = "wifi_disabled";
	public static final String PREF_ON_VPN = "on_vpn";
	public static final String PREF_NODEFAULT = "nodefault";
	public static final String PREF_NOPORT = "noport";
	public static final String PREF_ON = "on";
	public static final String PREF_PREFIX = "prefix";
	public static final String PREF_COMPRESSION = "compression";
	// public static final String PREF_RINGMODEx = "ringmodeX";
	// public static final String PREF_VOLUMEx = "volumeX";

	// Default values of the other preferences
	public static final boolean DEFAULT_OLDVALID = false;
	public static final boolean DEFAULT_SETMODE = false;
	public static final int DEFAULT_OLDVIBRATE = 0;
	public static final int DEFAULT_OLDVIBRATE2 = 0;
	public static final int DEFAULT_OLDPOLICY = 0;
	public static final int DEFAULT_OLDRING = 0;
	public static final boolean DEFAULT_AUTO_DEMAND = false;
	public static final boolean DEFAULT_WIFI_DISABLED = false;
	public static final boolean DEFAULT_ON_VPN = false;
	public static final boolean DEFAULT_NODEFAULT = false;
	public static final boolean DEFAULT_NOPORT = false;
	public static final boolean DEFAULT_ON = false;
	public static final String DEFAULT_PREFIX = "";
	public static final String DEFAULT_COMPRESSION = null;

	// public static final String DEFAULT_RINGTONEx = "";
	// public static final String DEFAULT_VOLUMEx = "";

	public static float getEarGain() {
		try {
			return Float.valueOf(PreferenceManager.getDefaultSharedPreferences(
					Receiver.mContext).getString(
					Receiver.headset > 0 ? PREF_HEARGAIN : PREF_EARGAIN,
					"" + DEFAULT_EARGAIN));
		} catch (NumberFormatException i) {
			return DEFAULT_EARGAIN;
		}
	}

	public static float getMicGain() {
		if (Receiver.headset > 0 || Receiver.bluetooth > 0) {
			try {
				return Float.valueOf(PreferenceManager
						.getDefaultSharedPreferences(Receiver.mContext)
						.getString(PREF_HMICGAIN, "" + DEFAULT_HMICGAIN));
			} catch (NumberFormatException i) {
				return DEFAULT_HMICGAIN;
			}
		}

		try {
			return Float.valueOf(PreferenceManager.getDefaultSharedPreferences(
					Receiver.mContext).getString(PREF_MICGAIN,
					"" + DEFAULT_MICGAIN));
		} catch (NumberFormatException i) {
			return DEFAULT_MICGAIN;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Receiver.mContext == null)
			Receiver.mContext = this;
		addPreferencesFromResource(R.xml.preferences_cinterphone);
		setDefaultValues();
	}

	@SuppressWarnings("deprecation")
	void reload() {
		setPreferenceScreen(null);
		addPreferencesFromResource(R.xml.preferences_cinterphone);
	}

	@SuppressWarnings("deprecation")
	private void setDefaultValues() {
		// settings = getSharedPreferences(sharedPrefsFile, MODE_PRIVATE);
		settings = PreferenceManager.getDefaultSharedPreferences(this);

		for (int i = 0; i < SipdroidEngine.LINES; i++) {
			String j = (i != 0 ? "" + i : "");
			if (settings.getString(PREF_SERVER + j, "").equals("")) {
				// CheckBoxPreference cb = (CheckBoxPreference)
				// getPreferenceScreen().findPreference(PREF_WLAN+j);
				// cb.setChecked(true);
				Editor edit = settings.edit();

				edit.putString(PREF_PORT + j, "5061");
				edit.putString(PREF_SERVER + j, DEFAULT_SERVER);
				edit.putString(PREF_PREF + j, DEFAULT_PREF);
				// edit.putString(PREF_PROTOCOL+j, DEFAULT_PROTOCOL);
				edit.commit();
				Receiver.engine(this).updateDNS();
				reload();
			}
		}
		settings.registerOnSharedPreferenceChangeListener(this);

		updateSummaries();
		Codecs.check();
	}

	public static String[] getProfileList() {
		File dir = new File(profilePath);
		return dir.list();
	}

	private String getProfileNameString() {
		return getProfileNameString(settings);
	}

	public static String getProfileNameString(SharedPreferences s) {
		String provider = s.getString(PREF_SERVER, DEFAULT_SERVER);

		if (!s.getString(PREF_DOMAIN, "").equals("")) {
			provider = s.getString(PREF_DOMAIN, DEFAULT_DOMAIN);
		}

		return s.getString(PREF_USERNAME, DEFAULT_USERNAME) + "@" + provider;
	}

	private void exportSettings() {
		if (!settings.getString(PREF_USERNAME, "").equals("")
				&& !settings.getString(PREF_SERVER, "").equals(""))
			try {
				// Create the directory for the profiles
				new File(profilePath).mkdirs();

				// Copy shared preference file on the SD card
				copyFile(new File(sharedPrefsPath + sharedPrefsFile + ".xml"),
						new File(profilePath + getProfileNameString()));
			} catch (Exception e) {
				Toast.makeText(this,
						getString(R.string.settings_profile_export_error),
						Toast.LENGTH_SHORT).show();
			}
	}

	private OnClickListener profileOnClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichItem) {
			boolean message = settings
					.getBoolean(PREF_MESSAGE, DEFAULT_MESSAGE);

			try {
				copyFile(new File(profilePath + profileFiles[whichItem]),
						new File(sharedPrefsPath + sharedPrefsFile + ".xml"));
			} catch (Exception e) {
				Toast.makeText(context,
						getString(R.string.settings_profile_import_error),
						Toast.LENGTH_SHORT).show();
				return;
			}

			settings.unregisterOnSharedPreferenceChangeListener(context);
			setDefaultValues();

			// Restart the engine
			Receiver.engine(context).halt();
			Receiver.engine(context).StartEngine();

			reload();
			settings.registerOnSharedPreferenceChangeListener(context);
			updateSummaries();
			if (message) {
				Editor edit = settings.edit();
				edit.putBoolean(PREF_MESSAGE, true);
				edit.commit();
			}
		}
	};

	private OnClickListener deleteOkButtonClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			File profile = new File(profilePath + profileFiles[profileToDelete]);
			boolean rv = false;
			// Check if the file exists and try to delete it
			if (profile.exists()) {
				rv = profile.delete();
			}
			if (rv) {
				Toast.makeText(
						context,
						getString(R.string.settings_profile_delete_confirmation),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context,
						getString(R.string.settings_profile_delete_error),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	public void copyFile(File in, File out) throws Exception {
		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		try {
			byte[] buf = new byte[1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (fis != null)
				fis.close();
			if (fos != null)
				fos.close();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		settings.unregisterOnSharedPreferenceChangeListener(this);
	}

	EditText transferText;
	String mKey;

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (!Thread.currentThread().getName().equals("main"))
			return;
		if (key.startsWith(PREF_PORT)
				&& sharedPreferences.getString(key, DEFAULT_PORT).equals("0")) {
			Editor edit = sharedPreferences.edit();
			edit.putString(key, DEFAULT_PORT);
			edit.commit();

			transferText = new InstantAutoCompleteTextView(this, null);
			transferText.setInputType(InputType.TYPE_CLASS_NUMBER);
			mKey = key;

			new AlertDialog.Builder(this)
					.setTitle(
							Receiver.mContext.getString(R.string.settings_port))
					.setView(transferText)
					.setPositiveButton(android.R.string.ok, this).show();
			return;
		} else if (key.startsWith(PREF_PASSWORD) || key.startsWith(PREF_SERVER)
				|| key.startsWith(PREF_PORT)) {
			Receiver.engine(this).halt();
			Receiver.engine(this).StartEngine();
		}
		updateSummaries();
	}

	void fill(String pref, String def, int val, int disp) {
		for (int i = 0; i < getResources().getStringArray(val).length; i++) {
			if (settings.getString(pref, def).equals(
					getResources().getStringArray(val)[i])) {
				getPreferenceScreen().findPreference(pref).setSummary(
						getResources().getStringArray(disp)[i]);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void updateSummaries() {
		for (int i = 0; i < SipdroidEngine.LINES; i++) {
			String j = (i != 0 ? "" + i : "");
			String role = settings.getString(PREF_ROLE, DEFAULT_ROLE);
			String username = settings.getString(PREF_USERNAME + j,
					DEFAULT_USERNAME);
			String server = settings.getString(PREF_SERVER
					+ j, DEFAULT_SERVER);
			String vquality = settings.getString(PREF_VQUALITY, DEFAULT_VQUALITY);
			
			getPreferenceScreen().findPreference(PREF_ROLE).setSummary(role);
			getPreferenceScreen().findPreference(PREF_USERNAME + j).setSummary(
					username);
			getPreferenceScreen().findPreference(PREF_SERVER + j).setSummary(
					server);
			getPreferenceScreen().findPreference(PREF_PORT + j).setSummary(
					settings.getString(PREF_PORT + j, DEFAULT_PORT));
			
			getPreferenceScreen().findPreference(PREF_VQUALITY).setSummary("fuck");
			if (vquality.equals("low")) {
				getPreferenceScreen().findPreference(PREF_VQUALITY).setSummary(getResources().getString(R.string.vquality_display_values1));
			} else {
				getPreferenceScreen().findPreference(PREF_VQUALITY).setSummary(getResources().getString(R.string.vquality_display_values0));
			}
			
		}
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		Editor edit = settings.edit();
		edit.putString(mKey, transferText.getText().toString());
		edit.commit();
	}
}