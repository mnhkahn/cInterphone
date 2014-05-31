package com.cyeam.cInterphone.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Process;

public class ProcessView extends LinearLayout {
	private LinearLayout process_timer_container = null;
	private TextView timer_view = null;
	private TextView process_now = null;
	private TextView process_next = null;
	private List<Process> processes = new ArrayList<Process>();
	private static CountDownTimer timer = null;
	private Integer i = 0;
	private final static String TIMER_FORMAT = "00:00:0%ss";
	private final static String PROCESS_FORMAT = "[时长%d秒]<b>%s</b>";

	private void init(Context context) {
		// 为当前view对象设置布局
		View.inflate(context, R.layout.processview, this);
		process_timer_container = (LinearLayout) findViewById(R.id.process_timer_container);
		timer_view = (TextView) findViewById(R.id.process_timer);
		process_now = (TextView) findViewById(R.id.process_now);
		process_next = (TextView) findViewById(R.id.process_next);
		
		process_timer_container.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				process_timer_container.setBackgroundColor(Color.CYAN);
			}
		});

		for (int i = 0; i < 10; i++) {
			Process process = new Process();
			process.setId(i);
			process.setContent("测试流程控件是否正常工作，这是第" + i + "个流程。");
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
			timer_view.setText(String.format(TIMER_FORMAT, 0));
			process_now.setText("没有流程");
			return ;
		}
		process_timer_container.setBackgroundColor(Color.WHITE);
		Toast.makeText(getContext(), "test" + i,
				Toast.LENGTH_SHORT).show();
		process_now.setText(Html.fromHtml(String.format(PROCESS_FORMAT, processes.get(i).getDuration(), processes.get(i).getContent())));
		if (i < processes.size() - 1) {
			process_next.setText(Html.fromHtml(String.format(PROCESS_FORMAT, processes.get(i + 1).getDuration(), processes.get(i + 1).getContent())));
		}
		else {
			process_next.setText("没有流程");
		}
		new CountDownTimer(processes.get(i).getDuration() * 1000, 1000) {
			public void onTick(long millisUntilFinished) {
				timer_view.setText(String.format(TIMER_FORMAT, millisUntilFinished/ 1000));
				
				if (((ColorDrawable) process_timer_container.getBackground()).getColor() == Color.RED) {
					process_timer_container.setBackgroundColor(Color.WHITE);
				} else if (((ColorDrawable) process_timer_container.getBackground()).getColor() == Color.WHITE) {
					process_timer_container.setBackgroundColor(Color.RED);
				}
			}

			public void onFinish() {
				timer_view.setText(String.format(TIMER_FORMAT, processes.get(i).getDuration()));
				++i;
				Timer();
			}
		}.start();
	}
	
}
