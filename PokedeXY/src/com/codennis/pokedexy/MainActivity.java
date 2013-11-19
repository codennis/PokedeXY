package com.codennis.pokedexy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private static final String POKE = "Pokemon";
	private ArrayList<Pokemon> pokedex = new ArrayList<Pokemon>();
	private SQLiteDatabase newDB;
	public View row;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		DBHelper dbh = DBHelper.getInstance(this);
		newDB = dbh.openDatabase();
		storeData();

		displayList();
		
	}
	
	// Update database when pause or close
	@Override
	protected void onPause() {
		for (Pokemon poke:pokedex) {
			Log.i("MAIN",poke.getName() + (poke.getCaught() ? 1 : 0));
			ContentValues cv = new ContentValues();
			cv.put("caught", (poke.getCaught() ? 1 : 0));
			newDB.update("pokedex", cv, "_id = " + poke.getNID(),null);
		}
		super.onPause();
	}

	private void storeData() {
		Cursor c = newDB.rawQuery("SELECT _id, name, location, caught FROM pokedex",null); //"name asc");
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				Log.i("storing", c.getInt(0) + c.getString(1) + c.getInt(3));
				pokedex.add(new Pokemon(c.getInt(0),0,0,0,c.getString(1),c.getString(2),c.getInt(3)));
			} while (c.moveToNext());
		}
		c.close();
	}
	
	private void displayList() {
		final ListView pokedexList = (ListView) findViewById(R.id.nationalDex);
		pokedexList.setAdapter(new PokedexAdapter(this, pokedex));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}


