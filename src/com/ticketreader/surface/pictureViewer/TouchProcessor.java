package com.ticketreader.surface.pictureViewer;

import com.ticketreader.utils.Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerProperties;
import android.widget.ImageView;

@TargetApi(14)
public class TouchProcessor {
	private static final String LOG_TAG = "TouchProcessor";
	
	private static final int ONE_TOUCH = 1;
	private static final int MULTI_TOUCH = 2;
	
	private static final float MAX_SCALE = 4f;
	private static final float MIN_SCALE = 1f;
	
	private static final int scaleSpeed = 300;
	
	private static final int MAX_NUMBER_OF_POINT = 2;
	
	private float[] firstTouchX = new float[MAX_NUMBER_OF_POINT];
	private float[] firstTouchY = new float[MAX_NUMBER_OF_POINT];
	private float firstTouchImageX = 0;
	private float firstTouchImageY = 0;
	private float currentX = 0;
	private float currentY = 0;
	private float firstTouchDistance = 0;
	private float firstScale = 0;
	
	private boolean[] touches = new boolean[MAX_NUMBER_OF_POINT];
	
	private float[] x = new float[MAX_NUMBER_OF_POINT];
	private float[] y = new float[MAX_NUMBER_OF_POINT];
	
	private boolean oneTouchPerformed = false;
	private boolean multiTouchPerformed = false;
	
	@SuppressLint("NewApi")
	public void process(ImageView imageView, MotionEvent event) {
		int pointsCount = event.getPointerCount();
		int action = event.getAction();
		
		switch (pointsCount) {
		case ONE_TOUCH:
			Log.d(LOG_TAG, "ONE_TOUCH case");
			touchProcess(imageView, pointsCount, action, event);
			break;
		case MULTI_TOUCH:
			Log.d(LOG_TAG, "MULTI_TOUCH case");
			multiTouchProcess(imageView, pointsCount, action, event);
			break;
		default:
			return;
		}
	}
	
	@SuppressLint("FloatMath")
	private void multiTouchProcess(ImageView imageView, int pointsCount, int action, MotionEvent event) {
		float scale = 1;
		for (int i = 0; i < pointsCount; i++) {
			int id = event.getPointerId(i);
			if (id < MAX_NUMBER_OF_POINT) {
				x[id] = (int)event.getX(i);
				y[id] = (int)event.getY(i);
			}
			
			PointerProperties outPointerProperties = new PointerProperties();
			
			switch(action) {
			case MotionEvent.ACTION_POINTER_1_DOWN:
			case MotionEvent.ACTION_POINTER_2_DOWN:
				touches[id] = true;
				multiTouchPerformed = true;
				oneTouchPerformed = false;
				firstScale = imageView.getScaleX();
				firstTouchDistance = (float) Math.sqrt( (x[1] - x[0])*(x[1] - x[0]) + (y[1] - y[0])*(y[1] - y[0]) );
				break;
			case MotionEvent.ACTION_MOVE:
				scale = firstScale;
				float pointsDistance = (float) Math.sqrt( (x[1] - x[0])*(x[1] - x[0]) + (y[1] - y[0])*(y[1] - y[0]) );
				float distanceDiff = firstTouchDistance - pointsDistance;
				
				scale -= distanceDiff / scaleSpeed;
				
				if (Utils.isInRange(MAX_SCALE, MIN_SCALE, scale)) {
					imageView.setScaleX(scale);
					imageView.setScaleY(scale);
				}
				break;
			case MotionEvent.ACTION_POINTER_1_UP:
			case MotionEvent.ACTION_POINTER_2_UP:
				touches[id] = false;
				oneTouchPerformed = false;
				if (Utils.equalsAll(touches, false))
					multiTouchPerformed = false;
				firstTouchDistance = 0;
				break;
			default:
				break;
			}
		}
	}
	
	private void touchProcess(ImageView imageView, int pointsCount, int action, MotionEvent event) {
		if (multiTouchPerformed)
			return;
		
		float diffX;
		float diffY;

		int id = event.getPointerId(0);
		x[id] = (int)event.getX();
		y[id] = (int)event.getY();
		switch(action) {
		case MotionEvent.ACTION_DOWN:
			oneTouchPerformed = true;
			firstTouchX[id] = x[id];
			firstTouchY[id] = y[id];
			firstTouchImageX = imageView.getX();
			firstTouchImageY = imageView.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (!oneTouchPerformed)
				break;
			currentX = firstTouchImageX;
			currentY = firstTouchImageY;
			
			diffX = firstTouchX[id] - x[id];
			diffY = firstTouchY[id] - y[id];
			
			currentX -= diffX;
			currentY -= diffY;
			if (currentX <= 0)
				currentX = 0;
			if (currentY <= 0)
				currentY = 0;

			break;
		case MotionEvent.ACTION_UP:
			if (!oneTouchPerformed)
				return;
			oneTouchPerformed = false;
			firstTouchX[id] = currentX;
			firstTouchY[id] = currentY;
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}
		
		imageView.setX(currentX);
		imageView.setY(currentY);
	}
}
