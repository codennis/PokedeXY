package com.codennis.pokedexy;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PokemonFrag extends Fragment {
	private Pokemon poke;
	private SQLiteDatabase db;
	private ArrayList<Pokemon> pokedex;
	private ArrayList<Trip<String, String, String>> locationList;
	private ArrayList<Trip<String, String, String>> safariList;
	private PokedexAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.pokemon, null);

		DBHelper dbh = DBHelper.getInstance(this.getActivity());
		db = dbh.getDb();
		Intent i = this.getActivity().getIntent();
		int id = i.getExtras().getInt("poke_id");
		String query = "SELECT _id, kalos, k_id, name, caught, evo_series, evo_lvl, evo_how, depth FROM pokedex";
		Cursor c = db.rawQuery(query + " WHERE _id=" + id,null);
		c.moveToFirst();
		if (!c.isAfterLast())
			poke = new Pokemon(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),
					c.getInt(4),c.getString(5),c.getInt(6),c.getString(7),c.getInt(8));
		else {
			return v;
		}
		c.close();

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
		
		updateDetails(v);
		updateHeader(v);
		
		ListView evoList = (ListView) v.findViewById(R.id.evoList);
		adapter = new PokedexAdapter(this.getActivity(), pokedex);
		adapter.setEvo(poke.getEvoSeries());
		adapter.setID(poke.getNID());
		adapter.updateList();
		evoList.setTextFilterEnabled(true);
		evoList.setAdapter(adapter);
		
		ImageView img = (ImageView) v.findViewById(R.id.pokeImg);
		String imgName = "_"+poke.getNIDString();
		int resID = getResources().getIdentifier(imgName, "drawable", getActivity().getPackageName());
		img.setImageResource(resID);
		
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null)
			adapter.updatePokedex();
	}
	
	private void updateDetails(View view) {

		final TextView viewName = (TextView) view.findViewById(R.id.name);
		final TextView viewKalos = (TextView) view.findViewById(R.id.kalos);
		final ListView viewLocation = (ListView) view.findViewById(R.id.locationList);
		final ListView viewSafari = (ListView) view.findViewById(R.id.safariList);
		
		String tempText, textID = "";
		viewName.setText(poke.getNIDString() + " " + poke.getName());
		
		switch (poke.getKalos()) {
		case 1:
			tempText = "Central  ";
			break;
		case 2:
			tempText = "Coastal  ";
			break;
		case 3:
			tempText = "Mountain ";
			break;
		default:
			tempText = "Kalos: N/A";
		}
		viewKalos.setText(tempText + poke.getKIDString());
		
		locationList = new ArrayList<Trip<String, String, String>>();
		TriplesAdapter adapter = new TriplesAdapter(this.getActivity());
		updateLocations();
		adapter.addAll(locationList);
		viewLocation.setAdapter(adapter);
		
		safariList = new ArrayList<Trip<String,String,String>>();
		TriplesAdapter safAdapter = new TriplesAdapter(this.getActivity());
		updateSafari();
		safAdapter.addAll(safariList);
		viewSafari.setAdapter(safAdapter);
	}
	
	private void updateLocations() {
		final ArrayList<Trip<String, String, String>> list = new ArrayList<Trip<String, String, String>>();
		String query = "SELECT location, location_how, xy FROM locations WHERE name=?";
		Cursor c = db.rawQuery(query,new String[] { poke.getName() });
		locationList.clear();
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				locationList.add(new Trip<String, String, String>(c.getString(0), c.getString(1), c.getString(2)));
			} while (c.moveToNext());
		} else {
			locationList.add(new Trip<String, String, String>("N/A", "", ""));
		}
		c.close();
	}
	
	private void updateSafari() {
		final ArrayList<Trip<String, String, String>> list = new ArrayList<Trip<String, String, String>>();
		String query = "SELECT safari, slot FROM safari WHERE name=?";
		Cursor c = db.rawQuery(query,new String[] { poke.getName() });
		safariList.clear();
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				safariList.add(new Trip<String, String, String>(c.getString(0), "  Slot", "" + c.getInt(1)));
			} while (c.moveToNext());
		} else {
			safariList.add(new Trip<String, String, String>("N/A", "", ""));
		}
		c.close();
	}
	
	private void updateHeader(View view) {
		TextView col1 = (TextView) view.findViewById(R.id.header_first);
		TextView col2 = (TextView) view.findViewById(R.id.header_second);
		TextView col3 = (TextView) view.findViewById(R.id.header_third);
		TextView col4 = (TextView) view.findViewById(R.id.header_fourth);
		
		col1.setText("###");
		col2.setText("Name");
		col3.setText("Trigger");
		col4.setText("Level");
	}
}