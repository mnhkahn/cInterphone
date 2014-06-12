package com.cyeam.cInterphone.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.http.CyeamHttp;
import com.cyeam.cInterphone.model.Process;
import com.cyeam.cInterphone.sqlite.DbHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ProcessActivity extends Activity {
	private int processid;
	private TimePicker tp;
	private EditText content_text;
	private ImageButton upload;
	private ImageButton delete;
	
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	
	public Process getProcess(int id) {
		Process process = new Process();
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(DbHelper.PROCESS_TABLE, new String[] {
				DbHelper.C_ID, DbHelper.C_CONTENT, DbHelper.C_DURATION },
				DbHelper.C_ID + "=?", new String[] {Integer.toString(id)}, null, null, null);
		if (cursor.moveToNext()) {
			process.setId(cursor.getInt(0));
			process.setContent(cursor.getString(1));
			process.setDuration(cursor.getLong(2));
		}
		
		db.close();
		return process;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_process);
		
		dbHelper = new DbHelper(this);
		
		tp = (TimePicker) findViewById(R.id.tpPicker);
		tp.setIs24HourView(true);
		
		upload = (ImageButton) findViewById(R.id.process_upload);
		delete = (ImageButton) findViewById(R.id.process_delete);
		content_text = (EditText) findViewById(R.id.process_content_dialog);
		
		processid = getIntent().getIntExtra("processid", -1);
		
		if (processid != -1) {
			Process process = getProcess(processid);
			content_text.setText(process.getContent());
			tp.setCurrentHour((int) (process.getDuration() / 3600));
			tp.setCurrentMinute((int) (process.getDuration() - tp.getCurrentHour() * 3600));
		}
		
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		upload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = content_text.getText().toString();
				try {
					content = URLEncoder.encode(content, "utf-8");
				} catch (UnsupportedEncodingException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				int t = tp.getCurrentHour() * 3600 + tp.getCurrentMinute() * 60;

				JSONObject process = new JSONObject();
		        try {
					process.put("content", content);
					process.put("duration", t);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		        StringEntity entity = null;
				try {
					entity = new StringEntity(process.toString());
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				CyeamHttp.post(v.getContext(), "process", entity, "application/x-www-form-urlencoded; charset=utf-8",
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONArray finalResult) {
								Toast.makeText(CInterphoneApplication.getInstance(), "添加成功",
										Toast.LENGTH_SHORT).show();
							}
							@Override
							public void onFailure(int statusCode,
									Header[] headers, String responseBody,
									Throwable e) {
								Toast.makeText(CInterphoneApplication.getInstance(), "添加失败:" + statusCode,
										Toast.LENGTH_SHORT).show();
								super.onFailure(statusCode, headers, responseBody, e);
							}
						});
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				onBackPressed();
			}
		});
		
	}
	

}
