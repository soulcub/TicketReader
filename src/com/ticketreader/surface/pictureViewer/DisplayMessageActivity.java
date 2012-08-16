package com.ticketreader.surface.pictureViewer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.ticketreader.MainActivity;
import com.ticketreader.R;
import com.ticketreader.R.id;
import com.ticketreader.R.layout;
import com.ticketreader.surface.camera.CameraSurfaceView;
import com.ticketreader.utils.Utils;

@SuppressLint("NewApi")
public class DisplayMessageActivity extends Activity {

	private static final String LOG_TAG_POINT = "Point Debug:";
	private ImageView imageView = null;
	private TouchProcessor touchProcessor = new TouchProcessor();
	
	private static final float DEFAULT_ROTATION = 90f;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.previewer);
		imageView = (ImageView) findViewById(R.id.ImageViewId);
		imageView.setRotation(DEFAULT_ROTATION);
		
		// Get the message from the intent
		Intent intent = getIntent();
		String message = null;
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		if (intent.getStringExtra(CameraSurfaceView.EXTRA_PICTURE_MESSAGE) != null) {
			if (Utils.image != null) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(Utils.image, 0, Utils.image.length);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean result = super.onTouchEvent(event);
		
		touchProcessor.process(imageView, event);
		
		return result;
	}
}