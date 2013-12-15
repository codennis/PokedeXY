package com.codennis.pokedexy;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class PokemonFragActivity extends FragmentActivity{
	public final static int pokedex_entry = 0;
	public final static int pokemon = 1;
	public final static int frag_three = 2;
	
	public static final int frag_count = 2;
	
	private FragmentPagerAdapter fpa;
	private ViewPager vp;
	protected List<Fragment> fragments = new ArrayList<Fragment>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pokemon_frag_activity);
		
		fragments.add(pokedex_entry, new PokemonFrag());
		fragments.add(pokemon, new PokemonFrag());
		
		fpa = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return frag_count;
			}
			@Override
			public android.support.v4.app.Fragment getItem(final int position) {
				return (android.support.v4.app.Fragment)fragments.get(position);
			}
			@Override
			public CharSequence getPageTitle(final int position) {
				switch (position) {
				case pokedex_entry:
					return "Title one";
				case pokemon:
					return "Title two";
				default:
					return null;
				}
			}
		};
		
		vp = (ViewPager) findViewById(R.id.pager);
		vp.setAdapter(fpa);
		vp.setCurrentItem(pokemon);
		
	}
}
