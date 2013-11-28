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
	private Pokemon poke;
	private SQLiteDatabase db;
	private ArrayList<Pokemon> pokedex;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pokemon);

		DBHelper dbh = DBHelper.getInstance(this);
		db = dbh.openDatabase();
		Intent i = getIntent();
		int id = i.getExtras().getInt("poke_id");

		String query = "SELECT _id, kalos, k_id, name, caught, evo_series, evo_lvl, evo_how, depth FROM pokedex";
		Cursor c = db.rawQuery(query + " WHERE _id=" + id,null);
		c.moveToFirst();
		if (!c.isAfterLast())
			poke = new Pokemon(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),
					c.getInt(4),c.getString(5),c.getInt(6),c.getString(7),c.getInt(8));
		else {
			Log.e("PokemonActivity", "Could not load pokemon.");
			return;
		}
		c.close();
		final TextView viewName = (TextView) findViewById(R.id.name);
		final TextView viewLocation = (TextView) findViewById(R.id.location);
		final TextView viewKalos = (TextView) findViewById(R.id.kalos);
		
		String tempText, textID = "";
		viewName.setText(poke.toString());
		
		tempText = "Kalos: ";
		if (poke.getKID() != 0)
			textID = "" + poke.getKID();
		if (textID.length() == 1)
			textID = "00" + textID;
		else if (textID.length() == 2)
			textID = "0" + textID;
		switch (poke.getKalos()) {
		case 1:
			tempText += "Central  #";
			break;
		case 2:
			tempText += "Coastal  #";
			break;
		case 3:
			tempText += "Mountain  #";
			break;
		default:
			tempText += "N/A";
		}
		viewKalos.setText(tempText + textID);
		viewLocation.setText(location());
		
		pokedex = new ArrayList<Pokemon>();
		c = db.rawQuery(query,null);
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				pokedex.add(new Pokemon(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),
						c.getInt(4),c.getString(5),c.getInt(6),c.getString(7),c.getInt(8)));
			} while (c.moveToNext());
		}
		c.close();
		
		ListView evoList = (ListView) findViewById(R.id.evoList);
		evoList.setTextFilterEnabled(true);
		PokedexAdapter adapter = new PokedexAdapter(this, pokedex);
		adapter.setEvo(poke.getEvo());
		evoList.setAdapter(adapter);
	}
	
	private String location() {
		String temp, xy, how;
		Boolean first = true;
		String query = "SELECT location, xy, location_how FROM locations where name=?";
		Cursor c = db.rawQuery(query,new String[] { poke.getName() });
		temp = "Locations: ";
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				if (!first)
					temp += ", ";
				temp += c.getString(0);
				xy = c.getString(1);
				how = c.getString(2);
				if (!how.isEmpty())
					temp += " - " + how;
				if (!xy.isEmpty())
					temp += " (" + xy + ")";
				first = false;
			} while (c.moveToNext());
		} else {
			temp += "N/A";
		}
		c.close();
		return temp;
	}
}
