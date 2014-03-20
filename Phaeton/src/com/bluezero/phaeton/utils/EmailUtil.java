package com.bluezero.phaeton.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bluezero.phaeton.SessionInfo;
import com.bluezero.phaeton.models.Child;

public class EmailUtil {
	public static void launchNewChildRegistrationEmailIntent(Context context, Child child, String uri) {
		
		String appName = context.getString(com.bluezero.phaeton.R.string.app_name);	

		String subject = appName + " Registration";
		
		StringBuilder sb = new StringBuilder();
		sb.append("Hi! ").append(SessionInfo.DisplayName).append(" has just registered ").append(child.toString())
		.append(" in ").append(appName).append(" online.\n").append("You can now view ").append(child.Forename)
		.append("'s busy day by visiting ").append(uri).append(" and signing into the secure portal.\n");
		
		launchEmailIntent(context, child.ParentContactUri, subject, sb.toString());		
	}
	
	public static void launchEmailIntent(Context context, String contactUri, String subject, String body) {
		String emailAddress = ContactsUtil.getContactEmailAddress(context, contactUri);
		if (emailAddress != null) {		
			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailAddress, null));
		
			if (subject != null) {			
				intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			}
		
			if (body != null) {
				intent.putExtra(Intent.EXTRA_TEXT, body); 
			}
		
			context.startActivity(Intent.createChooser(intent, "Send email..."));
		}
		else {
			ViewUtil.showToast(context, "Contact has no email address!");
		}
	}
}
