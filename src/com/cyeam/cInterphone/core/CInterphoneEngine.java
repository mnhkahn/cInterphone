package com.cyeam.cInterphone.core;

import org.zoolu.sip.address.NameAddress;

public class CInterphoneEngine implements RegisterAgentListener {

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
