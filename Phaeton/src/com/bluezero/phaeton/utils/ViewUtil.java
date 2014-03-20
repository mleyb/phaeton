package com.bluezero.phaeton.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bluezero.phaeton.R;

public final class ViewUtil {	
	public static void showErrorDialog(Context context, String message) {
		ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Phaeton);
        AlertDialog.Builder builder= new AlertDialog.Builder(ctw);
		builder.setTitle(R.string.app_name);
		builder.setIcon(android.R.drawable.ic_dialog_alert);		
		builder.setMessage(message);
		builder.setCancelable(false);
				
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void showPurchaseThanksDialog(Context context, String message) {
		ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Phaeton);
        AlertDialog.Builder builder= new AlertDialog.Builder(ctw);
		builder.setTitle(R.string.app_name);
		builder.setIcon(android.R.drawable.ic_dialog_info);		
		builder.setMessage(message);
		builder.setCancelable(false);
				
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static void showToast(Context context, String text) {    	
    	LayoutInflater inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    	
    	View toastRoot = inflater.inflate(R.layout.toast, null);
    	
    	((TextView)toastRoot.findViewById(R.id.toastTextView)).setText(text);
    	
    	ContextThemeWrapper ctw = new ContextThemeWrapper(context, R.style.Phaeton);    	
    	Toast toast = new Toast(ctw);    	

    	toast.setView(toastRoot);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.show();
    }
	
	public static void setEditTextError(EditText editText, String message) {
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		editText.setError(message);		
	}
}
