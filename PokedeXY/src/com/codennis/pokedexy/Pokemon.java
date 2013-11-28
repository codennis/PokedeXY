package com.codennis.pokedexy;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

public class Pokemon implements Parcelable {
	private int nID, kalos, kID, evoLvl, caught, depth;
	private String name, evoSeries, evoHow, location, locationHow, xy;
	
	/**
	 * 
	 */
	public Pokemon() {
		super();
	}
	
	/**
	 * @param nID
	 * @param kalos
	 * @param kID
	 * @param name
	 * @param caught
	 */
	public Pokemon(int nID, int kalos, int kID, String name, int caught) {
		this.nID = nID;
		this.kalos = kalos;
		this.kID = kID;
		this.name = name;
		this.caught = caught;
	}

	public Pokemon(int nID, int kalos, int kID, String name, int caught, String evoSeries, int evoLvl, String evoHow, int depth) {
		this.nID = nID;
		this.kalos = kalos;
		this.kID = kID;
		this.name = name;
		this.caught = caught;
		this.evoSeries = evoSeries;
		this.evoLvl = evoLvl;
		this.evoHow = evoHow;
		this.depth = depth;
	}

	@Override
	public String toString() {
		String text;
		text = "" + nID;
		if (text.length() == 1)
			text = "00" + text;
		else if (text.length() == 2)
			text = "0" + text;
		text = name + "   #" + text;
		return text;
	}
	
	public int getNID() {
		return nID;
	}
	public int getKalos() {
		return kalos;
	}
	public int getKID() {
		return kID;
	}
	public String getName() {
		return name;
	}
	public String getEvo() {
		return evoSeries;
	}
	public String getEvoHow() {
		return evoHow;
	}
	public int getEvoLvl() {
		return evoLvl;
	}
	public int getDepth() {
		return depth;
	}
	/**
	 * @return
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param caught
	 */
	public void setCaught(boolean caught) {
		this.caught = caught ? 1 : 0;
	}
	/**
	 * @return
	 */
	public boolean getCaught() {
		return (caught == 1 ? true : false);
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub		
		out.writeInt(nID);		
		out.writeInt(kID);

		out.writeString(name);
		out.writeString(location);
		
		out.writeInt(caught);
	}
	
	public static final Parcelable.Creator<Pokemon> CREATOR = new Parcelable.Creator<Pokemon>() {
		public Pokemon createFromParcel(Parcel in) {
			return new Pokemon(in);
		}
		public Pokemon[] newArray(int size) {
			return new Pokemon[size];
		}
	};
	
	
	/**
	 * @param in
	 */
	private Pokemon(Parcel in) {
		this.nID = in.readInt();
		this.kID = in.readInt();
		
		this.name = in.readString();
		this.location = in.readString();
		
		this.caught = in.readInt();
	}
	
}


class ViewHolder {
    protected TextView text;
    protected ImageView icon;
    protected int position;
    private int imageid;
    boolean caught;
	TextView txtName;
	TextView txtNumber;
	TextView txtDet;
	TextView txtKalos;
	
    public ViewHolder()
    { }
    
    /**
     * @return
     */
    public int getImageId() {
        return imageid;
    }
}
