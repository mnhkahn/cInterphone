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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cyeam.cInterphone.R;

public class ListWidget extends LinearLayout {

	private ListView processListView;
	private SimpleAdapter adapter;
	private Context context;
	private List data;

	private void init(Context context) {
		// 为当前view对象设置布局
		View.inflate(context, R.layout.listwidget, this);

		processListView = (ListView) findViewById(R.id.processes);
		// adapter = new SimpleAdapter(context, getData(context), R.layout.item,
		// new String[] {"id", "content", "role" }, new int[] { R.id.list_index,
		// R.id.list_content, R.id.list_content1});
		// processListView.setAdapter(adapter);
		usersThread.start();

	}

	// 复写父类的构造方法
	public ListWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// 初始化view对象
		init(context);
	}

	//
	// public static List getData(Context context) {
	//
	// }

	private Thread usersThread = new Thread() {
		@Override
		public void run() {
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
			AndroidHttpClient req = AndroidHttpClient.newInstance("cInterphone");
			HttpGet get = new HttpGet();
			HttpResponse resq = null;
			try {
				get.setURI(new URI("http://192.168.1.102:8080/users/fdsa"));
				resq = req.execute(get);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(resq.getEntity().getContent(),
								"UTF-8"));
				String json = "";
				String line;
				while ((line = reader.readLine()) != null) {
					json += line;					
				}

				JSONTokener tokener = new JSONTokener(json);
				JSONArray finalResult = new JSONArray(tokener);

				for (int i = 0; i < finalResult.length(); i++) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					JSONObject obj = finalResult.getJSONObject(i);
					map.put("id", i);
					map.put("content", obj.get("username"));
					if (obj.get("role").equals("Command")) {
						map.put("role", "管理员");
					}
					else {
						map.put("role", "");
					}
					data.add(map);
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				req.close();
			}
			adapter = new SimpleAdapter(context, data, R.layout.item,
					 new String[] {"id", "content", "role" }, new int[] { R.id.list_index,
					 R.id.list_content, R.id.list_content1});
			hander.sendEmptyMessage(0); // 下载完成后发送处理消息
//					 processListView.setAdapter(adapter);

		}
	};
	
	private Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
            case 0:
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
