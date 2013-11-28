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
	private static ArrayList<Pokemon> pokedex;
	private ArrayList<Pokemon> filterDex;
	private LayoutInflater mInflater;
	private SQLiteDatabase db;
	// Filter preferences
	private boolean filterCaught;
	private String evo;
	private int kalos;
	
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
		db = dbh.openDatabase();
		filterCaught = false;
		evo = null;
		kalos = 0;
	}

	public PokedexAdapter(Context context, ArrayList<Pokemon> pokedex, TextView txtCounter) {
		this.pokedex = new ArrayList<Pokemon>(pokedex);
		this.filterDex = new ArrayList<Pokemon>(pokedex);
		mInflater = LayoutInflater.from(context);
		DBHelper dbh = DBHelper.getInstance(context);
		db = dbh.openDatabase();
		filterCaught = false;
		evo = null;
		kalos = 0;
		this.txtCounter = txtCounter;
	}

	
	protected boolean hideCaught() {
		return filterCaught;
	}
	protected String getEvo() {
		return evo;
	}
	protected int getKalos() {
		return kalos;
	}
	
	public void setCaught(boolean caught) {
		this.filterCaught = caught;
		this.getFilter().filter(null);
	}
	public void setEvo(String evo) {
		this.evo = evo;
		this.getFilter().filter(null);
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
		this.getFilter().filter(null);
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
	
	public int caughtCount() {
		String query = "SELECT * FROM pokedex WHERE caught=1";
		if (kalos > 0)
			query += " AND kalos=" + kalos;
		Cursor c = db.rawQuery(query,  null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	public int totalCount() {
		String query = "SELECT * FROM pokedex";
		if (kalos > 0)
			query += " WHERE kalos=" + kalos;
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
		
		final Pokemon poke = pokedex.get(filterDex.get(position).getNID()-1);

		final ViewHolder viewHolder = new ViewHolder();
		ViewHolder holder = null;
		
		if (convertView == null) {
			view = mInflater.inflate(R.layout.pokedex_row, parent, false);
			view.setTag(viewHolder);
			
			viewHolder.txtNumber = (TextView)view.findViewById(R.id.row_number);
			viewHolder.txtName = (TextView)view.findViewById(R.id.row_name);
			viewHolder.txtDet = (TextView)view.findViewById(R.id.row_dets);
			viewHolder.txtKalos = (TextView)view.findViewById(R.id.kalos_number);
			holder = viewHolder;
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		final int width = (poke.getCaught() ? parent.getMeasuredWidth() : 0);
		final View green = view.findViewById(R.id.green);
		vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				green.getLayoutParams().width = width;
				green.getLayoutParams().height = view.getHeight();
				green.requestLayout();
			}
			
		});
		
		// Set swipe listener to animate sliding green on swipe
		view.setOnTouchListener(new OnSwipeListener(mInflater.getContext(), view) {
			private Pokemon pokem;
			public boolean initDown() {
				this.pokem = poke;
				return true;
			}
			
			public boolean onDrag(MotionEvent me) {
				drag(view, initX, me, pokem.getCaught());
				return true;
			}
			
			public boolean onUp(MotionEvent me) {
				finishAnim(view, initX, downTime, me, pokem);
				return true;
			}
				
			public boolean onTap() {
		    	Intent i = new Intent(context, PokemonActivity.class);
		    	i.putExtra("poke_id",pokem.getNID());
		    	context.startActivity(i);
		    	return true;
			}
		});
		
		holder.position = position;
		String textID;
		textID = "" + poke.getNID();
		if (textID.length() == 1)
			textID = "00" + textID;
		else if (textID.length() == 2)
			textID = "0" + textID;
		holder.txtNumber.setText(textID);
		
		if (evo == null) { // Pokedex
			String kalosText = "";
			if (poke.getKID() != 0)
				textID = "" + poke.getKID();
			else
				textID = "";
			if (textID.length() == 1)
				textID = "00" + textID;
			else if (textID.length() == 2)
				textID = "0" + textID;
			
			switch (poke.getKalos()) {
			case 1:
				kalosText = "    Central";
				break;
			case 2:
				kalosText = "    Coastal";
				break;
			case 3:
				kalosText = "    Mountain";
				break;
			}
			holder.txtName.setText(kalosText);
			holder.txtKalos.setText("   "+textID);
			holder.txtDet.setText(poke.getName());
		} else { // Evolutions
			holder.txtName.setText(poke.getName());
			String evoText = "";
			if (poke.getDepth() > 1 && poke.getEvoLvl() != 0)
				evoText += "Level " + poke.getEvoLvl();
			if (!poke.getEvoHow().isEmpty())
			{
				if (!evoText.isEmpty())
					evoText += ": ";
				evoText += poke.getEvoHow();
			}
			holder.txtDet.setText(evoText);
		}
		return view;
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
	}
	
	private void finishAnim(final View v, float initX, float downTime, MotionEvent me, final Pokemon poke) {
		final int width = v.getMeasuredWidth();
		final View green = v.findViewById(R.id.green);
		final float diff = me.getX() - initX;
		long time = (long) (me.getEventTime() - downTime);
		final float speed = Math.abs(diff/time);
		
		if ((poke.getCaught() && diff > 0) || (!poke.getCaught() && diff < 0))
			return;
		if (!poke.getCaught() && ((diff > width/3) || speed > 1))
			poke.setCaught(true);
		else if (poke.getCaught() && ((diff < -width/3) || speed > 1))
			poke.setCaught(false);
		
		// Set up animation listener
		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation a) {
				ViewHolder vh = (ViewHolder)v.getTag();
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
					if (diff > width/3 || speed > 1) // Animate getting caught
						green.getLayoutParams().width = (int) (diff + (width - diff)*time);
					else // Cancelling uncaught
						green.getLayoutParams().width = (int) (width + diff - diff* time);
				} else {
					if (diff < -width/3 || speed > 1) { // Animate uncaught
						green.getLayoutParams().width = (int) (width + diff - (width + diff)*time);
					} else { // Cancelling caught
						green.getLayoutParams().width = (int) (diff - diff * time);
					}
				}
			}
			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		
		if (al!=null) {
			anim.setAnimationListener(al);
		}
		if (time > 700)
			time = 700;
		else if (time < 100)
			time = 100;
		anim.setDuration(time);
		v.startAnimation(anim);
	}
	
	protected void updateList() {
		this.getFilter().filter(null);
	}
	
	@Override
	public void notifyDataSetChanged() {
		if (txtCounter != null)
			txtCounter.setText("Caught: " + caughtCount() + "/" + totalCount());
		super.notifyDataSetChanged();
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Filterable#getFilter()
	 * Filter ArrayList to display a given subset of Pokemon
	 */
	@Override
	public Filter getFilter() {
		return new Filter() {
			private boolean filterCaught = false;
			@Override
			protected FilterResults performFiltering(CharSequence str) {
				Cursor c;
				String query = "SELECT _id, kalos, k_id, name, caught, evo_series, evo_lvl, evo_how, depth FROM pokedex WHERE 1=1";
				// Query database for filter preference
				if (str==null) {
					if (getEvo() != null) {
						query += " AND evo_series=?";
						query += " ORDER BY depth";
					}
					else {
						if(hideCaught())
							query += " AND caught=0";
						if (getKalos() > 0)
						{
							query += " AND kalos=" + getKalos();
							query += " ORDER BY k_id";
						}
					}
				}
				// Filter by name search
				
				if (getEvo() != null)
					c = db.rawQuery(query, new String[] { getEvo() });
				else
					c = db.rawQuery(query, null);
				

				// Store filtered set of Pokemon into FilterResults
				ArrayList<Pokemon> filtered  = new ArrayList<Pokemon>();
				c.moveToFirst();
				if (!c.isAfterLast()) {
					do {
						filtered.add(new Pokemon(c.getInt(0),c.getInt(1),c.getInt(2),c.getString(3),
								c.getInt(4),c.getString(5),c.getInt(6),c.getString(7),c.getInt(8)));
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
			@Override
			protected void publishResults(CharSequence str, FilterResults results) {
				filterDex.clear();
				filterDex.addAll((ArrayList<Pokemon>) results.values);
				notifyDataSetChanged();
			}
		};
	}
}
