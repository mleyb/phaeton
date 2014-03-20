package com.bluezero.phaeton.models;

public final class Carer {
    public String Id;    
    public String Key;
    public String Name;
    public Child[] Children;    
    
    public Carer(){
        super();
    }
      
    public Carer(String id, String key, String name, Child[] children) {
    	super();
    	Id = id;
    	Key = key;
    	Name = name;
    	Children = children;
    }
}
