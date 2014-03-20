package com.bluezero.phaeton;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;

public class GCMBroadcastReceiver extends BroadcastReceiver {
	private final Context _context;	
	private final IntentFilter _gcmFilter;
	
	public GCMBroadcastReceiver(Context context) {
		_context = context;
		
		_gcmFilter = new IntentFilter();
        _gcmFilter.addAction(Constants.GCM.GCM_RECEIVED_ACTION);
	}
	
	public void register() {
		_context.registerReceiver(this, _gcmFilter);
	}
	
	public void unregister() {
		_context.unregisterReceiver(this);
	}
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
			.setSmallIcon(R.drawable.ic_stat_notification)
			.setContentTitle("New Notification!")
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setContentText(intent.getStringExtra("message"));
				
		Notification notification = builder.build();			
	
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	
		notificationManager.notify(0, notification);
    }
}
