package com.bluezero.phaeton.models;

public final class Nappy extends EntryModel {
	public boolean Dirty;
	public String Detail;
	
	public Nappy() {
		super();
	}
	
	public Nappy(String date, boolean dirty, String detail) {
		super(date);
		Dirty = dirty; 
		Detail = detail;
	}
}
