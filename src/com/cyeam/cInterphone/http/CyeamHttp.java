package com.cyeam.cInterphone.http;

import org.apache.http.HttpEntity;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CyeamHttp {
	private static final String BASE_URL = "http://192.168.1.102:8080/";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void post(Context context, String url, HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler) {
		client.post(context, getAbsoluteUrl(url), entity, contentType, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		Log.e("URL", BASE_URL + relativeUrl);
		return BASE_URL + relativeUrl;
	}
}