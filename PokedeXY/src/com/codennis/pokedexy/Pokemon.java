package com.codennis.pokedexy;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

public class Pokemon implements Parcelable {
	private int nID, ceID, coID, mtID, evoID;
	private String name, locations;
	private int caught;
	
	public Pokemon() {
		super();
	}
	
	public Pokemon(int nID, int ceID, int coID, int mtID, String name, String locations, int caught, int evo) {
		this.nID = nID;
		this.ceID = ceID;
		this.coID = coID;
		this.mtID = mtID;
		this.name = name;
		this.locations = locations;
		this.caught = caught;
		this.evoID = evo;
	}
	
	@Override
	public String toString() {
		return this.nID + " " + this.name;
	}

	public int getNID() {
		return nID;
	}
	public int getCeID() {
		return ceID;
	}
	public int getCoID() {
		return coID;
	}
	public int getMtID() {
		return mtID;
	}
	public int getEvo() {
		return evoID;
	}
	
	public String getName() {
		return name;
	}
	public String getLocation() {
		return locations;
	}
	public void setCaught(boolean caught) {
		this.caught = caught ? 1 : 0;
	}
	public boolean getCaught() {
		return (caught == 1 ? true : false);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub		
		out.writeInt(nID);		
		out.writeInt(ceID);		
		out.writeInt(coID);		
		out.writeInt(mtID);

		out.writeString(name);
		out.writeString(locations);
		
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
	
	
	private Pokemon(Parcel in) {
		this.nID = in.readInt();
		this.ceID = in.readInt();
		this.coID = in.readInt();
		this.mtID = in.readInt();
		
		this.name = in.readString();
		this.locations = in.readString();
		
		this.caught = in.readInt();
	}
	
}


class ViewHolder {
    protected TextView text;
    protected ImageView icon;
    protected int position;
    protected Pokemon pokemon;
    private int imageid;
    boolean caught;
	TextView txtName;
	TextView txtNumber;
	
    public ViewHolder(Pokemon poke)
    {
    	pokemon = poke;
    }
    
    public int getImageId() {
        return imageid;
    }
    
    public void setCaught(boolean caught) {
    	pokemon.setCaught(caught);
    }
}
