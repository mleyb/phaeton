package com.bluezero.phaeton.models;

public final class Drink extends EntryModel {
	public String Description;
	public double Amount;
	public int Unit;
	
	public Drink() {
		super();
	}
	
	public Drink(String date, String description, double amount, int unit) {
		super(date);
		Description = description;
		Amount = roundToDecimal(amount);	
		Unit = unit;
	}
}
