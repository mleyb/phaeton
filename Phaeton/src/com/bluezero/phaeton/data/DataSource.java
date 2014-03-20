package com.bluezero.phaeton.data;

import java.util.ArrayList;
import java.util.List;

import com.bluezero.phaeton.models.Child;
import com.bugsense.trace.BugSenseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {
	
	private DatabaseHelper _dbHelper;
	private SQLiteDatabase _db;	
	
	public DataSource(Context context) {
		_dbHelper = new DatabaseHelper(context);
        establishDb();
    }        
    
    public void cleanup() {
        if (_db != null) {
            _db.close();
            _db = null;
        }
    }      
    
    public void insert(Child child) {
        ContentValues values = getContentValues(child);
        _db.insert(DatabaseHelper.CHILDREN_TABLE_NAME, null, values);
    }
    
    public void update(Child child) {
    	ContentValues values = getContentValues(child);
        _db.update(DatabaseHelper.CHILDREN_TABLE_NAME, values, "_id=" + child.Id, null);
    }
    
    public void delete(int id) {
        _db.delete(DatabaseHelper.CHILDREN_TABLE_NAME, "_id=" + id, null);
    }

	public List<Child> getAll(boolean includeDeleted) {
        ArrayList<Child> children = new ArrayList<Child>();
        Cursor c = null;
        try {        	
            c = _db.query(DatabaseHelper.CHILDREN_TABLE_NAME, DatabaseHelper.CHILDREN_TABLE_COLUMNS, null, null, null, null, null);
            int numRows = c.getCount();
            c.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
            	boolean deleted = (c.getInt(7) == 1);
            	if (!deleted || deleted && includeDeleted) {                	            	            	
            		Child child = new Child(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getBlob(8));
            		child.Deleted = deleted;
            		children.add(child);            		
            	}
            	
            	c.moveToNext();
            }                      
        }
        catch (SQLException e) {
            Log.e("DataSource", e.toString());
            BugSenseHandler.sendException(e);
        } 
        finally 
        {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return children;
    }
    
    private void establishDb() {
        if (_db == null) {
            _db = _dbHelper.getWritableDatabase();
        }
    }    
    
    private ContentValues getContentValues(Child child) {
    	ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[0], child.Id);
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[1], child.Key);
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[2], child.Forename);
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[3], child.Surname);
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[4], child.DateOfBirth);
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[5], child.Notes);
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[6], child.ParentContactUri);
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[7], child.Deleted);
        values.put(DatabaseHelper.CHILDREN_TABLE_COLUMNS[8], child.ImageData);
        return values;
    }
}
