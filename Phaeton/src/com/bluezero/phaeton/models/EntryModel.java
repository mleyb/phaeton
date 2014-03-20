package com.bluezero.phaeton.models;

public abstract class EntryModel {
	public int Id = 0;
	public String Date = null;
	
	public EntryModel() {		
	}
	
	public EntryModel(String date) {
		Date = date;
	}	
	
	protected double roundToDecimal(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
}
