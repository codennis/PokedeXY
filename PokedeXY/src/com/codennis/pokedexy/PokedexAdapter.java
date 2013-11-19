package com.codennis.pokedexy;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PokedexAdapter extends BaseAdapter {
	private static ArrayList<Pokemon> pokedex;
	private LayoutInflater mInflater;
	
	public PokedexAdapter(Context context, ArrayList<Pokemon> pokedex) {
		this.pokedex = pokedex;
		mInflater = LayoutInflater.from(context);
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
		if (position > pokedex.size())
			return null;
		
		Pokemon poke = pokedex.get(position);
		
		final ViewHolder viewHolder = new ViewHolder(poke);
		ViewHolder holder = null;
		
		if (convertView == null) {
			view = mInflater.inflate(R.layout.pokedex_row, parent, false);
			view.setTag(viewHolder);
			
			viewHolder.txtNumber = (TextView)view.findViewById(R.id.row_number);
			viewHolder.txtName = (TextView)view.findViewById(R.id.row_name);
			Log.i("LOADING",poke.getName() + " " + poke.getCaught() + parent.getMeasuredWidth());
			view.findViewById(R.id.green).getLayoutParams().width = (poke.getCaught() ? parent.getMeasuredWidth() : 0);
			holder = viewHolder;
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		view.setOnTouchListener(new OnSwipeListener(mInflater.getContext(), view) {
			public boolean onTap() {
				Pokemon poke = viewHolder.pokemon;
				Log.i("LISTENER", "TAPPED" + poke.getName() + " " + poke.getLocation());
		    	Intent i = new Intent(context, PokemonActivity.class);
		    	Bundle b = new Bundle();
		    	b.putParcelable("TEST", poke);
		    	i.putExtras(b);
		    	context.startActivity(i);
		    	return true;
			}
			
			public boolean onSwipeLeft() {
				if (viewHolder.pokemon.getCaught()) {
			        viewHolder.setCaught(false);
			        Log.i("LISTENER", "UNCAUGHT");
			        //TransitionDrawable transition = (TransitionDrawable) view.getBackground();
			        //transition.startTransition(1000);
			        //view.setBackgroundColor(viewHolder.getColor());
			        animate(view, false);
				}
		        return true;
			}
			public boolean onSwipeRight() {
				if (!viewHolder.pokemon.getCaught()) {
			        viewHolder.setCaught(true);
			        Log.i("LISTENER", "CAUGHT");
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
		Log.i("ANIMATE","width");
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
}
