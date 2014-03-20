package com.bluezero.phaeton.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	
	private static final String DATABASE_NAME = "phaeton.db";
	private static final int DATABASE_VERSION = 1;
	
	public static final String CHILDREN_TABLE_NAME = "children";
	
	public static final String[] CHILDREN_TABLE_COLUMNS = new String[] { "_id", 
																		 "key", 
																		 "forename", 
																		 "surname", 
																		 "dateOfBirth",
																		 "notes",
																		 "parentContactUri",
																		 "deleted",
																		 "imageData" };
	
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CHILDREN_TABLE_NAME + " ("
        		+ CHILDREN_TABLE_COLUMNS[0] + " INTEGER PRIMARY KEY,"
                + CHILDREN_TABLE_COLUMNS[1] + " TEXT NOT NULL,"
                + CHILDREN_TABLE_COLUMNS[2] + " TEXT NOT NULL,"
                + CHILDREN_TABLE_COLUMNS[3] + " TEXT NOT NULL,"
                + CHILDREN_TABLE_COLUMNS[4] + " TEXT NOT NULL,"
                + CHILDREN_TABLE_COLUMNS[5] + " TEXT,"
                + CHILDREN_TABLE_COLUMNS[6] + " TEXT,"
                + CHILDREN_TABLE_COLUMNS[7] + " BOOLEAN,"
                + CHILDREN_TABLE_COLUMNS[8] + " BLOB"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CHILDREN_TABLE_NAME);
        onCreate(db);
    }
}
