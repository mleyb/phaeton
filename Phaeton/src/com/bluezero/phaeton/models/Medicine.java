package com.bluezero.phaeton.models;

public final class Medicine extends EntryModel {
	public String Type;
	public String Amount;
	public String Detail;

	public Medicine(){
        super();
    }
      
    public Medicine(String date, String type, String amount, String detail) {
    	super(date);
    	Date = date;
    	Type = type;
    	Amount = amount;
    	Detail = detail; 
    }
}