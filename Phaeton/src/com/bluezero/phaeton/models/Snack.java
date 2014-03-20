package com.bluezero.phaeton.models;

public final class Snack extends EntryModel {
	public String Description;
	public int AmountConsumed;
	
	public Snack() {
		super();
	}
	
	public Snack(String date, String description, int amountConsumed) {
		super(date);
		Description = description;
		AmountConsumed = amountConsumed;
	}
}
