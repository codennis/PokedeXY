package com.codennis.pokedexy;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ArrayList<Pokemon> pokedex = new ArrayList<Pokemon>();
	private SQLiteDatabase newDB;
	public View row;
	public OnTouchListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DBHelper dbh = new DBHelper(this, "test");
		newDB = dbh.openDatabase();
		storeData();
		
		listener = new View.OnTouchListener() {
			private int padding = 0;
		    private int initialx = 0;
		    private int currentx = 0;
		    private  ViewHolder viewHolder;

			
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
		};
		
		// Stuff with database
		
		/*
		pokedex.add(new Pokemon(1,0,0,0,"Test",""));
		pokedex.add(new Pokemon(2,0,0,0,"Test2",""));
		pokedex.add(new Pokemon(3,0,0,0,"Test3",""));
		pokedex.add(new Pokemon(4,0,0,0,"TestA",""));
		pokedex.add(new Pokemon(5,0,0,0,"TestB",""));
		*/
		displayList();
		
	}

	private void storeData() {
		Cursor c = newDB.query("pokedex", new String[] {"_id", "name"}, null, null, null, null, "name asc");
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				Log.i("storing", c.getInt(0) + c.getString(1));
				pokedex.add(new Pokemon(c.getInt(0),0,0,0,c.getString(1),""));
			} while (c.moveToNext());
		}
		c.close();
	}
	
	private void displayList() {
		final ListView pokedexList = (ListView) findViewById(R.id.nationalDex);
		pokedexList.setAdapter(new PokedexAdapter(this, pokedex, listener));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

class ViewHolder {
    protected TextView text;
    protected ImageView icon;
    protected int position;
    protected Pokemon pokemon;
    private int color;
    private int imageid;
    boolean caught;
	TextView txtName;
	TextView txtNumber;
	
    public ViewHolder()
    {
        position = 0;
        //imageid = R.drawable.bullet_go;
        color = 0xFFFFFFFF;
    }
    public int getColor() {
        return color;
    }
    public int getImageId() {
        return imageid;
    }
    public void setCaught(boolean caught) {
    	pokemon.setCaught(caught);
    	if (caught)
    		color = 0xFF00FF00;
    	else
    		color = 0xFFFF0000;  
    }
}

