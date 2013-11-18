package com.codennis.pokedexy;

public class Pokemon {
	private int nID, ceID, coID, mtID;
	private String name, locations;
	private boolean caught;
	
	public Pokemon() {
		super();
	}
	
	public Pokemon(int nID, int ceID, int coID, int mtID, String name, String locations) {
		this.nID = nID;
		this.ceID = ceID;
		this.coID = coID;
		this.mtID = mtID;
		this.name = name;
		this.locations = locations;
		caught = false;
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
	
	public String getName() {
		return name;
	}
	
	public void setCaught(boolean caught) {
		this.caught = caught;
	}
}
