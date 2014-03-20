package com.bluezero.phaeton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.StrictMode;

public class StrictModeWrapper { 
	@TargetApi(11) 
	public static void init(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// check if android:debuggable is set to true		
			int appFlags = context.getApplicationInfo().flags; 
			if ((appFlags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) { 
				StrictMode.setThreadPolicy( 
						new StrictMode.ThreadPolicy.Builder() 
							.detectDiskReads() 
							.detectDiskWrites() 
							.detectNetwork() 
							.penaltyLog() 
							.build()); 
				StrictMode.setVmPolicy( 
						new StrictMode.VmPolicy.Builder() 
							.detectLeakedSqlLiteObjects() 
							.penaltyLog() 
							.penaltyDeath() 
							.build()); 
			}
		}
	} 
} 
