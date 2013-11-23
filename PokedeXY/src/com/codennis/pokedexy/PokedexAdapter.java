package com.codennis.pokedexy;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class PokedexAdapter extends BaseAdapter implements Filterable {
	private static ArrayList<Pokemon> pokedex;
	private ArrayList<Pokemon> filterDex;
	private LayoutInflater mInflater;
	private SQLiteDatabase newDB;
	private boolean filterCaught;
	private int evo;
	
	public PokedexAdapter(Context context, ArrayList<Pokemon> pokedex) {
		this.pokedex = new ArrayList<Pokemon>(pokedex);
		this.filterDex = new ArrayList<Pokemon>(pokedex);
		mInflater = LayoutInflater.from(context);
		DBHelper dbh = DBHelper.getInstance(context);
		newDB = dbh.openDatabase();
		filterCaught = false;
		evo = 0;
	}
	
	public PokedexAdapter(Context context, ArrayList<Pokemon> pokedex, int evo) {
		this.pokedex = new ArrayList<Pokemon>(pokedex);
		this.filterDex = new ArrayList<Pokemon>(pokedex);
		mInflater = LayoutInflater.from(context);
		DBHelper dbh = DBHelper.getInstance(context);
		newDB = dbh.openDatabase();
		filterCaught = false;
		this.evo = evo;
	}

	@Override
	public int getCount() {
		return pokedex.size();
	}

	@Override
	public Object getItem(int index) {
		return pokedex.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		if (position >= filterDex.size())
			return null;
		
		Pokemon poke = pokedex.get(filterDex.get(position).getNID()-1);
		
		final ViewHolder viewHolder = new ViewHolder(poke);
		ViewHolder holder = null;
		
		if (convertView == null) {
			view = mInflater.inflate(R.layout.pokedex_row, parent, false);
			view.setTag(viewHolder);
			
			viewHolder.txtNumber = (TextView)view.findViewById(R.id.row_number);
			viewHolder.txtName = (TextView)view.findViewById(R.id.row_name);
			Log.i("LOADING",poke.getName() + " " + poke.getCaught());
			holder = viewHolder;
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		view.findViewById(R.id.green).getLayoutParams().width = (poke.getCaught() ? parent.getMeasuredWidth() : 0);
		
		view.setOnTouchListener(new OnSwipeListener(mInflater.getContext(), view) {
			Pokemon poke;
			public boolean onTap() {
				poke = viewHolder.pokemon;
				Log.i("LISTENER", "TAPPED" + poke.getName() + " " + poke.getLocation());
		    	Intent i = new Intent(context, PokemonActivity.class);
		    	Bundle b = new Bundle();
		    	b.putParcelable("TEST", poke);
		    	i.putExtra("poke_id",poke.getNID());
		    	i.putExtras(b);
		    	context.startActivity(i);
		    	return true;
			}
			
			public boolean onSwipeLeft() {
				poke = viewHolder.pokemon;
				if (viewHolder.pokemon.getCaught()) {
			        viewHolder.setCaught(false);
			        Log.i("LISTENER", "UNCAUGHT");
					ContentValues cv = new ContentValues();
					cv.put("caught", (poke.getCaught() ? 1 : 0));
					newDB.update("pokedex", cv, "_id = " + poke.getNID(),null);
			        //TransitionDrawable transition = (TransitionDrawable) view.getBackground();
			        //transition.startTransition(1000);
			        //view.setBackgroundColor(viewHolder.getColor());
			        animate(view, false);
				}
		        return true;
			}
			public boolean onSwipeRight() {
				poke = viewHolder.pokemon;
				if (!viewHolder.pokemon.getCaught()) {
			        viewHolder.setCaught(true);
			        Log.i("LISTENER", "CAUGHT");
					ContentValues cv = new ContentValues();
					cv.put("caught", (poke.getCaught() ? 1 : 0));
					newDB.update("pokedex", cv, "_id = " + poke.getNID(),null);
			        //TransitionDrawable transition = (TransitionDrawable) view.getBackground();
			        //transition.reverseTransition(1000);
			        //view.setBackgroundColor(viewHolder.getColor());
			        animate(view, true);
				}
		        return true;
			}
		});
		
		holder.pokemon = poke;
		holder.position = position;
		holder.txtNumber.setText("" + poke.getNID());
		holder.txtName.setText(poke.getName());
		//view.setBackgroundColor(holder.getColor());
		
		return view;
	}
	
	private void animate(final View v, final boolean catchIt) {
		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation a) {
				ViewHolder vh = (ViewHolder)v.getTag();
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationStart(Animation arg0) {}
		};
		
		final int width = v.getMeasuredWidth();
		final View green = v.findViewById(R.id.green);
		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float time, Transformation t) {
				if (catchIt) {
					green.getLayoutParams().width = (int) (width*time);
					green.requestLayout();
				} else {
					green.getLayoutParams().width = width - (int)(width*time);
					green.requestLayout();
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
		anim.setDuration(500);
		v.startAnimation(anim);
	}

	public void toggleCaught() {
		filterCaught = !filterCaught;
	}
	
	protected boolean getCaught() {
		return filterCaught;
	}
	
	protected int getEvo() {
		return evo;
	}
	
	@Override
	public Filter getFilter() {
		Log.i("FILTER", "each time");
		// TODO Auto-generated method stub
		return new Filter() {
			private boolean filterCaught = false;
			@Override
			protected FilterResults performFiltering(CharSequence arg0) {
				Cursor c;
				
				// Query database for filter preference
				if (arg0==null) {
					Log.i("FILTERING",getCaught() + " " + getEvo());
					if (getCaught()) {
						c = newDB.rawQuery("SELECT _id, name, location, caught, evo_id FROM pokedex WHERE caught=0",null); //"name asc");
					} else if (getEvo() != 0) {
						c = newDB.rawQuery("SELECT _id, name, location, caught, evo_id FROM pokedex WHERE evo_id="+getEvo(),null);
					}
					else {
						c = newDB.rawQuery("SELECT _id, name, location, caught, evo_id FROM pokedex",null);
					}
				} else { // By name search
					c = newDB.rawQuery("SELECT _id, name, location, caught, evo_id FROM pokedex", null);
				}
				
				c.moveToFirst();
				ArrayList<Pokemon> filtered = new ArrayList<Pokemon>();
				if (!c.isAfterLast()) {
					do {
						filtered.add(new Pokemon(c.getInt(0),0,0,0,c.getString(1),c.getString(2),c.getInt(3),c.getInt(4)));
					} while (c.moveToNext());
				}
				FilterResults res = new FilterResults();
				res.values = filtered;
				res.count = filtered.size();
				return res;
			}

			@Override
			protected void publishResults(CharSequence arg0, FilterResults arg1) {
				// TODO Auto-generated method stub
				filterDex.clear();
				filterDex.addAll((ArrayList<Pokemon>) arg1.values);
				notifyDataSetChanged();
			}
			
		};
	}
}
