package com.bluezero.phaeton.models;

public final class Sick extends EntryModel {
	public String Description;
	public String Detail;
	public boolean DoctorRecommended;

	public Sick(){
        super();
    }
      
    public Sick(String date, String description, String detail, boolean doctorRecommended) {
    	super(date);
    	Description = description;
    	Detail = detail;
    	DoctorRecommended = doctorRecommended;
    }
}