package com.cyeam.cInterphone.video;

public class H264Util {

	public static int getType(byte[] data) {
		return (data[0] & 0x1F) & 0xFF;
	}

	public static int getS(byte[] data) {
		return (data[1] & 0x80) >> 7;
	}

	public static int getE(byte[] data) {
		return (data[1] & 0x40) >> 6;
	}
}
