package com.cyeam.cInterphone.core;

import org.sipdroid.sipua.ui.Receiver;
import org.zoolu.sip.address.NameAddress;

import android.app.AlertDialog;
import android.content.Context;

import com.cyeam.cInterphone.R;

public class CInterphoneEngine implements RegisterAgentListener {
	private static AlertDialog m_AlertDlg;
	// 呼叫请求
	public static void call_menu(Context context, String target) {
		if (m_AlertDlg != null) {
			m_AlertDlg.cancel();
		}
		if (target.length() == 0)
			m_AlertDlg = new AlertDialog.Builder(context)
					.setMessage(R.string.empty).setTitle(R.string.app_name)
					.setIcon(R.drawable.icon22).setCancelable(true).show();
		else if (!Receiver.engine(context).call(target, true))
			m_AlertDlg = new AlertDialog.Builder(context)
					.setMessage(R.string.notfast).setTitle(R.string.app_name)
					.setIcon(R.drawable.icon22).setCancelable(true).show();
	}
	
	@Override
	public void onUaRegistrationSuccess(RegisterAgent ra, NameAddress target,
			NameAddress contact, String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMWIUpdate(RegisterAgent ra, boolean voicemail, int number,
			String vmacc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUaRegistrationFailure(RegisterAgent ra, NameAddress target,
			NameAddress contact, String result) {
		// TODO Auto-generated method stub

	}

}
