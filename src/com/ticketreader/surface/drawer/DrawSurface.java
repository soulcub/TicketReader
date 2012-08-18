package com.ticketreader.surface.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.ticketreader.R;

public class DrawSurface extends SurfaceView implements Callback {

	private DrawThread myThread;
	private Bitmap bmp;
	private SurfaceHolder holder;
	private Paint paint;
	private int x = 50;
	private int y = 50;
	int newX = 50;
	int newY = 50;
	int diffX = newX - x;
	int diffY = newY - y;
	int incrX = -1;
	int incrY = -1;

	public DrawSurface(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(this);
		paint = new Paint();
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	public void surfaceCreated(SurfaceHolder holder) {
		myThread = new DrawThread(holder, this);
		myThread.setFlag(true);
		myThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			newX = (int) event.getX();
			newY = (int) event.getY();
			diffX = newX - x;
			diffY = newY - y;
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (canvas == null)
			return;
		canvas.drawColor(Color.DKGRAY);

		super.onDraw(canvas);
		diffX = newX - x;
		diffY = newY - y;

		if (diffX < 0) {
			incrX = 1;
		} else if (diffX > 0) {
			incrX = 0;
		} else {
			incrX = -1;
		}

		if (diffY < 0) {
			incrY = 1;
		} else if (diffY > 0) {
			incrY = 0;
		} else {
			incrY = -1;
		}

		if (diffX != 0) {
			if (incrX == 0) {
				x++;
			} else if (incrX == 1) {
				x--;
			}
		}
		if (diffY != 0) {
			if (incrY == 0) {
				y++;
			} else if (incrY == 1) {
				y--;
			}
		}
		canvas.drawBitmap(bmp, x, y, paint);

	}
}