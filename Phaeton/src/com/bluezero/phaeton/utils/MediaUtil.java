package com.bluezero.phaeton.utils;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;

public class MediaUtil {
		
	public static File createExternalStoragePrivatePicture(Context context, String filename) {
	    // Create a path where we will place our picture in our own private
	    // pictures directory.  Note that we don't really need to place a
	    // picture in DIRECTORY_PICTURES, since the media scanner will see
	    // all media in these directories; this may be useful with other
	    // media types such as DIRECTORY_MUSIC however to help it classify
	    // your media for display to the user.
	    File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
	    File file = new File(path, filename);

//	    try {
//	        // Very simple code to copy a picture from the application's
//	        // resource into the external file.  Note that this code does
//	        // no error checking, and assumes the picture is small (does not
//	        // try to copy it in chunks).  Note that if external storage is
//	        // not currently mounted this will silently fail.
//	        InputStream is = getResources().openRawResource(R.drawable.balloons);
//	        OutputStream os = new FileOutputStream(file);
//	        byte[] data = new byte[is.available()];
//	        is.read(data);
//	        os.write(data);
//	        is.close();
//	        os.close();
//
//	        // Tell the media scanner about the new file so that it is
//	        // immediately available to the user.
//	        MediaScannerConnection.scanFile(this,
//	                new String[] { file.toString() }, null,
//	                new MediaScannerConnection.OnScanCompletedListener() {
//	            public void onScanCompleted(String path, Uri uri) {
//	                Log.i("ExternalStorage", "Scanned " + path + ":");
//	                Log.i("ExternalStorage", "-> uri=" + uri);
//	            }
//	        });
//	    } catch (IOException e) {
//	        // Unable to create file, likely because external storage is
//	        // not currently mounted.
//	        Log.w("ExternalStorage", "Error writing " + file, e);
//	    }
	    
	    return file;
	}

	public static void mediaScanFile(Context context, String path) {
		MediaScannerConnection.scanFile(context, new String[] { path }, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            }
        });
	}
	
	public static void deleteExternalStoragePrivatePicture(Context context, String filename) {
	    // Create a path where we will place our picture in the user's
	    // public pictures directory and delete the file.  If external
	    // storage is not currently mounted this will fail.
	    File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
	    if (path != null) {
	        File file = new File(path, filename);
	        file.delete();
	    }
	}

	public static boolean hasExternalStoragePrivatePicture(Context context, String filename) {
	    // Create a path where we will place our picture in the user's
	    // public pictures directory and check if the file exists.  If
	    // external storage is not currently mounted this will think the
	    // picture doesn't exist.
	    File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
	    if (path != null) {
	        File file = new File(path, filename);
	        return file.exists();
	    }
	    return false;
	}

	public static String getImageFilePath(Context context, Uri uri) {
        String path = null;
        Cursor cursor = null;
        
        try {        
        	cursor = context.getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null, null, null);
        	cursor.moveToFirst();

        	path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        finally {        
        	cursor.close();
        }
        
        return path;
	}
	
	
//	public static final int MEDIA_TYPE_IMAGE = 1;
//	public static final int MEDIA_TYPE_VIDEO = 2;
//
//	/** Create a file Uri for saving an image or video */
//	public static Uri getOutputMediaFileUri(int type){
//	      return Uri.fromFile(getOutputMediaFile(type));
//	}
//
//	/** Create a File for saving an image or video */
//	private static File getOutputMediaFile(int type){
//	    // To be safe, you should check that the SDCard is mounted
//	    // using Environment.getExternalStorageState() before doing this.
//
//	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
//	    // This location works best if you want the created images to be shared
//	    // between applications and persist after your app has been uninstalled.
//
//	    // Create the storage directory if it does not exist
//	    if (! mediaStorageDir.exists()){
//	        if (! mediaStorageDir.mkdirs()){
//	            Log.d("MyCameraApp", "failed to create directory");
//	            return null;
//	        }
//	    }
//
//	    // Create a media file name
//	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//	    File mediaFile;
//	    if (type == MEDIA_TYPE_IMAGE){
//	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//	        "IMG_"+ timeStamp + ".jpg");
//	    } else if(type == MEDIA_TYPE_VIDEO) {
//	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//	        "VID_"+ timeStamp + ".mp4");
//	    } else {
//	        return null;
//	    }
//
//	    return mediaFile;
//	}
}
