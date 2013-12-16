package com.codennis.pokedexy;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TriplesAdapter extends ArrayAdapter<Trip> {
	private Context context;

	private static class ViewHolder {
		TextView location, locationHow, xy;
	}

	public TriplesAdapter(Context context) {
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

