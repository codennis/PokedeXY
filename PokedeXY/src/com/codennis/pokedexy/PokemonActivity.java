package com.codennis.pokedexy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class PokemonActivity extends Activity {
	private Pokemon pokemon;
	private SQLiteDatabase newDB;
	private ArrayList<Pokemon> pokedex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pokemon);

		DBHelper dbh = DBHelper.getInstance(this);
		newDB = dbh.openDatabase();
		// Receive pokemon object to display details about
		Intent i = getIntent();
		//pokemon = (Pokemon) i.getExtras().getParcelable("TEST");
		int id = i.getExtras().getInt("poke_id");
		Cursor c = newDB.rawQuery("SELECT _id, name, location, caught, evo_id FROM pokedex WHERE _id="+id,null);
		c.moveToFirst();
		if (!c.isAfterLast())
			pokemon = new Pokemon(c.getInt(0),0,0,0,c.getString(1),c.getString(2),c.getInt(3),c.getInt(4));
		else {
			Log.e("PokemonActivity", "Could not load pokemon.");
			return;
		}
		c.close();
		final TextView viewName = (TextView) findViewById(R.id.name);
		final TextView viewLocation = (TextView) findViewById(R.id.location);
		
		viewName.setText(pokemon.getName());
		viewLocation.setText(pokemon.getLocation());
		
		pokedex = new ArrayList<Pokemon>();
		c = newDB.rawQuery("SELECT _id, name, location, caught, evo_id FROM pokedex",null);
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				pokedex.add(new Pokemon(c.getInt(0),0,0,0,c.getString(1),c.getString(2),c.getInt(3),c.getInt(4)));
			} while (c.moveToNext());
		}
		c.close();
		
		ListView evoList = (ListView) findViewById(R.id.evoList);
		evoList.setTextFilterEnabled(true);
		PokedexAdapter adapter = new PokedexAdapter(this, pokedex, pokemon.getEvo());
		adapter.getFilter().filter(null);
		evoList.setAdapter(adapter);
		/*
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("POKEMON", "CLICKED");
				Pokemon poke = new Pokemon(10,0,0,0,"Testamon","testloc", 0);
				Context context = v.getContext();
		    	Intent i = new Intent(context, PokemonActivity.class);
		    	Bundle b = new Bundle();
		    	b.putParcelable("TEST", poke);
		    	i.putExtras(b);
		    	context.startActivity(i);
				
			}
		});
		*/
	}
}
