package com.ticketreader.surface.camera;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;

import com.ticketreader.DisplayMessageActivity;
import com.ticketreader.R;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, AutoFocusCallback {
	private static final String LOG_TAG = "CameraSurfaceView";

	private SurfaceHolder holder;
	private Camera camera;
	
	private boolean focusState = false;
	private boolean waitingForFocus = false;
	
	private int CAMERA_ORIENTATION_ANGLE = 90;
	
	byte[] pictureFromCamera = null;
	
	MakePhotoActivity parentActivity = null;
	
	public final static String EXTRA_PICTURE_MESSAGE = "com.ticketreader.MainActivity.src.com.ticketreader.surface.camera.CameraSurfaceView";

	public CameraSurfaceView(Context context, MakePhotoActivity parentActivity) {
		super(context);

		this.parentActivity = parentActivity;
		// Initiate the Surface Holder properly
		this.holder = this.getHolder();
		this.holder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			this.camera = Camera.open();
			camera.setDisplayOrientation(CAMERA_ORIENTATION_ANGLE);
			this.camera.setPreviewDisplay(this.holder);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		if (camera == null)
			return;

		Camera.Parameters cameraParameters = camera.getParameters();

		final List<Camera.Size> sizes = cameraParameters.getSupportedPreviewSizes();
		Collections.sort(sizes, new Comparator<Camera.Size>() {
			public int compare(Camera.Size o1, Camera.Size o2) {
				return Integer.valueOf(o2.width).compareTo(o1.width);
			}
		});

		for (Camera.Size size : sizes) {
			cameraParameters.setPreviewSize(size.width, size.height);
			camera.setParameters(cameraParameters);
			Log.d(LOG_TAG, "Attempt preview size:" + size.width + "x" + size.height);
			try {
				camera.startPreview();
				Log.d(LOG_TAG, "...accepted - go along");
				cameraParameters = camera.getParameters();
				break;
			} catch (RuntimeException rx) {
				// ups, camera did not like this size
				Log.d(LOG_TAG, "...barfed, try next");
			}

		}

		camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when replaced with a new screen
		// Always make sure to release the Camera instance
		if (camera == null)
			return;
		releaseCamera();
	}

	public Camera getCamera() {
		return this.camera;
	}
	
	public void releaseCamera() {
		camera.stopPreview();
		camera.release();
		camera = null;
	}
	
	public synchronized boolean doAutofocus() {
		Log.d(LOG_TAG, "Autofocus requested");

		if (waitingForFocus)
			return false;

		waitingForFocus = true;
		
		if (camera == null)
			return false;
		
		camera.autoFocus(this);
		return true;
	}

	@Override
	public void onAutoFocus(boolean result, Camera camera) {
		Log.d(LOG_TAG, "autofocus callback received");
		focusState = result;
		waitingForFocus = false;
		
		PictureCallback jpeg = new PictureCallback() {
			@Override
			public void onPictureTaken(byte[] arg0, Camera arg1) {
				pictureTaken(arg0);
			}
		};

		camera.takePicture(null, null, jpeg);
	}
	
	public void pictureTaken(byte[] arg0) {
		pictureFromCamera = arg0;
		Intent intent = new Intent(parentActivity, DisplayMessageActivity.class);
		intent.putExtra(EXTRA_PICTURE_MESSAGE, arg0);
		parentActivity.startActivity(intent);
	}
}