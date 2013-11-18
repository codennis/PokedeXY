package com.codennis.pokedexy;

import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeListener implements OnTouchListener {
    private int padding = 0;
    private int initialx = 0;
    private int currentx = 0;
    private  ViewHolder viewHolder;
    
	@SuppressWarnings("deprecation")
	private final GestureDetector gestDetector = new GestureDetector(new GestureListener());
	
	public boolean onTouch(final View v, MotionEvent event) {
        Log.i("LISTENER", "TOUCHED");
		if ( event.getAction() == MotionEvent.ACTION_DOWN)
	    {
	        padding = 0;
	        initialx = (int) event.getX();
	        currentx = (int) event.getX();
	        viewHolder = ((ViewHolder) v.getTag());
	    }
	    if ( event.getAction() == MotionEvent.ACTION_MOVE)
	    {
	        currentx = (int) event.getX();
	        padding = currentx - initialx;
	    }
	    
	    if ( event.getAction() == MotionEvent.ACTION_UP || 
	         event.getAction() == MotionEvent.ACTION_CANCEL)
	    {
	        padding = 0;
	        initialx = 0;
	        currentx = 0;
	    }
	    
	    if(viewHolder != null)
	    {
	        if(padding == 0)
	        {
	            v.setBackgroundColor(0xFF000000 );  
	        }
	        if(padding > 75)
	        {
	            viewHolder.setCaught(true);
	            Log.i("LISTENER", "CAUGHT");
	        }
	        if(padding < -75)
	        {
	            viewHolder.setCaught(false);
	            Log.i("LISTENER", "UNCAUGHT");
	        }
	        v.setBackgroundColor(viewHolder.getColor());
	        v.setPadding(padding, 0,0, 0);
	    }
	    return true;
	}
	//	return gestDetector.onTouchEvent(me);
	//}
	
	private final class GestureListener extends SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_MAX_OFF_PATH = 250;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			boolean result = false;
			
			try {
				float diffX = e2.getX() - e1.getX();
				if (Math.abs(diffX) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
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

	public void onSwipeRight() {}
    public void onSwipeLeft() {}

}

