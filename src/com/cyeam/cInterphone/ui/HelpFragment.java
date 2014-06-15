package com.cyeam.cInterphone.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cyeam.cInterphone.R;

public class HelpFragment extends Fragment {
	
	SharedPreferences sharedPreferences;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_help, container, false);
	}
	
	private RadioGroup role;
    private RadioButton commandar, coordinate, defaultRole;
    private ImageButton callBtn;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		role = (RadioGroup) getActivity().findViewById(R.id.radioGroup);
        commandar = (RadioButton) getActivity().findViewById(R.id.role_commandar);
        coordinate = (RadioButton) getActivity().findViewById(R.id.rool_coordinate);
        defaultRole = (RadioButton) getActivity().findViewById(R.id.role_default);
        callBtn = (ImageButton)getActivity().findViewById(R.id.sip_invite);
        
        role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@SuppressLint("CommitPrefEdits")
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Editor editor = sharedPreferences.edit();
				String role = "";
				if(checkedId == commandar.getId()) {
					role = commandar.getText().toString();
				} else if (checkedId == coordinate.getId()) {
					role = coordinate.getText().toString();
				} else {
					role = defaultRole.getText().toString();
				}
				editor.putString(Settings.PREF_ROLE, role);
				editor.commit();
				Intent intent = new Intent();//创建Intent对象
				intent.setAction("com.cyeam.cInterphone.ui.update");
				intent.putExtra("role", role);
				getActivity().sendBroadcast(intent);//发送广播
			}
        	
        });
        IntentFilter filter = new IntentFilter();//创建IntentFilter对象
		filter.addAction("com.cyeam.cInterphone.ui.update");
		getActivity().registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String role = intent.getExtras().getString("role");
				if (role.equals("指挥")) {
					callBtn.setImageResource(R.drawable.ic_jog_dial_answer);
					callBtn.setEnabled(true);
				} else {
					callBtn.setImageResource(R.drawable.ic_jog_dial_decline);
					callBtn.setEnabled(false);
				}
//				Log.e("", role);
			}
			
		}, filter);
        super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String role = sharedPreferences.getString(Settings.PREF_ROLE, Settings.DEFAULT_ROLE);
		if (role.equals("指挥")) {
			commandar.setChecked(true);
		} else if (role.equals("协调")) {
			coordinate.setChecked(true);
		} else {
			defaultRole.setChecked(true);
		}
	}
}
