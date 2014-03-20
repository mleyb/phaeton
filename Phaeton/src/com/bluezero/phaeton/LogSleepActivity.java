package com.bluezero.phaeton;

import android.os.Bundle;

import com.bluezero.phaeton.models.Sleep;
import com.bluezero.phaeton.tasks.LogSleepTask;
import com.bluezero.phaeton.utils.DateTimeUtil;
import com.bluezero.phaeton.utils.IAsyncTaskResultHandler;
import com.bluezero.phaeton.widgets.TimePickerButton;

public class LogSleepActivity extends LogActionActivityBase implements IAsyncTaskResultHandler {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_sleep_activity);            
        
        super.setHeaderText(R.id.headerTextView, R.string.sleep_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
	
    public void saveData() {
		String date = DateTimeUtil.getCurrentDateJSONString();
		String start = ((TimePickerButton)findViewById(R.id.startTimeButton)).getUtcFormattedDateString();
		String end = ((TimePickerButton)findViewById(R.id.endTimeButton)).getUtcFormattedDateString();
		
		new LogSleepTask(this, this, getChild().Id).execute(new Sleep(date, start, end));
    }	
}
