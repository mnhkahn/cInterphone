package com.cyeam.cInterphone.ui;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Process;

public class ProcessView extends LinearLayout {
	private TextView timer_view = null;
	private TextView process_now = null;
	private TextView process_next = null;
	private List<Process> processes = new ArrayList<Process>();
	private static CountDownTimer timer = null;
	private Integer i = 0;
	private final static String TIMER_FORMAT = "00:00:0%s";

	private void init(Context context) {
		// 为当前view对象设置布局
		View.inflate(context, R.layout.processview, this);
		timer_view = (TextView) findViewById(R.id.process_timer);
		process_now = (TextView) findViewById(R.id.process_now);
		process_next = (TextView) findViewById(R.id.process_next);

		for (int i = 0; i < 10; i++) {
			Process process = new Process();
			process.setId(i);
			process.setContent("Hello, world" + i);
			process.setDuration(5);
			processes.add(process);
		}
	}

	public ProcessView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

		Timer();
	}

	public void Timer() {
		if (i >= processes.size()) {
			return ;
		}
		Toast.makeText(getContext(), "test" + i,
				Toast.LENGTH_SHORT).show();
		process_now.setText(processes.get(i).getContent());
		if (i < processes.size() - 1) {
			process_next.setText(processes.get(i + 1).getContent());
		}
		else {
			process_now.setText("none");
		}
		new CountDownTimer(processes.get(i).getDuration() * 1000, 1000) {

			
			public void onTick(long millisUntilFinished) {
				timer_view.setText(String.format(TIMER_FORMAT, millisUntilFinished/ 1000));
			}

			public void onFinish() {
				timer_view.setText(String.format(TIMER_FORMAT, processes.get(i).getDuration()));
				++i;
				Timer();
			}
		}.start();
//		Timer(++i);
	}
	
//	class Timer extends CountDownTimer {
//		private int index;
//		private static final long countDownInterval = 1000;
//		private TextView timer_view;
//		public Timer(long millisInFuture, TextView timer_view, int i) {
//			super(millisInFuture, countDownInterval);
//			this.index = i;
//			this.timer_view = timer_view;
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public void onTick(long millisUntilFinished) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onFinish() {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}

}
