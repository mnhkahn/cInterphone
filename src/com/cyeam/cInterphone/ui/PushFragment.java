package com.cyeam.cInterphone.ui;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.http.CyeamHttp;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PushFragment extends Fragment {
	private ImageButton send = null;
	private TextView title_text = null;
	private TextView content_text = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_push, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		title_text = (TextView) getActivity().findViewById(
				R.id.notification_title0);
		content_text = (TextView) getActivity().findViewById(
				R.id.notification_content);
		send = (ImageButton) getActivity().findViewById(
				R.id.notification_upload);

		send.setOnClickListener(new View.OnClickListener() {
			private Context mContext = null;

			@Override
			public void onClick(View v) {
				mContext = v.getContext();
				String title = title_text.getText().toString();
				String content = content_text.getText().toString();
				CyeamHttp.get(String.format("push?title=%s&description=%s",
						title, content), null, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String responseBody) {
						clearContentHandler.sendEmptyMessage(0);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							String responseBody, Throwable e) {
						Toast.makeText(mContext, "发送失败:" + statusCode,
								Toast.LENGTH_SHORT).show();
						super.onFailure(statusCode, headers, responseBody, e);
					}
				});
			}
		});
		super.onActivityCreated(savedInstanceState);
	}
	
	private Handler clearContentHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0,
					InputMethodManager.HIDE_NOT_ALWAYS);
			content_text.setText("");
			Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT)	.show();
			super.handleMessage(msg);
		}
		
	};

}
