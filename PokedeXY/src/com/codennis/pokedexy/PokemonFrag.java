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
		Log.i("FRAG",id+"");
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
		
		Log.i("POKEMON", poke.getName() + " " + poke.getEvoSeries());

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
		evoList.setTextFilterEnabled(true);
		adapter = new PokedexAdapter(this.getActivity(), pokedex);
		adapter.setEvo(poke.getEvoSeries());
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
		LocationAdapter adapter = new LocationAdapter(this.getActivity());
		updateLocations();
		adapter.addAll(locationList);
		viewLocation.setAdapter(adapter);
		
		safariList = new ArrayList<Trip<String,String,String>>();
		LocationAdapter safAdapter = new LocationAdapter(this.getActivity());
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

class LocationAdapter extends ArrayAdapter<Trip> {
	private Context context;
	
	private static class ViewHolder {
		TextView location, locationHow, xy;
	}
	
	public LocationAdapter(Context context) {
		super(context, R.layout.location_row);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Trip trip = getItem(position);
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.location_row, null);
			vh.location = (TextView) convertView.findViewById(R.id.loc_first);
			vh.locationHow = (TextView) convertView.findViewById(R.id.loc_second);
			vh.xy = (TextView) convertView.findViewById(R.id.loc_third);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		convertView.setBackgroundColor(context.getResources().getColor(getTypeColor((String)trip.getLeft())));
		vh.location.setText((String)trip.getLeft());
		vh.locationHow.setText((String)trip.getMid());
		vh.xy.setText((String)trip.getRight());
		return convertView;
	}
	
	private int getTypeColor(String safari) {
		if (safari.equals("Bug"))
			return R.color.Bug;
		else if (safari.equals("Dark"))
			return R.color.Dark;
		else if (safari.equals("Dragon"))
			return R.color.Dragon;
		else if (safari.equals("Electric"))
			return R.color.Electric;
		else if (safari.equals("Fairy"))
			return R.color.Fairy;
		else if (safari.equals("Fighting"))
			return R.color.Fighting;
		else if (safari.equals("Fire"))
			return R.color.Fire;
		else if (safari.equals("Flying"))
			return R.color.Flying;
		else if (safari.equals("Ghost"))
			return R.color.Ghost;
		else if (safari.equals("Grass"))
			return R.color.Grass;
		else if (safari.equals("Ground"))
			return R.color.Ground;
		else if (safari.equals("Ice"))
			return R.color.Ice;
		else if (safari.equals("Normal"))
			return R.color.Normal;
		else if (safari.equals("Poison"))
			return R.color.Poison;
		else if (safari.equals("Psychic"))
			return R.color.Psychic;
		else if (safari.equals("Rock"))
			return R.color.Rock;
		else if (safari.equals("Steel"))
			return R.color.Steel;
		else if (safari.equals("Water"))
			return R.color.Water;
		else
			return R.color.NA;
	}
}


class Trip<L,M,R> {
	  private final L left;
	  private final M mid;
	  private final R right;

	  public Trip(L left, M mid, R right) {
	    this.left = left;
	    this.mid = mid;
	    this.right = right;
	  }

	  public L getLeft() { return left; }
	  public M getMid() { return mid; }
	  public R getRight() { return right; }
}
