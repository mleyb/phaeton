package com.bluezero.phaeton.models;

public final class Meal extends EntryModel {
	public int Type;
	public String Description;
	public int AmountConsumed;
	
	public Meal() {
		super();
	}
	
	public Meal(String date, int type, String description, int amountConsumed) {
		super(date);
		Date = date;
		Type = type;
		Description = description;
		AmountConsumed = amountConsumed;
	}
}
