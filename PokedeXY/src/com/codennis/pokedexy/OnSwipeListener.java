package com.codennis.pokedexy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeListener implements OnTouchListener {
	
	private final GestureDetector gDetector;
	protected final View view;
	protected final Context context;
	
	public OnSwipeListener(Context context, View v) {
		gDetector = new GestureDetector(context, new GestureListener());
		view = v;
		this.context = context;
	}

	public boolean onTouch(final View v, final MotionEvent me) {
		return gDetector.onTouchEvent(me);
	}
	
	private final class GestureListener extends SimpleOnGestureListener {
		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;
		
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
	    @Override
	    public boolean onSingleTapUp(MotionEvent event) {
	    	onTap();
	    	return false;
	    }
	    
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velX, float velY) {
			boolean result = false;
			
			try {
				float diffX = e2.getX() - e1.getX();
				Log.i("SWIPE", "" + e1.getX() + " to " + e2.getX());
				if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velX) > SWIPE_VELOCITY_THRESHOLD) {
					if (diffX > 0) {
						onSwipeRight();
					} else {
						onSwipeLeft();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}
	}

    public boolean onTap() { return true; }
	public boolean onSwipeRight() { return true; };
	public boolean onSwipeLeft() { return true; };
}
