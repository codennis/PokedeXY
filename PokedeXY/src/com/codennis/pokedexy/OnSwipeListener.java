package com.codennis.pokedexy;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeListener implements OnTouchListener {

	private final GestureDetector gDetector;
	private boolean startedSwiping;
	protected final View view;
	protected final Context context;
	protected float initX, initY = 0;
	protected float downTime;

	/**
	 * @param context
	 * @param v
	 */
	public OnSwipeListener(Context context, View v) {
		gDetector = new GestureDetector(context, new GestureListener());
		view = v;
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(final View v, final MotionEvent me) {
		float diffX,diffY;
		int width = v.getWidth();
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			initX = me.getX();
			initY = me.getY();
			downTime = me.getDownTime();
			startedSwiping = false;
			initDown();
		}
		if (me.getAction() == MotionEvent.ACTION_UP) {
			onUp(me, false);
			return gDetector.onTouchEvent(me);
		}
		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			v.getParent().requestDisallowInterceptTouchEvent(true);
			diffX = Math.abs(me.getX() - initX);
			diffY = Math.abs(me.getY() - initY);
			if (!startedSwiping && diffX > width/5)
				startedSwiping = true;
			if (diffX < width/5 && diffY > width/10 && !startedSwiping) {
				v.getParent().requestDisallowInterceptTouchEvent(false);
				onUp(me, true);
				return true;
			}
			/*
                    if (Math.abs(me.getY() - initY) < Math.abs(me.getX() - initX)) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                    } else if (Math.abs(me.getX() - initX) > 200) {
                            //v.getParent().requestDisallowInterceptTouchEvent(false);
                            //if (Math.abs(me.getX() - initX) > v.getWidth()/10) {
                            //        onUp(me);
                            //}
                    }
			 */
			onDrag(me);
		}
		return gDetector.onTouchEvent(me);
	}

	private final class GestureListener extends SimpleOnGestureListener {
		private static final int SWIPE_THRESHOLD = 100;
		private static final int SWIPE_VELOCITY_THRESHOLD = 100;

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent event) {
			onTap();
			return false;
		}
	}

	public boolean initDown() { return true; }
	public boolean onDrag(MotionEvent me) { return true; }
	public boolean onUp(MotionEvent me, boolean cancel) { return true; }
	public boolean onTap() { return true; }
}