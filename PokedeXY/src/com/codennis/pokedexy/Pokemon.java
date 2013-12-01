package com.codennis.pokedexy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class Pokemon {
	private int nID, kalos, kID, evoLvl, caught, depth;
	private String name, evoSeries, evoHow, location, locationHow, xy;
	private ArrayList<Pair> safari;
	private int whichSaf;
	
	public Pokemon() {
		super();
	}
	
	public Pokemon(int 		nID, 
				   int 		kalos,
				   int		kID,
				   String	name,
				   int 		caught,
				   String 	evoSeries,
				   int 		evoLvl,
				   String 	evoHow,
				   int 		depth) {
		
		this.nID = nID;
		this.kalos = kalos;
		this.kID = kID;
		this.name = name;
		this.caught = caught;
		this.evoSeries = evoSeries;
		this.evoLvl = evoLvl;
		this.evoHow = evoHow;
		this.depth = depth;
		this.safari = new ArrayList<Pair>();
		this.whichSaf = 0;
	}
	
	@Override
	public String toString() {
		return "";
	}

	public boolean getCaught() {
		return (caught == 1 ? true : false);
	}
	
	public void setCaught(int caught) {
		this.caught = caught;
	}
	public void setCaught(boolean caught) {
		this.caught = caught ? 1 : 0;
	}

	public int getNID() {
		return nID;
	}
	public String getNIDString() {
		String textID;
		if (nID < 10)
			textID = "00" + nID;
		else if (nID < 100)
			textID = "0" + nID;
		else
			textID = "" + nID;
		return textID;
	}

	public int getKalos() {
		return kalos;
	}

	public int getKID() {
		return kID;
	}
	public String getKIDString() {
		String textID;
		if (kID < 10)
			textID = "00" + kID;
		else if (kID < 100)
			textID = "0" + kID;
		else
			textID = "" + kID;
		return textID;
	}

	public int getEvoLvl() {
		return evoLvl;
	}

	public int getDepth() {
		return depth;
	}

	public String getName() {
		return name;
	}

	public String getEvoSeries() {
		return evoSeries;
	}

	public String getEvoHow() {
		return evoHow;
	}

	public String getLocation() {
		return location;
	}

	public String getLocationHow() {
		return locationHow;
	}

	public String getXy() {
		return xy;
	}
	
	public Pair getSafari(int i) {
		return safari.get(i);
	}
	public void setWhichSaf(int i) {
		whichSaf = i;
	}
	public int getWhichSaf() {
		return whichSaf;
	}
	
	public void addSafari(String type, int slot) {
		safari.add(new Pair(type,  slot));
	}
}

class Pair<L,R> {
	  private final L left;
	  private final R right;

	  public Pair(L left, R right) {
	    this.left = left;
	    this.right = right;
	  }

	  public L getLeft() { return left; }
	  public R getRight() { return right; }
}

class ViewHolder {
    protected ImageView icon;
    private int imageid;
	protected TextView col1;
	protected TextView col2;
	protected TextView col3;
	protected TextView col4;
	
    public ViewHolder() { }
    
    public int getImageId() {
        return imageid;
    }
}
