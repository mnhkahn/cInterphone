package com.cyeam.cInterphone.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Process;

public class ProcessAdapter extends ArrayAdapter<Process> {

	private int resource;
//	HashMap<Integer, View> map = new HashMap<Integer, View>();

	public ProcessAdapter(Context context, int resource, List<Process> objects) {
		super(context, resource, objects);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout processListView = null;

		Process process = getItem(position);
		String content = process.getContent();

		if (convertView == null) {
			processListView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					inflater);
			vi.inflate(this.resource, processListView, true);
		} else {
			processListView = (LinearLayout) convertView;
		}

		TextView contentTextView = (TextView) processListView
				.findViewById(R.id.process_content);
		contentTextView
				.setText("[持续:" + process.getDuration() + "秒]" + content);

		TextView indexTextView = (TextView) processListView
				.findViewById(R.id.process_index);
		indexTextView.setText("" + process.getId());

		ImageView update = (ImageView) processListView
				.findViewById(R.id.process_update);
		update.setTag(process.getId());
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater)v.getContext().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.dialog_process,
						(ViewGroup) v.findViewById(R.id.dialog));

//				new AlertDialog.Builder(v.getContext()).setTitle("創建流程")
//						.setView(layout).setPositiveButton("确定", null)
//						.setNegativeButton("取消", null).show();
				Intent intent = new Intent(v.getContext(), ProcessActivity.class);
				intent.putExtra("processid", (Integer)v.getTag());
				v.getContext().startActivity(intent);
			}

		});

		return processListView;
	}

}
