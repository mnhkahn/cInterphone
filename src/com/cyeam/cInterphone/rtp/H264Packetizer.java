package com.cyeam.cInterphone.rtp;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import net.majorkernelpanic.streaming.rtp.MediaCodecInputStream;

public class H264Packetizer extends Thread {

	private InputStream is;
	byte[] header = new byte[5];
	private int naluLength = 0;
	String TAG = "FUCK";

	public H264Packetizer(InputStream is) {
		this.is = is;
	}

	@Override
	public void run() {
		if (is instanceof MediaCodecInputStream) {
			System.out.println("MediaCodecInputStream");
		} else {
			System.out.println("Nothing");
		}

		int i = 0;
		while (i < 30) {
			try {
				// NAL units are preceeded by their length, we parse the length
				int num = fill(header, 0, 5);
				System.out.println(header[0] + "ffffffffffffffffffff");
				System.out.println(header[1] + "ffffffffffffffffffff");
				System.out.println(header[2] + "ffffffffffffffffffff");
				System.out.println(header[3] + "ffffffffffffffffffff");
				System.out.println(header[4] + "ffffffffffffffffffff");
				naluLength = header[3]&0xFF | (header[2]&0xFF)<<8 | (header[1]&0xFF)<<16 | (header[0]&0xFF)<<24;
				System.out.println(naluLength + "******************8");
				if (naluLength > 100000 || naluLength < 0)
					resync();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
	}

	private void resync() throws IOException {
		int type;

		Log.e(TAG,
				"Packetizer out of sync ! Let's try to fix that...(NAL length: "
						+ naluLength + ")");

		while (true) {

			header[0] = header[1];
			header[1] = header[2];
			header[2] = header[3];
			header[3] = header[4];
			header[4] = (byte) is.read();

			type = header[4] & 0x1F;

			if (type == 5 || type == 1) {
				naluLength = header[3] & 0xFF | (header[2] & 0xFF) << 8
						| (header[1] & 0xFF) << 16 | (header[0] & 0xFF) << 24;
				if (naluLength > 0 && naluLength < 100000) {
					// oldtime = System.nanoTime();
					Log.e(TAG,
							"A NAL unit may have been found in the bit stream !");
					break;
				}
				if (naluLength == 0) {
					Log.e(TAG, "NAL unit with NULL size found...");
				} else if (header[3] == 0xFF && header[2] == 0xFF
						&& header[1] == 0xFF && header[0] == 0xFF) {
					Log.e(TAG, "NAL unit with 0xFFFFFFFF size found...");
				}
			}

		}

	}

	private int fill(byte[] buffer, int offset, int length) throws IOException {
		int sum = 0, len;
		while (sum < length) {
			len = is.read(buffer, offset + sum, length - sum);
			if (len < 0) {
				throw new IOException("End of stream");
			} else
				sum += len;
		}
		return sum;
	}

}
