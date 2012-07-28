package com.ticketreader.surface.camera;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String LOG_TAG = "CameraSurfaceView";

	private SurfaceHolder holder;
	private Camera camera;

	public CameraSurfaceView(Context context) {
		super(context);

		// Initiate the Surface Holder properly
		this.holder = this.getHolder();
		this.holder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			// Open the Camera in preview mode
			this.camera = Camera.open();
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
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	public Camera getCamera() {
		return this.camera;
	}
}