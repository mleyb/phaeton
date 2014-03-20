package com.bluezero.phaeton.models;

public final class Bottle extends EntryModel {
	public double Amount;
	public int Unit;
	
	public Bottle() {
		super();
	}
	
	public Bottle(String date, double amount, int unit) {
		super(date);
		Amount = roundToDecimal(amount);		
		Unit = unit;
	}
}
