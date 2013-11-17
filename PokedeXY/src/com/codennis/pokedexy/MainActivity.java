package com.codennis.pokedexy;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ArrayList<Pokemon> pokedex = new ArrayList<Pokemon>();
	private SQLiteDatabase newDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Stuff with database

		pokedex.add(new Pokemon(1,0,0,0,"Test",""));
		pokedex.add(new Pokemon(2,0,0,0,"Test2",""));
		pokedex.add(new Pokemon(3,0,0,0,"Test3",""));
		pokedex.add(new Pokemon(4,0,0,0,"TestA",""));
		pokedex.add(new Pokemon(5,0,0,0,"TestB",""));
		
		displayList();
		
	}

	private void displayList() {
		final ListView pokedexList = (ListView) findViewById(R.id.nationalDex);
		pokedexList.setAdapter(new PokedexAdapter(this, pokedex));
		
		pokedexList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = pokedexList.getItemAtPosition(position);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
