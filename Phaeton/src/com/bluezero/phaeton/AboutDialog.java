package com.bluezero.phaeton;

import android.app.Dialog;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutDialog {
	public static void show(Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);		
		dialog.setTitle(R.string.app_name);			
		dialog.setContentView(R.layout.about_dialog);
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_info);
		
		((TextView)dialog.findViewById(R.id.text)).setText(context.getString(R.string.about));		
		((ImageView)dialog.findViewById(R.id.image)).setImageResource(R.drawable.ic_launcher);
		((TextView)dialog.findViewById(R.id.link)).setText(context.getString(R.string.app_link));
 
		((TextView)dialog.findViewById(R.id.link)).setMovementMethod(LinkMovementMethod.getInstance());
		
		Button dialogButton = (Button)dialog.findViewById(R.id.dialogButtonOK);

		dialogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();					
			}
		}); 
		
		dialog.show();
    }
}
