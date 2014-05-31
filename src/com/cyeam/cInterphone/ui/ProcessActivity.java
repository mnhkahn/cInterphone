package com.cyeam.cInterphone.ui;

import com.cyeam.cInterphone.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ProcessActivity extends Activity {
	private int processid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_process);
		
		processid = getIntent().getIntExtra("processid", -1);
		Toast.makeText(this, "" + processid,
				Toast.LENGTH_SHORT).show();
	}
	

}
