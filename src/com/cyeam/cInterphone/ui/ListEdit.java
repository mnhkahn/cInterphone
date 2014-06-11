package com.cyeam.cInterphone.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.http.CyeamHttp;
import com.cyeam.cInterphone.model.Process;
import com.cyeam.cInterphone.sqlite.DbHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ListEdit extends LinearLayout {

	private ListView processListView;
	private ImageButton add;
	private ImageButton refresh;
	private ProcessAdapter adapter;
	private Context mContext;
	private Intent intent;

	private void init(Context context) {
		// 为当前view对象设置布局
		View.inflate(context, R.layout.listedit, this);
		
		intent = new Intent(mContext, ProcessActivity.class);

		add = (ImageButton) findViewById(R.id.process_add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mContext.startActivity(intent);
			}
		});

		refresh = (ImageButton) findViewById(R.id.procress_refersh);
		refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getProcesses();
			}
		});

		processListView = (ListView) findViewById(R.id.processes);
	}
	
	public void getProcesses() {
		CyeamHttp.get("process", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray finalResult) {
				db = dbHelper.getWritableDatabase();
				db.execSQL("delete from '" + DbHelper.PROCESS_TABLE	 + "';");
				ContentValues values = new ContentValues();
				for (int i = 0; i < finalResult.length(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					JSONObject obj;
					try {
						obj = finalResult.getJSONObject(i);
						values.clear();
						values.put(DbHelper.C_ID, i + 1);
						values.put(DbHelper.C_CONTENT, obj.getString("content"));
						values.put(DbHelper.C_DURATION, obj.getLong("duration"));
						db.insertOrThrow(DbHelper.PROCESS_TABLE, null, values);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				db.close();
				hander.sendEmptyMessage(0); // 下载完成后发送处理消息
			}
		});
	}

	// 复写父类的构造方法
	public ListEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		// 初始化view对象
		init(context);
		
		dbHelper = new DbHelper(context);

		adapter = new ProcessAdapter(context, R.layout.process_item,
				getData(context));
		processListView.setAdapter(adapter);
		
		getProcesses();
	}
	
	public void setData(JSONArray finalResult) {
		
		
	}

	private Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				adapter = null;
				adapter = new ProcessAdapter(mContext, R.layout.process_item,
						getData(mContext));
				processListView.setAdapter(adapter); // 重新设置ListView的数据适配器
				adapter.notifyDataSetChanged(); // 发送消息通知ListView更新
				break;
			default:
				// do something
				break;
			}
		}
	};

	private DbHelper dbHelper;
	private SQLiteDatabase db;
	
	public List<Process> getData(Context context) {
		List<Process> processes = new ArrayList<Process>();

		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(DbHelper.PROCESS_TABLE, new String[] {
				DbHelper.C_ID, DbHelper.C_CONTENT, DbHelper.C_DURATION },
				null, null, null, null, null);
		while (cursor.moveToNext()) {
			Process process = new Process();
			process.setId(cursor.getInt(0));
			process.setContent(cursor.getString(1));
			process.setDuration(cursor.getLong(2));
			processes.add(process);
		}
		
		db.close();
		return processes;
	}

}
