package h264.com;

import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.view.View;


public class VView extends View {
	
	int width = 800;
	int height = 600;

	byte[] mPixel = new byte[width * height * 2];

	ByteBuffer buffer = ByteBuffer.wrap(mPixel);
	Bitmap VideoBit = Bitmap.createBitmap(width, height, Config.RGB_565);

	public VView(Context context) {
		super(context);
		setFocusable(true);

		int i = mPixel.length;

		for (i = 0; i < mPixel.length; i++) {
			mPixel[i] = (byte) 0x00;
		}
	}

	public static native int InitDecoder(int width, int height);

	public static native int UninitDecoder();

	public static native int DecoderNal(byte[] in, int insize, byte[] out);

	static {
		System.loadLibrary("H264Android");
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Bitmap tmpBit = Bitmap.createBitmap(mPixel, 320, 480,
		// Bitmap.Config.RGB_565);//.ARGB_8888);

		VideoBit.copyPixelsFromBuffer(buffer);// makeBuffer(data565, N));
		buffer.position(0);//将下一个读写位置置为0

		canvas.drawBitmap(VideoBit, 0, 0, null);
	}
}
