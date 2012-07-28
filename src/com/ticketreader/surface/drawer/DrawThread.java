package com.ticketreader.surface.drawer;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class DrawThread extends Thread {
	boolean flag;
	SurfaceHolder myHolder;
	DrawSurface myDraw;

	public DrawThread(SurfaceHolder holder, DrawSurface drawMain) {
		myHolder = holder;
		myDraw = drawMain;
	}

	public void setFlag(boolean myFlag) {
		flag = myFlag;
	}

	public void run() {
		Canvas canvas = null;
		while (flag) {
			try {
				canvas = myHolder.lockCanvas(null);
				myDraw.onDraw(canvas);
			} finally {
				if (canvas != null) {
					myHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}