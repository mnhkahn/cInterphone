package com.cyeam.cInterphone.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyeam.cInterphone.R;

public class ProcessFragment extends Fragment {

	private ListEdit listEdit = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_process, container, false);
	}

	@Override
	public void onResume() {
		listEdit.getProcesses();
		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		listEdit = (ListEdit)getActivity().findViewById(R.id.listedit);
		super.onActivityCreated(savedInstanceState);
	}
}
