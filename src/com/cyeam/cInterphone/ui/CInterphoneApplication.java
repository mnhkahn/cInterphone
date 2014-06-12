package com.cyeam.cInterphone.ui;

import com.baidu.frontia.FrontiaApplication;

public class CInterphoneApplication extends FrontiaApplication {
	private static CInterphoneApplication instance;

	public static CInterphoneApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
	}
}
