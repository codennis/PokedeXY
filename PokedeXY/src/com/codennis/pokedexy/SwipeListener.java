package com.codennis.pokedexy;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class SwipeListener extends SimpleOnGestureListener implements OnTouchListener {
    private ViewHolder viewHolder;
    private Context context;
    private GestureDetector gDetector;
    private SQLiteDatabase newDB;
    
    static final int SWIPE_MIN_DISTANCE = 80;
    static final int SWIPE_MAX_OFF_PATH = 250;
    static final int SWIPE_THRESHOLD_VELOCITY = 2000;

    private Pokemon poke = null;
    
    public SwipeListener() {
     super();
    }
   /*
    public SwipeListener(Context context) {
    	this(context, null);
    	DBHelper dbh = DBHelper.getInstance(context);
		newDB = dbh.openDatabase();
    }
   
    public SwipeListener(Context context, GestureDetector gDetector) {
   
     if (gDetector == null)
    	 gDetector = new GestureDetector(context, this);
   
     this.context = context;
     this.gDetector = gDetector;
    }
    
    public void setPoke(Pokemon poke) {
    	this.poke = poke;
    }
    
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
    	Log.i("LISTENER", "TAPPED" + poke.getName() + " " + poke.getLocation());
    	Intent i = new Intent(context, PokemonActivity.class);
    	Bundle b = new Bundle();
    	b.putParcelable("TEST", this.poke);
    	i.putExtras(b);
    	context.startActivity(i);
    	return super.onSingleTapUp(event);
    }

	public boolean onDown(MotionEvent e) {
		return true;
	}
	
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {
    	Log.i("LISTENER", "FLING " + vX + " " + vY);
    	
    	if (Math.abs(vX) < SWIPE_THRESHOLD_VELOCITY)
    	{
    		Log.i("LISTENER", "TOO SLOW");
    		return false;
    	}
    	if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
	        viewHolder.setCaught(false);
	        Log.i("LISTENER", "UNCAUGHT");
    	}
    	else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
            viewHolder.setCaught(true);
	        Log.i("LISTENER", "CAUGHT");
    	}
        
    	return super.onFling(e1,e2,vX,vY);
    }

	public boolean onTouch(final View v, MotionEvent event) {
		boolean bool;
        viewHolder = ((ViewHolder) v.getTag());
        bool = gDetector.onTouchEvent(event);
        v.setBackgroundColor(viewHolder.getColor());rn true;
	}
	
	
	public GestureDetector getDetector() {
		return gDetector;
	}
	*/
}

