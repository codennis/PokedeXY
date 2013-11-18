package com.codennis.pokedexy;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PokemonActivity extends Activity {
	private Pokemon pokemon;
	private SQLiteDatabase newDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pokemon);

		/*
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int value = Integer.parseInt(extras.getString("pokeID")) + 1;
			Log.i("POKEMON", ""+value);
			Cursor c = newDB.rawQuery("SELECT _id, name, location, caught FROM pokedex WHERE _id = " + value,null); //"name asc");
			c.moveToFirst();
			if (!c.isAfterLast()) {
				pokemon = new Pokemon(c.getInt(0),0,0,0,c.getString(1),c.getString(2), c.getInt(3));
			}
			c.close();
		}
		*/
		/*
		Intent i = getIntent();
		Log.i("POKEMON", i.toString());
		Log.i("POKEMON", i.getExtras().toString());
		pokemon = (Pokemon) i.getExtras().getParcelable("TEST");
		//Log.i("POKEMON",pokemon.getNID() + " " + pokemon.getLocation() + " " + pokemon.getName());
	
		final TextView viewName = (TextView) findViewById(R.id.name);
		final TextView viewLocation = (TextView) findViewById(R.id.location);
		
		viewName.setText(pokemon.getName());
		viewLocation.setText(pokemon.getLocation());
		*/
	}
}
