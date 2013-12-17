package com.codennis.pokedexy;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * @author codennis
 *
 */
public class PokedexAdapter extends BaseAdapter implements Filterable {
	private ArrayList<Pokemon> pokedex;
	private ArrayList<Pokemon> filterDex;
	private LayoutInflater mInflater;
	private SQLiteDatabase db;
	// Filter preferences
	private boolean hideCaught;
	private String evoSeries;
	private int kalos;
	private String search;
	
	private ViewTreeObserver vto;
	private TextView txtCounter;
	
	
	/**
	 * Default constructor for main Pokedex listings.
	 * @param context
	 * @param pokedex ArrayList of all Pokemon
	 */
	public PokedexAdapter(Context context, ArrayList<Pokemon> pokedex) {
		this.pokedex = new ArrayList<Pokemon>(pokedex);
		this.filterDex = new ArrayList<Pokemon>(pokedex);
		mInflater = LayoutInflater.from(context);
		DBHelper dbh = DBHelper.getInstance(context);
		db = dbh.getDb();
		hideCaught = false;
		evoSeries = null;
		kalos = 0;
		search = null;
	}

	public PokedexAdapter(Context context, ArrayList<Pokemon> pokedex, TextView txtCounter) {
		this.pokedex = new ArrayList<Pokemon>(pokedex);
		this.filterDex = new ArrayList<Pokemon>(pokedex);
		mInflater = LayoutInflater.from(context);
		DBHelper dbh = DBHelper.getInstance(context);
		db = dbh.getDb();
		hideCaught = false;
		evoSeries = null;
		kalos = 0;
		this.txtCounter = txtCounter;
		search = null;
	}

	@Override
	public int getCount() {
		return filterDex.size();
	}
	@Override
	public Object getItem(int index) {
		return filterDex.get(index);
	}
	@Override
	public long getItemId(int index) {
		return index;
	}

	
	public boolean hideCaught() {
		return hideCaught;
	}
	
	public void setCaught(boolean caught) {
		this.hideCaught = caught;
		updateList();
	}

	protected int getKalos() {
		return kalos;
	}
	
	public String getEvoSeries() {
		return evoSeries;
	}
	
	public void setEvo(String evo) {
		this.evoSeries = evo;
		updateList();
	}
	
	public void setKalos(String kalos) {
		if (kalos == "National")
			this.kalos = 0;
		else if (kalos == "Central")
			this.kalos = 1;
		else if (kalos == "Coastal")
			this.kalos = 2;
		else if (kalos == "Mountain")
			this.kalos = 3;
		else if (kalos == "Safari")
			this.kalos = 4;
		updateList();
	}
	
	public void setSearch(String search) {
		this.search = search;
		this.getFilter().filter(search);
	}
	
	public void updateHeader(View view) {
		TextView col1 = (TextView) view.findViewById(R.id.header_first);
		TextView col2 = (TextView) view.findViewById(R.id.header_second);
		TextView col3 = (TextView) view.findViewById(R.id.header_third);
		TextView col4 = (TextView) view.findViewById(R.id.header_fourth);
		String first, second, third, fourth;
		switch (kalos)
		{
		case 0:
		case 1:
		case 2:
		case 3:
			first = "###";
			second = "Name";
			third = "Region";
			fourth = "###";
			break;
		case 4:
			first = "###";
			second = "Name";
			third = "Safari";
			fourth = "Slot";
			break;
		default:
			first = "";
			second = "";
			third = "";
			fourth = "";
		}
		col1.setText(first);
		col2.setText(second);
		col3.setText(third);
		col4.setText(fourth);
	}

	public int caughtCount() {
		String query;
		if (kalos == 4) // Safari
			query = "SELECT * FROM safari NATURAL JOIN pokedex WHERE caught=1";
		else {
			query = "SELECT * FROM pokedex WHERE caught=1";
			if (kalos > 0)
				query += " AND kalos=" + kalos;
		}
		Cursor c = db.rawQuery(query,  null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	public int totalCount() {
		String query = "SELECT * FROM ";
		if (kalos == 4) // Safari
			query += "safari";
		else if (kalos > 0)
			query += "pokedex WHERE kalos=" + kalos;
		else
			query += "pokedex";
		Cursor c = db.rawQuery(query,  null);
		int count = c.getCount();
		c.close();
		return count;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View view;
		if (position >= filterDex.size())
			return null;
		
		Pokemon tempPoke = filterDex.get(position);
		int whichSaf = tempPoke.getWhichSaf();
		final Pokemon poke = pokedex.get(tempPoke.getNID()-1);
		final ViewHolder viewHolder = new ViewHolder();
		ViewHolder holder = null;
		
		if (convertView == null) {
			view = mInflater.inflate(R.layout.pokedex_row, parent, false); // ******************************************** row
			
			view.setTag(viewHolder);
			
			viewHolder.col1 = (TextView)view.findViewById(R.id.row_first);
			viewHolder.col2 = (TextView)view.findViewById(R.id.row_second);
			viewHolder.col3 = (TextView)view.findViewById(R.id.row_third);
			viewHolder.col4 = (TextView)view.findViewById(R.id.row_fourth);
			
			holder = viewHolder;
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		// Global layout listener to keep rows correct colors
		final View green = view.findViewById(R.id.green);
		
		int width = poke.getCaught() ? view.getWidth() : 0;
		green.getLayoutParams().width = width;
		green.getLayoutParams().height = view.getHeight();
		//green.requestLayout();
		
		vto = view.getViewTreeObserver();
		if (vto.isAlive()) {
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (vto.isAlive())
						vto.removeGlobalOnLayoutListener(this);
					int width = (poke.getCaught() ? ((View) green.getParent()).getMeasuredWidth() : 0);
					green.getLayoutParams().width = width;
					green.getLayoutParams().height = view.getMeasuredHeight();
					green.requestLayout();
					//Log.i("GLOBAL","REQUEST LAYOUT");
				}
			});
		}
		
		// Set swipe listener to animate sliding green on swipe
		view.setOnTouchListener(new OnSwipeListener(mInflater.getContext(), view) {
			private Pokemon pkmn;
			@Override
			public boolean initDown() {
				this.pkmn = poke;
				return true;
			}
			
			@Override
			public boolean onDrag(MotionEvent me) {
				drag(view, initX, me, pkmn.getCaught());
				return true;
			}
			
			@Override
			public boolean onUp(MotionEvent me, boolean cancel) {
				finishAnim(view, initX, downTime, me, pkmn, cancel);
				return true;
			}
				
			@Override
			public boolean onTap() {
		    	Intent i = new Intent(context, PokemonFragActivity.class);
		    	i.putExtra("poke_id",pkmn.getNID());
		    	context.startActivity(i);
		    	return true;
			}
		});
		
		updateHolder(holder, poke, whichSaf);
		return view;
	}
		
	private void updateHolder(ViewHolder holder, Pokemon poke, int whichSaf) {
		holder.col1.setText(poke.getNIDString());
		holder.col2.setText(poke.getName());
		
		if (evoSeries == null) { // Pokedex
			String kalosText = "";
			switch (poke.getKalos()) {
			case 1:
				kalosText = "Central";
				break;
			case 2:
				kalosText = "Coastal";
				break;
			case 3:
				kalosText = "Mountain";
				break;
			}
			if (kalos != 4) {
				holder.col3.setText(kalosText);
				holder.col4.setText(poke.getKIDString());
			} else {
				@SuppressWarnings("rawtypes")
				Pair pair = poke.getSafari(whichSaf);
				if (pair != null) {
					holder.col3.setText("" + pair.getLeft());
					holder.col4.setText("" + pair.getRight());
				}
			}
		} else { // Evolutions
			String evoText = "";
			if (poke.getDepth() > 1 && poke.getEvoLvl() != 0)
				evoText += "Level " + poke.getEvoLvl();
			if (!poke.getEvoHow().isEmpty())
			{
				if (!evoText.isEmpty())
					evoText += ": ";
				evoText += poke.getEvoHow();
			}

			holder.col3.setText(poke.getEvoHow());
			if (poke.getEvoLvl() > 0)
				holder.col4.setText("" + poke.getEvoLvl());
			else
				holder.col4.setText("");
		}
	}
	
	// Resize green to correspond to position of finger dragging
	private void drag(final View v, float initX, MotionEvent e, boolean caught) {
		final int width = v.getMeasuredWidth();
		final View green = v.findViewById(R.id.green);
		float diff = e.getX() - initX;
		if (!caught && diff > 0)
			green.getLayoutParams().width = (int)diff;
		else if (caught && diff < 0)
			green.getLayoutParams().width = width + (int)diff;
        green.getLayoutParams().height = v.getMeasuredHeight();
        green.requestLayout();
	}
	
	private void finishAnim(final View v, float initX, float downTime, MotionEvent me, final Pokemon poke, boolean cancel) {
		final int width = v.getMeasuredWidth();
		final View green = v.findViewById(R.id.green);
		final int greenWidth = green.getLayoutParams().width;
		final float diffX = me.getX() - initX;
		long time = (long) (me.getEventTime() - downTime);
		final float speed = diffX/time;

		if (!poke.getCaught() && ((diffX > width/2) || speed > 1) && !cancel)
			poke.setCaught(true);
		else if (poke.getCaught() && ((diffX < -width/2) || speed < -1) && !cancel)
			poke.setCaught(false);

		// Set up animation listener
		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation a) {
				ContentValues cv = new ContentValues();
				cv.put("caught", (poke.getCaught() ? 1 : 0));
				db.update("pokedex", cv, "_id = " + poke.getNID(),null);
				updateList();
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationStart(Animation arg0) {}
		};

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float time, Transformation t) {
				if (poke.getCaught()) {
					green.getLayoutParams().width = (int) (greenWidth + (width - greenWidth) * time);
				} else {
					green.getLayoutParams().width = (int) (greenWidth - greenWidth * time);
				}
				green.getLayoutParams().height = v.getMeasuredHeight();
				green.requestLayout();
			}
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		if (al!=null) {
			anim.setAnimationListener(al);
		}
		if (time > 500)
			time = 500;
		else if (time < 100)
			time = 100;
		anim.setDuration(time);
		v.startAnimation(anim);
	}
	
	protected void updateList() {
		this.getFilter().filter(search);
	}
	
	// Updates arrayList to reflect new caught values
	public void updatePokedex() {
		String query = "SELECT _id, caught FROM pokedex";
		Cursor c = db.rawQuery(query,null);
		c.moveToFirst();
		if (!c.isAfterLast()) {
			do {
				pokedex.get(c.getInt(0)-1).setCaught(c.getInt(1));
			} while (c.moveToNext());
		}
		c.close();
		updateList();
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (txtCounter != null)
			txtCounter.setText("Caught: " + caughtCount() + "/" + totalCount());
	}
	
	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence str) {
				Cursor c;
				String query = "SELECT _id, kalos, k_id, name, caught, evo_series, evo_lvl, evo_how, depth FROM pokedex WHERE 1=1";
				String where = "";
				String order = "";
				// Filter for evolutions
				if (getEvoSeries() != null) {
					where += " AND evo_series=?";
					order += " ORDER BY depth";
				}
				else {
					if(hideCaught())
						where += " AND caught=0";
					if (getKalos() == 4)
					{
						query = "SELECT _id, kalos, k_id, name, caught, evo_series, evo_lvl, evo_how, depth, safari, slot FROM " +
								"safari NATURAL JOIN pokedex WHERE 1=1";
						if (hideCaught())
							where += " AND caught=0";
						order += " ORDER BY safari, slot ";
					}
					else if (getKalos() > 0)
					{
						where += " AND kalos=" + getKalos();
						order += " ORDER BY k_id";
					}
					// Filter by name search
					if (str != null) {
						where += " AND name LIKE '%" + str + "%'";
					}
				}
				
				if (getEvoSeries() != null)
					c = db.rawQuery(query + where + order, new String[] { getEvoSeries() });
				else
					c = db.rawQuery(query + where + order, null);
				

				// Store filtered set of Pokemon into FilterResults
				ArrayList<Pokemon> filtered  = new ArrayList<Pokemon>();
				String temp;
				Boolean added;
				Pokemon tempPoke;
				int id = 0;
				c.moveToFirst();
				if (!c.isAfterLast()) {
					do {
						added = false;
						if (getKalos() == 4) {
							temp = c.getString(3);
							for (Pokemon p : filtered) {
								if (p.getName().equals(temp)) {
									added = true;
									id = p.getNID();
									break;
								}
							}
						}
						tempPoke = new Pokemon(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),
								c.getInt(4),c.getString(5),c.getInt(6),c.getString(7),c.getInt(8));
						if (added) {
							tempPoke.setWhichSaf(pokedex.get(id-1).getWhichSaf() + 1);
						}
						filtered.add(tempPoke);
					} while (c.moveToNext());
				}
				c.close();
				FilterResults res = new FilterResults();
				res.values = filtered;
				res.count = filtered.size();
				return res;
			}
			
			/**
			 * Reset filterDex to new set of Pokemon to display
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence str, FilterResults results) {
				filterDex.clear();
				filterDex.addAll((ArrayList<Pokemon>) results.values);
				notifyDataSetChanged();
			}
		};
	}
}
