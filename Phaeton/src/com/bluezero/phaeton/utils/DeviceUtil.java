package com.bluezero.phaeton.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;

public class DeviceUtil {
	private static Boolean _hasRearCamera;
	private static Boolean _hasTelephony;
	
	public static boolean hasRearCamera() {
		if (_hasRearCamera == null) {
			_hasRearCamera = Boolean.valueOf(getHasRearCamera());
		}
		
		return _hasRearCamera.booleanValue();
	}
	
	public static boolean hasTelephony(Context context) {
		if (_hasTelephony == null) {
			_hasTelephony = Boolean.valueOf(getHasTelephony(context));
		}
		
		return _hasTelephony.booleanValue();
	}
	
	private static boolean getHasRearCamera() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			int numberOfCameras = Camera.getNumberOfCameras();	
			
			for (int i = 0; i < numberOfCameras; i++) {
				Camera.getCameraInfo(i, cameraInfo);
				
				if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {              
					return true;
				}
			}
		}
		return false;		
	}
	
	private static boolean getHasTelephony(Context context) {
		boolean hasTelephony = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);		
		return hasTelephony;
	}
}
