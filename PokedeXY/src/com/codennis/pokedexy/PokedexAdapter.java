package com.codennis.pokedexy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PokedexAdapter extends BaseAdapter {
	private static ArrayList<Pokemon> pokedex;
	private LayoutInflater mInflater;
	private SwipeListener listener;
	
	public PokedexAdapter(Context context, ArrayList<Pokemon> pokedex, SwipeListener listener) {
		this.pokedex = pokedex;
		this.listener = listener;
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
			view = mInflater.inflate(R.layout.pokedex_row, null);
			view.setTag(viewHolder);
			
			viewHolder.txtNumber = (TextView)view.findViewById(R.id.row_number);
			viewHolder.txtName = (TextView)view.findViewById(R.id.row_name);
			
			holder = viewHolder;
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		// Add individual listeners to each row to pass position data
		SwipeListener newListen = new SwipeListener(mInflater.getContext());
		newListen.setPoke(poke);
		view.setOnTouchListener(newListen);
		//viewHolder.txtName.setOnTouchListener(newListen);
		/*
		if (this.listener != null)
		{
			this.listener.setPosition(position);
			view.setOnTouchListener(this.listener);
		}
		*/
		// Fix hacky string
		holder.pokemon = poke;
		holder.position = position;
		holder.txtNumber.setText("" + poke.getNID());
		holder.txtName.setText(poke.getName());
		view.setBackgroundColor(holder.getColor());
		
		return view;
	}
}
