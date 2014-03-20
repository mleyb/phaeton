package com.bluezero.phaeton.models;

public final class Milestone extends EntryModel {
	public String Description;
	public String Detail;

	public Milestone(){
        super();
    }
      
    public Milestone(String date, String description, String detail) {
    	super(date);
    	Date = date;
    	Description = description;
    	Detail = detail; 
    }
}
