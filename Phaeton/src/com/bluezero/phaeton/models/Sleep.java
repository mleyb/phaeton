package com.bluezero.phaeton.models;

public class Sleep extends EntryModel {
	public String Start;
	public String End;
	
	public Sleep() {
		super();
	}
	
	public Sleep(String date, String start, String end) {
		super(date);
		Start = start;
		End = end;
	}
}
