package com.bluezero.phaeton;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class ActionSelection {
    public Class<?> _activityClass;
    public String _title;
    public String _subtitle;
    public int _imageResourceId;
    
    public Class<?> getActivityClass() {
    	return _activityClass;
    }
    
    public String getTitle() {
    	return _title;
    }
    
    public String getSubtitle() {
    	return _subtitle;
    }
    	    
    public int getImageResourceId() {
    	return _imageResourceId;
    }
    
    public ActionSelection(Class<?> activityClass, String title, String subtitle, int imageResourceId) {
        super();
        _activityClass = activityClass;
        _title = title;
        _subtitle = subtitle;
        _imageResourceId = imageResourceId;
    }

    @Override
    public String toString() {
        return _title;
    } 
    
    public static ActionSelection[] getAllSelections(Context context) {
		List<ActionSelection> items = new ArrayList<ActionSelection>();
	
		items.add(new ActionSelection(LogActivityActivity.class, "Activity", context.getString(R.string.activity_list_item), R.drawable.activity));
		items.add(new ActionSelection(LogBottleActivity.class, "Bottle", context.getString(R.string.bottle_list_item), R.drawable.bottle));
		items.add(new ActionSelection(LogDrinkActivity.class, "Drink", context.getString(R.string.drink_list_item), R.drawable.drink));
		items.add(new ActionSelection(LogFirstAidActivity.class, "First Aid", context.getString(R.string.first_aid_list_item), R.drawable.firstaid));
		items.add(new ActionSelection(LogMealActivity.class, "Meal", context.getString(R.string.meal_list_item), R.drawable.meal));
		items.add(new ActionSelection(LogMedicineActivity.class, "Medicine", context.getString(R.string.medicine_list_item), R.drawable.medicine));
		items.add(new ActionSelection(LogMilestoneActivity.class, "Milestone", context.getString(R.string.milestone_list_item), R.drawable.milestone));
		items.add(new ActionSelection(LogNappyActivity.class, "Nappy", context.getString(R.string.nappy_list_item), R.drawable.nappy));
		items.add(new ActionSelection(LogNoteActivity.class, "Note", context.getString(R.string.note_list_item), R.drawable.note));
		items.add(new ActionSelection(LogSickActivity.class, "Sick", context.getString(R.string.sick_list_item), R.drawable.sick));
		items.add(new ActionSelection(LogSleepActivity.class, "Sleep", context.getString(R.string.sleep_list_item), R.drawable.sleep));
		items.add(new ActionSelection(LogSnackActivity.class, "Snack", context.getString(R.string.snack_list_item), R.drawable.snack));
		
		return items.toArray(new ActionSelection[items.size()]);		
	}
}
