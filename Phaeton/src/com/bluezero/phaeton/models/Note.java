package com.bluezero.phaeton.models;

public final class Note extends EntryModel {
	public String Detail;

	public Note(){
        super();
    }
      
    public Note(String date, String detail) {
    	super(date);
    	Date = date;
    	Detail = detail; 
    }
}