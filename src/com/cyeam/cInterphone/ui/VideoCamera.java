package com.cyeam.cInterphone.ui;

/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * Copyright (C) 2007 The Android Open Source Project
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import h264.com.VView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import net.majorkernelpanic.streaming.MediaStream;
import net.majorkernelpanic.streaming.Stream;
import net.majorkernelpanic.streaming.gl.SurfaceView;
import net.majorkernelpanic.streaming.video.H264Stream;
import net.majorkernelpanic.streaming.video.VideoQuality;
import net.majorkernelpanic.streaming.video.VideoStream;

import org.sipdroid.media.RtpStreamReceiver;
import org.sipdroid.sipua.ui.CallScreen;
import org.sipdroid.sipua.ui.Receiver;
import org.sipdroid.sipua.ui.SipdroidListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.model.Process;
import com.cyeam.cInterphone.rtp.RtpUtil;
import com.cyeam.cInterphone.video.H264Util;

public class VideoCamera extends CallScreen implements SipdroidListener,
		SurfaceHolder.Callback, MediaRecorder.OnErrorListener,
		MediaPlayer.OnErrorListener, OnClickListener, OnLongClickListener {

	Thread t;
	Context mContext = this;

	private static final String TAG = "videocamera";

	// private static int UPDATE_RECORD_TIME = 1;

	// private static final float VIDEO_ASPECT_RATIO = 144.0f / 176.0f;
	// VideoPreview mVideoPreview;

	SurfaceView mPreviewSurfaceView;
	SurfaceHolder mPreviewHolder;

	View mPlaySurfaceView;

	// VideoView mVideoFrame;
	// MediaController mMediaController;
	// MediaPlayer mediaPlayer;

	// private TextView mRecordingTimeView, mFPS;

	// ArrayList<MenuItem> mGalleryItems = new ArrayList<MenuItem>();
	//
	// View mPostPictureAlert;
	// LocationManager mLocationManager = null;
	//
	// // private Handler mHandler = new MainHandler();
	LocalSocket receiver, sender;
	LocalServerSocket lss;

	// int obuffering, opos;
	// int fps;

	/**
	 * This Handler is used to post message back onto the main thread of the
	 * application
	 */
	// private class MainHandler extends Handler {
	// @Override
	// public void handleMessage(Message msg) {
	// long now = SystemClock.elapsedRealtime();
	// long delta = now - Receiver.ccCall.base;
	//
	// long seconds = (delta + 500) / 1000; // round to nearest
	// long minutes = seconds / 60;
	// long hours = minutes / 60;
	// long remainderMinutes = minutes - (hours * 60);
	// long remainderSeconds = seconds - (minutes * 60);
	//
	// String secondsString = Long.toString(remainderSeconds);
	// if (secondsString.length() < 2) {
	// secondsString = "0" + secondsString;
	// }
	// String minutesString = Long.toString(remainderMinutes);
	// if (minutesString.length() < 2) {
	// minutesString = "0" + minutesString;
	// }
	// String text = minutesString + ":" + secondsString;
	// if (hours > 0) {
	// String hoursString = Long.toString(hours);
	// if (hoursString.length() < 2) {
	// hoursString = "0" + hoursString;
	// }
	// text = hoursString + ":" + text;
	// }
	// mRecordingTimeView.setText(text);
	// if (fps != 0)
	// mFPS.setText(fps + (videoQualityHigh ? "h" : "l") + "fps");
	// if (mVideoFrame != null) {
	// int buffering = mVideoFrame.getBufferPercentage(), pos = mVideoFrame
	// .getCurrentPosition();
	// if (buffering != 100 && buffering != 0) {
	// mMediaController.show();
	// }
	// if (buffering != 0 && !mMediaRecorderRecording)
	// // mVideoPreview.setVisibility(View.INVISIBLE);
	// mPreviewSurfaceView.setVisibility(View.INVISIBLE);
	// if (((obuffering != buffering && buffering == 100) || (opos == 0 && pos >
	// 0))
	// && rtp_socket != null) {
	// RtpPacket keepalive = new RtpPacket(new byte[12], 0);
	// keepalive.setPayloadType(125);
	// try {
	// rtp_socket.send(keepalive);
	// } catch (IOException e) {
	// }
	// }
	// obuffering = buffering;
	// opos = pos;
	// }
	//
	// // Work around a limitation of the T-Mobile G1: The T-Mobile
	// // hardware blitter can't pixel-accurately scale and clip at the
	// // same time,
	// // and the SurfaceFlinger doesn't attempt to work around this
	// // limitation.
	// // In order to avoid visual corruption we must manually refresh the
	// // entire
	// // surface view when changing any overlapping view's contents.
	// // mVideoPreview.invalidate();
	// // mPreviewSurfaceView.invalidate();
	// mHandler.sendEmptyMessageDelayed(UPDATE_RECORD_TIME, 1000);
	// }
	// };

	/** Called with the activity is first created. */
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// mLocationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);

		// setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setScreenOnFlag();
		setContentView(R.layout.activity_cinterphone_screen);

		// Sets the port of the RTSP server to 1234
		// Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
		// .edit();
		// editor.putString(RtspServer.KEY_PORT, String.valueOf(1234));
		// editor.commit();

		mPreviewSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
		mPreviewHolder = mPreviewSurfaceView.getHolder();
		mPreviewHolder.addCallback(this);
		mPreviewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mPlaySurfaceView = (View) findViewById(R.id.video_frame_cinterphone);

		ImageView stopButton = (ImageView) findViewById(R.id.camera_stop);
		stopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		ImageView switchButton = (ImageView) findViewById(R.id.camera_switch);

		ImageView muteButton = (ImageView) findViewById(R.id.camera_mute);
		muteButton.setTag(R.drawable.medium_volume);
		muteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((Integer) v.getTag() == R.drawable.medium_volume) {
					((ImageView) v).setImageResource(R.drawable.mute);
					v.setTag(R.drawable.mute);
				} else {
					((ImageView) v).setImageResource(R.drawable.medium_volume);
					v.setTag(R.drawable.medium_volume);
				}

			}
		});

		// Get processes from db
		Process[] processes = null;

		for (int i = 0; i < 3; i++) {
			Process process = new Process();
			process.setContent("content" + new Integer(i).toString());
			process.setDuration(500);
		}

		// Start proable
		// new Thread(new Proable(processes)).start();
	}

	class Proable implements Runnable {
		private Process[] processes;

		public Proable(Process[] processes) {
			this.processes = processes;
		}

		@Override
		public void run() {
		}
	}

	int speakermode;
	boolean justplay;

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// videoStream.stop();
	}

	/*
	 * catch the back and call buttons to return to the in call activity.
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	//
	// switch (keyCode) {
	// // finish for these events
	// case KeyEvent.KEYCODE_CALL:
	// Receiver.engine(this).togglehold();
	// case KeyEvent.KEYCODE_BACK:
	// finish();
	// return true;
	//
	// case KeyEvent.KEYCODE_CAMERA:
	// // Disable the CAMERA button while in-call since it's too
	// // easy to press accidentally.
	// return true;
	//
	// case KeyEvent.KEYCODE_VOLUME_DOWN:
	// case KeyEvent.KEYCODE_VOLUME_UP:
	// RtpStreamReceiver.adjust(keyCode, true);
	// return true;
	// }
	//
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean result = super.onPrepareOptionsMenu(menu);

		menu.findItem(VIDEO_MENU_ITEM).setVisible(false);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case VIDEO_MENU_ITEM:
			intent.removeExtra("justplay");
			onResume();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		mPreviewHolder = holder;
		// holder.setFixedSize(w, h);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mPreviewHolder = holder;
		// 预览
		initializeVideo();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mPreviewHolder = null;
	}

	boolean isAvailableSprintFFC, useFront = true;

	boolean videoQualityHigh;
	Camera mCamera;

	private Stream videoStream = null;

	// private MediaCodec codec = null;

	// initializeVideo() starts preview and prepare media recorder.
	// Returns false if initializeVideo fails
	@SuppressLint("NewApi")
	private boolean initializeVideo() {
		Log.v(TAG, "initializeVideo");

		videoStream = new H264Stream();
		try {
			((VideoStream) videoStream).setSurfaceView(mPreviewSurfaceView);
			((VideoStream) videoStream).setVideoQuality(new VideoQuality(800,
					600, 30, 500000));
			((MediaStream) videoStream).setDestinationAddress(InetAddress
					.getByName("127.0.0.1"));
			((MediaStream) videoStream).setDestinationPorts(9080);
			((VideoStream) videoStream).setPreferences(PreferenceManager
					.getDefaultSharedPreferences(mContext));
			videoStream.configure();
			// ((VideoStream) videoStream).startPreview();
			videoStream.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("FUCK", "skip mp4 header success!");

		// codec = MediaCodec.createDecoderByType("Video/AVC");

		new FuckThread().start();

		return true;
	}

	class FuckThread extends Thread {
		MulticastSocket socket = null;

		public void run() {
			int iTemp = 0;
			int nalLen;
			VView.InitDecoder(800, 600);
			byte[] h264 = new byte[0];
			byte[] mPixel = new byte[200000];
			// FileWriter fr = null;
			// try {
			// fr = new FileWriter(new File("/sdcard/fuck.264"), true);
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			while (!Thread.currentThread().isInterrupted()) {
				try {
					socket = new MulticastSocket(9080);
					InetAddress group = InetAddress.getByName("239.0.0.1");
					socket.joinGroup(group);
					byte buff[] = new byte[8192];
					DatagramPacket packet = null;
					packet = new DatagramPacket(buff, buff.length);
					try {
						h264 = new byte[0];
						socket.receive(packet);
						boolean error = true;
						byte[] payload = RtpUtil.getPayload(packet.getData(),
								packet.getLength());
						
						int s = H264Util.getS(payload);
						int e = H264Util.getE(payload);
						System.out.println(s + " " + e);

//						if (H264Util.getType(payload) == 28) {
//							int s = H264Util.getS(payload);
//							if (s == 0) {
//								error = false;
//							}
//							if (s == 1) {
//								h264 = RtpUtil.merge(new byte[0], payload);
//							}
//							for (int e = H264Util.getE(payload); e != 1;) {
//								socket.receive(packet);
//								byte[] payload1 = RtpUtil.getPayload(packet.getData(),
//										packet.getLength());
//								System.out.println(s + " " + e);
//								h264 = RtpUtil.merge(h264, payload1);
//								if (e == 1) {
//									h264 = RtpUtil.merge(h264, payload1);
//									RtpUtil.addHeader(h264);
//								}
//							}							
//						} else {
//							int s = H264Util.getS(payload);
//							int e = H264Util.getE(payload);
//							System.out.println(s + " " + e);
//						}
//						System.out.println(h264.length - 4 + "" + (h264[3] & 0xFF | (h264[2] & 0xFF) << 8
//								| (h264[1] & 0xFF) << 16 | (h264[0] & 0xFF) << 24));
						// byte[] h264 = RtpUtil.addHeader(RtpUtil.getPayload(
						// packet.getData(), packet.getLength()));
						// if (h264.length > 0) {
						// fr.append(new String(h264));

						// System.out.println(RtpUtil.getPayload(packet.getData(),
						// packet.getLength()).length + " " + (h264[3]&0xFF |
						// (h264[2]&0xFF)<<8 | (h264[1]&0xFF)<<16 |
						// (h264[0]&0xFF)<<24));
						// iTemp = VView.DecoderNal(h264, h264.length, mPixel);
						// System.out.println(iTemp);
						// }
						// System.out.println(RtpUtil.getPayload(packet.getData(),
						// packet.getLength())[0]);
						// System.out.println("Version:" +
						// RtpUtil.getVersion(packet.getData(),
						// packet.getLength()));
						// System.out.println("Payload Type:" +
						// RtpUtil.getVersion(packet.getData(),
						// packet.getLength()));
						// System.out.println("Header Length" +
						// RtpUtil.getHeaderLength(packet.getData(),
						// packet.getLength()));
						// iTemp =
						// VView.DecoderNal(RtpUtil.getPayload(packet.getData(),
						// packet.getLength()),
						// RtpUtil.getPayloadLength(packet.getData(),
						// packet.getLength()), mPixel);
						// System.out.println(iTemp);
						// mPlaySurfaceView.draw(canvas);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
				}
			}
			// try {
			// fr.flush();
			// fr.close();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			VView.UninitDecoder();
		}
	}

	private void releaseMediaRecorder() {
		Log.v(TAG, "Releasing media recorder.");
	}

	public void onError(MediaRecorder mr, int what, int extra) {
		if (what == MediaRecorder.MEDIA_RECORDER_ERROR_UNKNOWN) {
			System.out.println("unknown media recorder");
			finish();
		}
	}

	boolean change;

	private void stopVideoRecording() {
		Log.v(TAG, "stopVideoRecording");
	}

	private void setScreenOnFlag() {
		Window w = getWindow();
		final int keepScreenOnFlag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		if ((w.getAttributes().flags & keepScreenOnFlag) == 0) {
			w.addFlags(keepScreenOnFlag);
		}
	}

	public void onHangup() {
		finish();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			RtpStreamReceiver.adjust(keyCode, false);
			return true;
		case KeyEvent.KEYCODE_ENDCALL:
			if (Receiver.pstn_state == null
					|| (Receiver.pstn_state.equals("IDLE") && (SystemClock
							.elapsedRealtime() - Receiver.pstn_time) > 3000)) {
				// Receiver.engine(mContext).rejectcall();
				return true;
			}
			break;
		}
		return false;
	}

	static TelephonyManager tm;

	static boolean videoValid() {
		if (Receiver.on_wlan)
			return true;
		if (tm == null)
			tm = (TelephonyManager) Receiver.mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getNetworkType() < TelephonyManager.NETWORK_TYPE_UMTS)
			return false;
		return true;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return true;
	}

	@Override
	public void onClick(View v) {
		useFront = !useFront;
		// initializeVideo();
		change = true;
	}

	@Override
	public boolean onLongClick(View v) {
		videoQualityHigh = !videoQualityHigh;
		// initializeVideo();
		change = true;
		return true;
	}

}
