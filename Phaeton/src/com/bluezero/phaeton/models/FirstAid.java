package com.bluezero.phaeton.models;

public final class FirstAid extends EntryModel {
	public String Description;
	public String Reason;
	public String Detail;
	public boolean DoctorRecommended;

	public FirstAid(){
        super();
    }
      
    public FirstAid(String date, String description, String detail, String reason, boolean doctorRecommended) {
    	super(date);
    	Date = date;
    	Description = description;
    	Reason = reason;
    	Detail = detail;
    	DoctorRecommended = doctorRecommended;
    }
}
