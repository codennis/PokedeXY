package com.codennis.pokedexy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PokedexAdapter extends BaseAdapter {
	private static ArrayList<Pokemon> pokedex;
	private LayoutInflater mInflater;
	private OnTouchListener listener;
	
	public PokedexAdapter(Context context, ArrayList<Pokemon> pokedex, OnTouchListener listener) {
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
		
		final ViewHolder viewHolder = new ViewHolder();
		ViewHolder holder = null;
		
		if (convertView == null) {
			view = mInflater.inflate(R.layout.pokedex_row, null);
			view.setTag(viewHolder);
			
			viewHolder.txtNumber = (TextView)view.findViewById(R.id.number);
			viewHolder.txtName = (TextView) view.findViewById(R.id.name);
			viewHolder.position = position;
			
			holder = viewHolder;
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		if (this.listener != null)
			view.setOnTouchListener(this.listener);

		// Fix hacky string
		holder.pokemon = poke;
		holder.position = position;
		holder.txtNumber.setText("" + pokedex.get(position).getNID() + " " + position);
		holder.txtName.setText(pokedex.get(position).getName());
		
		return view;
	}
}
