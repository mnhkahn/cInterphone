package com.cyeam.cInterphone.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Process;

public class ListEdit extends LinearLayout {

	private ListView processListView;
	private ProcessAdapter adapter;
	private Context context;
	
	private void init(Context context) {
		//为当前view对象设置布局
		View.inflate(context, R.layout.listedit , this);
		
		processListView = (ListView)findViewById(R.id.processes);
		adapter = new ProcessAdapter(context, R.layout.process_item, getData(context));
		processListView.setAdapter(adapter);

	}
	//复写父类的构造方法
	public ListEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		//初始化view对象
		init(context);

	}
	
	public static List<Process> getData(Context context) {

		ContentResolver resolver = context.getContentResolver();

		List<Process> processes = new ArrayList<Process>();
	
		for (int i = 0; i < 10; i++) {
			Process process = new Process();
			process.setId(i);
			process.setContent("Hello, world");
			process.setDuration(500);
			processes.add(process);
		}
		
		return processes;
	}
	
}
