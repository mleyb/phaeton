package com.bluezero.phaeton.models;

public final class Activity extends EntryModel {
	public String Description;
	public String Detail;

	public Activity(){
        super();
    }
      
    public Activity(String date, String description, String detail) {
    	super(date);
    	Description = description;
    	Detail = detail; 
    }
}