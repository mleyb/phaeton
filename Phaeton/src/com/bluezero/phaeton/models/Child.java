package com.bluezero.phaeton.models;

import java.io.ByteArrayInputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public final class Child implements Parcelable {
    public int Id;
    public String Key;
    public String Forename;
    public String Surname;
    public String DateOfBirth;
    public String Notes;
    public String ParentContactUri;
    public String RegistrationCode;
    public boolean Deleted = false;
    public byte[] ImageData;
       
    public Bitmap getImage() {
    	if (ImageData != null) {
    		ByteArrayInputStream imageStream = new ByteArrayInputStream(ImageData);
    		Bitmap image = BitmapFactory.decodeStream(imageStream);    	
    		return image;
    	}
    	else {
    		return null;
    	}
    }
    
    public Child(){
        super();
    }
    
    public Child(String forename, String surname, String dateOfBirth, String notes, String parentContactUri) {
    	super();
    	Forename = forename;
        Surname = surname;
        DateOfBirth = dateOfBirth;
        Notes = notes;
        ParentContactUri = parentContactUri;
    }
    
    public Child(int id, String key, String forename, String surname, String dateOfBirth, String notes, String parentContactUri) {
        super();
        Id = id;
        Key = key;
        Forename = forename;
        Surname = surname;
        DateOfBirth = dateOfBirth;    
        Notes = notes;
        ParentContactUri = parentContactUri;
    }

    public Child(int id, String key, String forename, String surname, String dateOfBirth, String notes, String parentContactUri, byte[] imageData) {
        super();
        Id = id;
        Key = key;
        Forename = forename;
        Surname = surname;
        DateOfBirth = dateOfBirth;    
        Notes = notes;
        ParentContactUri = parentContactUri;
        ImageData = imageData;
    }
    
    private Child(Parcel in) {
        Id = in.readInt();
        Key = in.readString();
        Forename = in.readString();
        Surname = in.readString();
        DateOfBirth = in.readString();
        Notes = in.readString();
        ParentContactUri = in.readString();
        Deleted = (in.readByte() == 1);
        
        ImageData = new byte[in.readInt()];
        in.readByteArray(ImageData);        
    }
    
    @Override
    public String toString() {
        return Forename + " " + Surname;
    }

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(Id);
		dest.writeString(Key);
		dest.writeString(Forename);
		dest.writeString(Surname);
		dest.writeString(DateOfBirth);
		dest.writeString(Notes);
		dest.writeString(ParentContactUri);
		dest.writeByte((byte)(Deleted ? 1 : 0));
		
		if (ImageData != null) {
			dest.writeInt(ImageData.length);			
			dest.writeByteArray(ImageData);
		}
	}    
	
    public static final Parcelable.Creator<Child> CREATOR = new Parcelable.Creator<Child>() {
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        public Child[] newArray(int size) {
            return new Child[size];
        }
    };
}