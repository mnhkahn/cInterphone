package com.cyeam.cInterphone.util;

import android.os.CountDownTimer;

public class Timer extends CountDownTimer {
	
	private static long countDownInterval = 1000;
	private int index;

	public Timer(long millisInFuture) {
		super(millisInFuture, countDownInterval);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub

	}

}
