package com.codennis.pokedexy;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.pokedex_row, null);
			holder = new ViewHolder();
			holder.txtNumber = (TextView)convertView.findViewById(R.id.number);
			holder.txtName = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Fix hacky string
		holder.txtNumber.setText("" + pokedex.get(position).getNID());
		holder.txtName.setText(pokedex.get(position).getName());
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView txtName;
		TextView txtNumber;
	}
}
