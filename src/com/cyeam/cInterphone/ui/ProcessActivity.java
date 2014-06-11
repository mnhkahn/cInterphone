package com.cyeam.cInterphone.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cyeam.cInterphone.R;

public class ProcessActivity extends Activity {
	private int processid;
	private TimePicker tp;
	private EditText content;
	private ImageButton upload;
	private ImageButton delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_process);
		
		tp = (TimePicker) findViewById(R.id.tpPicker);
		tp.setIs24HourView(true);
		
		processid = getIntent().getIntExtra("processid", -1);
		Toast.makeText(this, "" + processid,
				Toast.LENGTH_SHORT).show();
		
		upload = (ImageButton) findViewById(R.id.process_upload);
		delete = (ImageButton) findViewById(R.id.process_delete);
		content = (EditText) findViewById(R.id.process_content_dialog);
		
		upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int t = tp.getCurrentHour() * 3600 + tp.getCurrentMinute() * 60; 
				Toast.makeText(v.getContext(), content.getText().toString() + t,
						Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	

}
