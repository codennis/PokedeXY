package com.codennis.pokedexy;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OnSwipeListener implements OnTouchListener {
	
	private final GestureDetector gDetector;
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
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			initX = me.getX();
			initY = me.getY();
			downTime = me.getDownTime();
			initDown();
		}
		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			if (Math.abs(me.getY() - initY) < Math.abs(me.getX() - initX)) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				onDrag(me);
			} else {
				v.getParent().requestDisallowInterceptTouchEvent(false);
			}
		}
		if (me.getAction() == MotionEvent.ACTION_UP) {
			onUp(me);
		}
		return gDetector.onTouchEvent(me);
	}

    public boolean onDoubleTap(MotionEvent e) { return true; }
    public boolean onDoubleEvent(MotionEvent e) { return true; }
    public boolean onDown(MotionEvent e) { return true; }
    public boolean onLongPress(MotionEvent e) { return true; }
    public boolean onScroll(MotionEvent e) { return true; }
    public boolean onShowPress(MotionEvent e) { return true; }
    public boolean onSingleTapConfirmed(MotionEvent e) { return true; }
	
    
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
	public boolean onUp(MotionEvent me) { return true; }
    public boolean onTap() { return true; }
}
