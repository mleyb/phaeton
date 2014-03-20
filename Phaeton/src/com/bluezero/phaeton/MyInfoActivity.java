package com.bluezero.phaeton;

import android.os.Bundle;
import android.widget.TextView;

public class MyInfoActivity extends PhaetonActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       
        setContentView(R.layout.my_info_activity);   
                        
        super.setHeaderText(R.id.headerTextView, R.string.my_info_header);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        ((TextView)findViewById(R.id.nameTextView)).setText(SessionInfo.DisplayName);        
	}
}
