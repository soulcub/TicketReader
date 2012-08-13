package com.ticketreader.surface.camera;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MakePhotoActivity extends Activity {

	private static final String LOG_TAG = "MakePhotoActivity";
	
	private CameraSurfaceView cameraSurfaceView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context applicationContext = getApplicationContext();

		LinearLayout lLayout = new LinearLayout(applicationContext);
		cameraSurfaceView = new CameraSurfaceView(applicationContext, this);
		
		lLayout.addView(cameraSurfaceView);
		setContentView(lLayout);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    boolean result = super.onTouchEvent(event);
		cameraSurfaceView.doAutofocus();
		return result;
	}
	
	@Override
	protected void onDestroy() {
		cameraSurfaceView.releaseCamera();
		super.onDestroy();
	}
	
}