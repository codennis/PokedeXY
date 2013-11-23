package com.codennis.pokedexy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	private static final String POKE = "Pokemon";
	private ArrayList<Pokemon> pokedex = new ArrayList<Pokemon>();
	private SQLiteDatabase newDB;
	private PokedexAdapter adapter;
	public View row;
	ListView pokedexList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		DBHelper dbh = DBHelper.getInstance(this);
		newDB = dbh.openDatabase();
		
		storeData();
		pokedexList = (ListView) findViewById(R.id.nationalDex);
		displayList();
		
		
		// Temp filter testing
		final Button button = (Button) findViewById(R.id.button2);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("MAIN", "CLICKED");
				adapter.toggleCaught();
				adapter.getFilter().filter(null);
				pokedexList.setAdapter(adapter);

				Log.i("POKE", "POKEDEX");
				for (Pokemon poke:pokedex) {
					Log.i("POKEDEX",poke.getNID() + poke.getName());
				}
			}
		});
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

	@Override
	protected void onResume() {
		DBHelper dbh = DBHelper.getInstance(this);
		newDB = dbh.openDatabase();
		storeData();
		displayList();
		Log.i("RESUME","RESUMING");
		super.onResume();
	}
	// Initial loading of database
	private void storeData() {
		Cursor c = newDB.rawQuery("SELECT _id, name, location, caught, evo_id FROM pokedex",null); //"name asc");
		c.moveToFirst();
		pokedex.clear();
		if (!c.isAfterLast()) {
			do {
				//Log.i("MAIN STORING", c.getInt(0) + c.getString(1) + c.getInt(3));
				pokedex.add(new Pokemon(c.getInt(0),0,0,0,c.getString(1),c.getString(2),c.getInt(3),c.getInt(4)));
			} while (c.moveToNext());
		}
		c.close();
	}
	
	// Call adapter to display listview of pokedex
	private void displayList() {
		adapter = new PokedexAdapter(this,pokedex);
		pokedexList.setTextFilterEnabled(true);
		pokedexList.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}


