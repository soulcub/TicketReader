package com.ticketreader.surface.camera;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.LinearLayout;

public class MakePhotoActivity extends Activity implements Camera.AutoFocusCallback, Camera.PreviewCallback {

	private static final String LOG_TAG = "MakePhotoActivity";

	Camera camera;

	boolean cameraActive = false;
	boolean previewActive = false;
	private Camera.Size previewSize;
	public static final int MIN_PREVIEW_WIDTH = 480;

	boolean focusState;

	boolean waitingForFocus = false;
	boolean waitingForPreview = false;

	byte[] previewImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context applicationContext = getApplicationContext();

		LinearLayout lLayout = new LinearLayout(applicationContext);
		CameraSurfaceView cameraSurfaceView = new CameraSurfaceView(applicationContext);
		lLayout.addView(cameraSurfaceView);
		setContentView(lLayout);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (previewActive) {
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;
			cameraActive = false;
			previewActive = false;
		}
		super.onDestroy();
	}

	public Camera.Size getPreviewSize() {
		return previewSize;
	}

	public synchronized void start(SurfaceHolder holder) throws IOException {
		Log.d(LOG_TAG, " starting requested ");
		if (!cameraActive) {
			Log.d(LOG_TAG, " starting");
			camera = Camera.open();

			startPreview(holder);
			cameraActive = true;
			waitingForFocus = false;
			waitingForPreview = false;

			Log.d(LOG_TAG, " started");
		}
	}

	public synchronized void stop() {
		if (cameraActive) {
			if (camera != null) {
				stopPreview();
				camera.release();
				camera = null;
			}
			cameraActive = false;
		}
	}

	private void startPreview(SurfaceHolder holder) throws IOException {
		if (!previewActive) {
			Log.d(LOG_TAG, " starting preview ");

			Camera.Parameters cameraParameters = camera.getParameters();
			Log.d(LOG_TAG, "Camera flash modes:" + cameraParameters.getSupportedFlashModes());
			Log.d(LOG_TAG, "Camera autofocus:" + cameraParameters.getSupportedFocusModes());
			Log.d(LOG_TAG, "Camera preview formats:" + cameraParameters.getSupportedPreviewFormats());
			Log.d(LOG_TAG, "Camera scene modes:" + cameraParameters.getSupportedSceneModes());
			Log.d(LOG_TAG, "Camera white balance modes:" + cameraParameters.getSupportedWhiteBalance());

			camera.setPreviewDisplay(holder);

			previewSize = cameraParameters.getPreviewSize();

			Log.d(LOG_TAG, " accepted default preview size on the spot:" + previewSize.width + "x" + previewSize.height);
			camera.startPreview();

			previewActive = true;
		}
	}

	private void stopPreview() {
		if (previewActive) {
			camera.stopPreview();
			previewActive = false;
			previewSize = null;
		}
	}

	public Camera getCamera() {
		return camera;
	}

	public boolean isCameraActive() {
		return cameraActive;
	}

	public synchronized boolean doAutofocus() {

		Log.d(LOG_TAG, "autofocus requested");
		if (!isCameraActive())
			return false;

		if (waitingForFocus)
			return false;

		focusState = false;

		camera.autoFocus(this);

		try {
			wait();
		} catch (InterruptedException e) {
			focusState = false;
		}
		waitingForFocus = false;
		Log.d(LOG_TAG, "autofocus ready");
		return focusState;
	}

	public synchronized byte[] getPreviewFrame() {
		Log.d(LOG_TAG, "preview frame requested");
		if (!isCameraActive())
			return null;

		if (waitingForPreview)
			return null;

		waitingForPreview = true;

		previewImage = null;
		camera.setOneShotPreviewCallback(this);
		try {
			wait();
		} catch (InterruptedException e) {
			return previewImage = null;
		}
		waitingForPreview = false;
		return previewImage;
	}

	public synchronized void onAutoFocus(boolean b, Camera camera) {
		Log.d(LOG_TAG, "autofocus callback received");
		focusState = b;
		notify();
	}

	public synchronized void onPreviewFrame(byte[] bytes, Camera camera) {
		Log.d(LOG_TAG, "preview frame received");
		previewImage = bytes;
		notify();
	}
}