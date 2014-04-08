package com.cyeam.cInterphone.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cyeam.cInterphone.R;
import com.cyeam.cInterphone.service.RecordService;

public class InterphoneActivity extends Activity {
	
	private static final String TAG = "[cInterhpone|InterphoneActivity]";
	
	private Intent recordServiceIntent;
	
	private Intent playServiceIntent;
	
	private SurfaceView sv;
//	private MediaPlayer mediaPlayer;
	private boolean isRun;
	private YouThread yThread;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interphone);
        
//		/* 取得Intent中的Bundle对象 */
//	    Bundle bundle = this.getIntent().getExtras();
//	    
//	    /* 取得Bundle对象中的数据 */
//	    Contact contact = (Contact) bundle.getSerializable("contact");
//	    
//	    ActionBar ab = getActionBar();
//	    ab.setTitle(contact.getName());
		
		Button stopButton = (Button)findViewById(R.id.camera_stop);
		ImageButton switchButton = (ImageButton)findViewById(R.id.camera_switch);
		ImageButton muteButton = (ImageButton)findViewById(R.id.camera_mute);
        
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        
        stopButton.setLayoutParams(new LinearLayout.LayoutParams(width / 2, width / 10));
        switchButton.setLayoutParams(new LinearLayout.LayoutParams(width / 4, width / 10));
        muteButton.setLayoutParams(new LinearLayout.LayoutParams(width / 4, width / 10));
        
        sv = (SurfaceView)findViewById(R.id.you_view);
        sv.getHolder().addCallback(new Callback() {

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
//				isRun = true;
//				yThread = new YouThread(holder);
//				yThread.start();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				isRun = false;
			}
		});
		
//        mediaPlayer = new MediaPlayer();        
	}
	

	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		recordServiceIntent = new Intent(this, RecordService.class);
		startService(recordServiceIntent);		
		
		super.onResume();
	}
	

	@Override
	protected void onPause() {
		stopService(recordServiceIntent);
//		stopPlaying();
		super.onPause();
	}
	

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void stopCamera(View view) {
		super.onBackPressed();// Click back button.
	}
	
	public void switchCamera(View view) {
		System.out.println("****************switch");
	}
	
	public void muteCamera(View view) {
		System.out.println("***************mute");
	}

	protected void startPlaying() {
//		try {
//			mediaPlayer.reset();
//			mediaPlayer.setDataSource("/sdcard/video1.mp4");
//			mediaPlayer.setDisplay(sv.getHolder());
//			mediaPlayer.setOnErrorListener(new OnErrorListener() {
//				
//				@Override
//				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
//					mediaPlayer.release();
//					mediaPlayer = null;
//					isRun = false;
//					return false;
//				}
//			});
//			
//			mediaPlayer.prepare();
//			mediaPlayer.start();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	protected void stopPlaying() {
//		if (isRun) {
//			mediaPlayer.stop();
//			mediaPlayer.release();
//			isRun = false;
//		}
	}
	
	class YouThread extends Thread {
		private SurfaceHolder holder;
		
		public YouThread(SurfaceHolder h) {
			holder = h;
			isRun = true;
		}
		
		@Override
		public void run() {
			if (isRun) {
				startPlaying();
			}
		}
	}

}
