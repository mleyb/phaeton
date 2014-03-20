package com.bluezero.phaeton;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bluezero.phaeton.utils.IntentUtil;

public class SelectActionActivity extends LogActionActivityBase {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_action_activity);
                
        super.setHeaderText(R.id.headerTextView, R.string.log_new_activity_header);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        populateListView();
                
        ListView listView = (ListView) findViewById(R.id.selectActionListView);
   		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				onListItemClick(adapterView, position);
			}
		});
    }
		
	protected void onListItemClick(AdapterView<?> adapterView, int position) {
		ActionSelection selection = (ActionSelection)adapterView.getItemAtPosition(position);
		Intent intent = IntentUtil.generateIntent(this,  selection.getActivityClass(), getChild());		
		startActivity(intent);
	}
	
	private void populateListView() {
		ActionSelection[] selections = ActionSelection.getAllSelections(getApplicationContext());
		ListView listView = (ListView)findViewById(R.id.selectActionListView);            		
        listView.setAdapter(new ActionSelectionArrayAdapter(this, selections));
	}
		
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
    		// app icon in action bar clicked; go to previous activity
            Intent intent = new Intent(this, SelectChildActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
    	}
    	else {
    		return super.onOptionsItemSelected(item);
    	}
    }	
	
	public void saveData() {
		// Nothing to do
	}
	
	public class ActionSelectionArrayAdapter extends TwoLineArrayAdapter<ActionSelection> {
	    public ActionSelectionArrayAdapter(Context context, ActionSelection[] selections) {
	        super(context, R.layout.select_action_list_item, selections);
	    }

	    @Override
	    public String lineOneText(ActionSelection selection) {
	        return selection.getTitle();
	    }

	    @Override
	    public String lineTwoText(ActionSelection selection) {
	    	return selection.getSubtitle();
	    }

		@Override
		public int lineImageResourceId(ActionSelection selection) {
			return selection.getImageResourceId();
		}
	}
}