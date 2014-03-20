package com.bluezero.phaeton.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bluezero.phaeton.SessionInfo;
import com.bluezero.phaeton.models.Child;

public class SmsUtil {
	public static void launchNewChildRegistrationSmsIntent(Context context, Child child, String uri) {
		
		String appName = context.getString(com.bluezero.phaeton.R.string.app_name);	
		
		StringBuilder sb = new StringBuilder();
		sb.append("Hi! ").append(SessionInfo.DisplayName).append(" has just registered ").append(child.toString())
		.append(" in ").append(appName).append(" online.\n").append("You can now view ").append(child.Forename)
		.append("'s busy day by visiting ").append(uri).append(" and signing into the secure portal.\n");
		
		launchSmsIntent(context, child.ParentContactUri, sb.toString());		
	}
	
	public static void launchSmsIntent(Context context, String contactUri, String body) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("vnd.android-dir/mms-sms");
		intent.setData(Uri.parse("sms:" + ContactsUtil.getContactNumber(context, contactUri)));
		
		if (body != null) {
			intent.putExtra("sms_body", body); 
		}
		
		context.startActivity(Intent.createChooser(intent, "Send message..."));	
	}
}
