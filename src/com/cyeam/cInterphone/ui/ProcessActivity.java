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
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.http.CyeamHttp;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ProcessActivity extends Activity {
	private Context mContext;
	private int processid;
	private TimePicker tp;
	private EditText content_text;
	private ImageButton upload;
	private ImageButton delete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_process);
		
		mContext = this;
		
		tp = (TimePicker) findViewById(R.id.tpPicker);
		tp.setIs24HourView(true);
		
		processid = getIntent().getIntExtra("processid", -1);
		Toast.makeText(this, "" + processid,
				Toast.LENGTH_SHORT).show();
		
		upload = (ImageButton) findViewById(R.id.process_upload);
		delete = (ImageButton) findViewById(R.id.process_delete);
		content_text = (EditText) findViewById(R.id.process_content_dialog);
		
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
								Toast.makeText(mContext, "添加成功",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, String responseBody,
									Throwable e) {
								Toast.makeText(mContext, "添加失败:" + statusCode,
										Toast.LENGTH_SHORT).show();
								super.onFailure(statusCode, headers, responseBody, e);
							}
						});
			}
		});
		
	}
	

}
