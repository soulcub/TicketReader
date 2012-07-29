package com.ticketreader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ticketreader.surface.camera.CameraSurfaceView;

public class DisplayMessageActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the message from the intent
		Intent intent = getIntent();
		String message = null;
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		byte[] picture = null;
		picture = intent.getByteArrayExtra(CameraSurfaceView.EXTRA_PICTURE_MESSAGE);
		// Create the text view
		if (message != null) {
			TextView textView = new TextView(this);
			textView.setTextSize(40);
			textView.setText(message);
	
			setContentView(textView);
		}
		
		if (picture != null) {
			ImageView imageView = new ImageView(this);
			Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 1, 1);
			imageView.setImageBitmap(bitmap);
			
			setContentView(imageView);
		}
	}
}