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
		
		// Receive pokemon object to display details about
		Intent i = getIntent();
		pokemon = (Pokemon) i.getExtras().getParcelable("TEST");
	
		final TextView viewName = (TextView) findViewById(R.id.name);
		final TextView viewLocation = (TextView) findViewById(R.id.location);
		
		viewName.setText(pokemon.getName());
		viewLocation.setText(pokemon.getLocation());
		
	}
}
