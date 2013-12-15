package com.codennis.pokedexy;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EntryFrag extends Fragment {
	private Pokemon poke;
	private String x,y;
	private SQLiteDatabase db;
	private ArrayList<Pokemon> pokedex;
	private PokedexAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.pokemon, null);

		DBHelper dbh = DBHelper.getInstance(this.getActivity());
		db = dbh.getDb();
		Intent i = this.getActivity().getIntent();
		int id = i.getExtras().getInt("poke_id");

		String query = "SELECT _id, kalos, k_id, name, caught, evo_series, evo_lvl, evo_how, depth, x, y FROM pokedex";
		Cursor c = db.rawQuery(query + " WHERE _id=" + id,null);
		c.moveToFirst();
		if (!c.isAfterLast()) {
			poke = new Pokemon(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),
					c.getInt(4),c.getString(5),c.getInt(6),c.getString(7),c.getInt(8));
			x = c.getString(9);
			y = c.getString(10);
		}
		else {
			return v;
		}
		c.close();

		updateDetails(v);
		
		ImageView img = (ImageView) v.findViewById(R.id.pokeImg);
		String imgName = "_"+poke.getNIDString();
		int resID = getResources().getIdentifier(imgName, "drawable", getActivity().getPackageName());
		img.setImageResource(resID);
		
		return v;
	}
	
	private void updateDetails(View view) {

		final TextView viewName = (TextView) view.findViewById(R.id.name);
		final TextView viewKalos = (TextView) view.findViewById(R.id.kalos);
		final TextView xEntry = (TextView) view.findViewById(R.id.xEntry);
		final TextView yEntry = (TextView) view.findViewById(R.id.yEntry);
		
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
		
		xEntry.setText(x);
		yEntry.setText(y);
		
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
