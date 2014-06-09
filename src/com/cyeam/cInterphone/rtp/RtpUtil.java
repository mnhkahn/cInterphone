package com.cyeam.cInterphone.rtp;

import java.nio.ByteBuffer;

public class RtpUtil {
	
	public static int getVersion(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return (packet[0] >> 6 & 0x03);
		else
			return 0; // broken packet
	}
	
	public static int getCscrCount(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return (packet[0] & 0x0F);
		else
			return 0; // broken packet
	}

	public static int getHeaderLength(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return 12 + 4 * getCscrCount(packet, packet_len);
		else
			return packet_len; // broken packet
	}

	
	public static byte[] getPayload(byte [] packet, int packet_len) {
		int header_len = getHeaderLength(packet, packet_len);
		int len = packet_len - header_len;
		byte[] payload = new byte[len];
		for (int i = 0; i < len; i++)
			payload[i] = packet[header_len + i];
		return payload;
	}
	
	public static int getPayloadLength(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return packet_len - getHeaderLength(packet, packet_len);
		else
			return 0; // broken packet
	}
	
	public static long getTimestamp(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return getLong(packet, 4, 8);
		else
			return 0; // broken packet
	}
	
	public static int getSequenceNumber(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return getInt(packet, 2, 4);
		else
			return 0; // broken packet
	}
	
	public static boolean hasMarker(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return getBit(packet[1], 7);
		else
			return false; // broken packet
	}
	
	public static boolean hasExtension(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return getBit(packet[0], 4);
		else
			return false; // broken packet
	}
	
	public static boolean hasPadding(byte [] packet, int packet_len) {
		if (packet_len >= 12)
			return getBit(packet[0], 5);
		else
			return false; // broken packet
	}
	
	/** Gets int value */
	public static int getInt(byte b) {
		return ((int) b + 256) % 256;
	}

	/** Gets long value */
	public static long getLong(byte[] data, int begin, int end) {
		long n = 0;
		for (; begin < end; begin++) {
			n <<= 8;
			n += data[begin] & 0xFF;
		}
		return n;
	}

	/** Sets long value */
	public static void setLong(long n, byte[] data, int begin, int end) {
		for (end--; end >= begin; end--) {
			data[end] = (byte) (n % 256);
			n >>= 8;
		}
	}

	/** Gets Int value */
	public static int getInt(byte[] data, int begin, int end) {
		return (int) getLong(data, begin, end);
	}

	/** Sets Int value */
	public static void setInt(int n, byte[] data, int begin, int end) {
		setLong(n, data, begin, end);
	}

	/** Gets bit value */
	public static boolean getBit(byte b, int bit) {
		return (b >> bit) == 1;
	}

	/** Sets bit value */
	public static byte setBit(boolean value, byte b, int bit) {
		if (value)
			return (byte) (b | (1 << bit));
		else
			return (byte) ((b | (1 << bit)) ^ (1 << bit));
	}
	
	public static byte [] addHeader(byte[] data) {
		byte[] h264 = new byte[data.length + 4];
		for (int i = 0; i < data.length; i++) {
			h264[i + 4] = data[i];
		}
		byte[] lens = ByteBuffer.allocate(4).putInt(data.length).array();
		h264[0] = lens[0];
		h264[1] = lens[1];
		h264[2] = lens[2];
		h264[3] = lens[3];
		return h264;
	}
	
	public static byte [] merge(byte[] a, byte[] b) {
		byte[] m = new byte[a.length + b.length];
		int i = 0;
		for (; i < a.length; i++) {
			m[i] = a[i];
		}
		for (int j = 0; j < b.length; j++) {
			m[i++] = b[j];
		}
		return m;
	}
}
