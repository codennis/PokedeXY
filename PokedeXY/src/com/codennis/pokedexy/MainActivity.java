package com.codennis.pokedexy;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author codennis
 *
 */
public class MainActivity extends Activity implements TabListener {
	
	private static final String STATE_SELECTED_NAV_ITEM = "selected_navigation_item";
	
	private ArrayList<Pokemon> pokedex = new ArrayList<Pokemon>();
	private SQLiteDatabase db;
	private PokedexAdapter adapter;
	private ListView pokedexList;
	private EditText search;
	private String kalos;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Initialize action bar tabs
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("National").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Central").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Coastal").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Mountain").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Safari").setTabListener(this));
		
		DBHelper dbh = DBHelper.getInstance(this);
		db = dbh.getDb();
		pokedexList = (ListView) findViewById(R.id.nationalDex);
		initializePokedex();
		// Set up filtering checkbox to update listview.
		final CheckedTextView filterCheck = (CheckedTextView) findViewById(R.id.filterCheck);
		filterCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((CheckedTextView) v).toggle();
				adapter.setCaught(filterCheck.isChecked());
			}
		});
		
		search =  (EditText) findViewById(R.id.search);
		TextWatcher tw = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable edit) {
				adapter.setSearch(edit.toString());
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
		};
		search.addTextChangedListener(tw);
		search.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					search.clearFocus();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
				}
				return false;
			}
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.updatePokedex();
			search.clearFocus();
		}
	}

	protected void showData() {
		String query = "SELECT name, safari FROM safari";
		Cursor c = db.rawQuery(query,null);
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				Log.i("83", c.getString(0) + " " + c.getString(1));
			} while (c.moveToNext());
		}
		c.close();
	}
	
	/**
	 *   Read database and store data into Pokedex arrayList. Then set up adapter to display
	 *   in the Pokedex listView.
	 */
	private void initializePokedex() {
		String query = "SELECT _id, kalos, k_id, name, caught, evo_series, evo_lvl, evo_how, depth FROM pokedex";
		Cursor c = db.rawQuery(query,null);
		c.moveToFirst();
		pokedex.clear();
		if (!c.isAfterLast()) {
			do {
				pokedex.add(new Pokemon(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),
						c.getInt(4),c.getString(5),c.getInt(6),c.getString(7),c.getInt(8)));
			} while (c.moveToNext());
		}
		
		query = "SELECT _id, safari, slot FROM safari NATURAL JOIN pokedex";
		c = db.rawQuery(query, null);
		c.moveToFirst();
		Pokemon poke;
		if (!c.isAfterLast()) {
			do {
				poke = pokedex.get(c.getInt(0) - 1);
				poke.addSafari(c.getString(1),c.getInt(2));
			} while (c.moveToNext());
		}
		
		c.close();

		adapter = new PokedexAdapter(this, pokedex, (TextView) findViewById(R.id.counter));
		adapter.updateHeader(findViewById(android.R.id.content));
		pokedexList.setTextFilterEnabled(true);
		pokedexList.setAdapter(adapter);
		adapter.updateList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAV_ITEM)) {
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAV_ITEM));
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAV_ITEM, getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		kalos = tab.getText().toString();
		if (adapter != null) {
			adapter.setKalos(kalos);
			adapter.updateHeader(findViewById(android.R.id.content));
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// Do nothing
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// Do nothing
	}
}


