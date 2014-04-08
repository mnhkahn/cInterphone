package com.cyeam.cInterphone.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.cyeam.cInterphone.R;

public class RecordService extends Service {
	private static final String TAG = "[cInterphone|VideoService]";

	private View view;
	private SurfaceView sv;
	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	private MediaRecorder mediaRecorder;
	private Camera camera;
	private boolean isRun;
	private File file;
	private DrawThread dt;

	/**
	 * 记录系统状态栏的高度
	 */
	private static int statusBarHeight;
	/**
	 * 记录当前手指位置在屏幕上的横坐标值
	 */
	private float xInScreen;

	/**
	 * 记录当前手指位置在屏幕上的纵坐标值
	 */
	private float yInScreen;

	/**
	 * 记录手指按下时在屏幕上的横坐标的值
	 */
	private float xDownInScreen;

	/**
	 * 记录手指按下时在屏幕上的纵坐标的值
	 */
	private float yDownInScreen;

	/**
	 * 记录手指按下时在小悬浮窗的View上的横坐标的值
	 */
	private float xInView;

	/**
	 * 记录手指按下时在小悬浮窗的View上的纵坐标的值
	 */
	private float yInView;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		view = LayoutInflater.from(this).inflate(R.layout.float_window_small,
				null);
		sv = (SurfaceView) view.findViewById(R.id.me_view);
		//sv.setBackgroundColor(Color.BLACK);
		sv.getHolder().addCallback(new Callback() {

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				if (dt == null) {
					isRun = true;
					dt = new DrawThread(holder);
					
					dt.start();
				}
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				isRun = false;
			}
		});

		windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();

		params = new LayoutParams();
		params.type = LayoutParams.TYPE_PHONE;
		params.format = PixelFormat.RGBA_8888;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		params.gravity = Gravity.LEFT | Gravity.TOP;
		params.width = screenWidth / 4;
		params.height = screenHeight / 4;
		params.x = 10;
		params.y = screenHeight / 2;

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
					xInView = event.getX();
					yInView = event.getY();
					xDownInScreen = event.getRawX();
					yDownInScreen = event.getRawY() - getStatusBarHeight();
					xInScreen = event.getRawX();
					yInScreen = event.getRawY() - getStatusBarHeight();
					break;
				case MotionEvent.ACTION_MOVE:
					xInScreen = event.getRawX();
					yInScreen = event.getRawY() - getStatusBarHeight();
					// 手指移动的时候更新小悬浮窗的位置
					updateViewPosition();
					break;
				default:
					break;
				}
				return true;
			}
		});

		file = new File("/sdcard/video.mp4");
		if (file.exists()) {
			file.delete();
		}
		mediaRecorder = new MediaRecorder();
		camera = getCameraInstance();

		windowManager.addView(view, params);
	}

	@Override
	public void onDestroy() {
		stopRecording();
		
		windowManager.removeView(view);
		super.onDestroy();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	protected void startRecording() {
		try {
			mediaRecorder.reset();

//			camera.setDisplayOrientation(90);

			camera.lock();
			camera.unlock();
			mediaRecorder.setCamera(camera);

			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//			mediaRecorder.setVideoFrameRate(15);
//			mediaRecorder.setOrientationHint(270);
			mediaRecorder.setOutputFile(file.getAbsolutePath());
			mediaRecorder.setPreviewDisplay(sv.getHolder().getSurface());

			mediaRecorder.setOnErrorListener(new OnErrorListener() {
				@Override
				public void onError(MediaRecorder mr, int what, int extra) {
					System.out.println(what + "-------------------");
					System.out.println(extra + "------------------");
//					stopRecording();
				}
			});

			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	protected void stopRecording() {
		if (isRun) {
			mediaRecorder.stop();
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			isRun = false;
//			
//			System.out.println("Stop Service");
//			
//			// Release the Camera
			try {
				camera.reconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			camera.release();
			
			//dt.interrupt();
			//dt = null;
		}
	}

	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

	/**
	 * 更新小悬浮窗在屏幕中的位置。
	 */
	private void updateViewPosition() {
		params.x = (int) (xInScreen - xInView);
		params.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(view, params);
	}

	@SuppressLint("NewApi")
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			int camera_id = FindFrontCamera();
			if (camera_id == -1) {
				camera_id = CameraInfo.CAMERA_FACING_BACK;
			}
			c = Camera.open(camera_id); // attempt to get a Camera
												// instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	@SuppressLint("NewApi")
	private static int FindFrontCamera() {
		int cameraCount = 0;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras(); // get cameras number

		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				// 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
				return camIdx;
			}
		}
		return -1;
	}
	
	class DrawThread extends Thread {
		private SurfaceHolder holder;
		int x = 0, y = 0;

		public DrawThread(SurfaceHolder h) {
			holder = h;
			isRun = true;
		}

		@Override
		public void run() {
			if (isRun) {
				startRecording();
			}
		}
	}
}