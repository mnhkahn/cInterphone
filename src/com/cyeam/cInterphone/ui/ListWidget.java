package com.cyeam.cInterphone.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.http.CyeamHttp;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ListWidget extends LinearLayout {

	private ListView processListView;
	private ImageButton refresh;
	private SimpleAdapter adapter;
	private Context context;
	private List data;
	private boolean isRefresh = true;

	private void init(Context context) {
		// 为当前view对象设置布局
		View.inflate(context, R.layout.listwidget, this);

		processListView = (ListView) findViewById(R.id.users);
		refresh = (ImageButton) findViewById(R.id.user_refersh);
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				isRefresh = true;
				getUsers("fuck");
			}
		});
		getUsers("fuck");
//		usersThread.start();
	}

	// 复写父类的构造方法
	public ListWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// 初始化view对象
		init(context);
	}
	
	public void getUsers(String username) {
		CyeamHttp.get("users/" + username, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray finalResult) {
				List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
				
				for (int i = 0; i < finalResult.length(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					try {
						JSONObject obj = finalResult.getJSONObject(i);
						map.put("id", i + 1);
						map.put("content", obj.get("username"));
						if (obj.get("role").equals("Command")) {
							map.put("role", "管理员");
						}
						else {
							map.put("role", "");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					data.add(map);
				}
				adapter = new SimpleAdapter(context, data, R.layout.item,
						 new String[] {"id", "content", "role" }, new int[] { R.id.list_index,
						 R.id.list_content, R.id.list_content1});
				hander.sendEmptyMessage(0); // 下载完成后发送处理消息
			}
		});
	}
	
	private Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
            case 0:
    			Toast.makeText(context, "refresh user", Toast.LENGTH_SHORT);
                adapter.notifyDataSetChanged(); //发送消息通知ListView更新
                processListView.setAdapter(adapter); // 重新设置ListView的数据适配器
                break;
            default:
                //do something
                break;
            }
        }
    };
}
