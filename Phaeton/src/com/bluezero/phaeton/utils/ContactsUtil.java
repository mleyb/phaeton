package com.bluezero.phaeton.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactsUtil {	
	private static final String TAG = ContactsUtil.class.getSimpleName();
	
//	private void retrieveContactPhoto(Context context, String uriContact) {
//
//        Bitmap photo = null;
//
//        try {
//            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
//                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));
//
//            if (inputStream != null) {
//                photo = BitmapFactory.decodeStream(inputStream);
//                ImageView imageView = (ImageView) findViewById(R.id.img_contact);
//                imageView.setImageBitmap(photo);
//            }
//
//            assert inputStream != null;
//            inputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

	public static String getContactId(Context context, String contactUri) {
		String contactId = null;
        Cursor idCursor = null;
        
        try {        
	        idCursor = context.getContentResolver().query(Uri.parse(contactUri), 
	         											  new String[] { ContactsContract.Contacts._ID },
	        											  null, 
	        											  null, 
	        											  null);
	        
	        if (idCursor.moveToFirst()) {
	
	        	contactId = idCursor.getString(idCursor.getColumnIndex(ContactsContract.Contacts._ID));
	        }
        }
	    finally {
        	if (idCursor != null) {
        		idCursor.close();
        	}
        }
        
        return contactId;
	}
	
    public static String getContactNumber(Context context, String contactUri) {
        String phoneNumber = null;
        Cursor phoneCursor = null;
        
        try {        	
	        String contactId = getContactId(context, contactUri);
	        	
	        String phoneQuery = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
					 			ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
					 			ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
	        
	        // using the contact ID now we now get contact phone number
	        phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	                										 new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
	                										 phoneQuery,
	                										 new String[] { contactId },
	                										 null);
	
	        if (phoneCursor.moveToFirst()) {
	            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	        }
	
	        phoneCursor.close();
        }
        finally {       	
        	if (phoneCursor != null) {
        		phoneCursor.close();
        	}
        }
        
        return phoneNumber;
    }

    public static String getContactName(Context context, String contactUri) {
        String name = null;
        Cursor cursor = null;
        
        try {                
        	cursor = context.getContentResolver().query(Uri.parse(contactUri), null, null, null, null);

        	if (cursor.moveToFirst()) {        		
        		name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        	}
        }
        finally {
        	if (cursor != null) {
        		cursor.close();
        	}
        }

        return name;
    }
    
    public static String getContactEmailAddress(Context context, String contactUri) {
        String emailAddress = null;
        Cursor emailCursor = null;
        
        try {        	
	        String contactId = getContactId(context, contactUri);	        
	        							        
	        // using the contact ID now we now get contact email address
	        emailCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
	        												 null,
	        												 ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
	                										 new String[] { contactId },
	                										 null);
	
	        if (emailCursor.moveToFirst()) {
	        	emailAddress = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
	        }	
        }
        finally {       	
        	if (emailCursor != null) {
        		emailCursor.close();
        	}
        }
        
        return emailAddress;
    }
}
