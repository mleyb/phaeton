package com.bluezero.phaeton.utils;

import com.bluezero.phaeton.models.Child;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class IntentUtil {
	public static Intent generateIntent(Context context, Class<?> cls, Child child) {
		Intent intent = new Intent(context, cls);
		intent.putExtra("com.bluezero.phaeton.Child",  child);
		return intent;
	}	
	
	public static Intent generateMarketIntent(String packageName) {		
		return new Intent(Intent.ACTION_VIEW, Uri.parse("http://market.android.com/details?id=" + packageName));
	}
	
	public static Child getChildFromIntent(Intent intent) {
		return (Child)intent.getParcelableExtra("com.bluezero.phaeton.Child");
	}
}
