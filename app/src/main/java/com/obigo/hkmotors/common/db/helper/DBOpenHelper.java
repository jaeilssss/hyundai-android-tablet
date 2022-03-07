package com.obigo.hkmotors.common.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.obigo.hkmotors.common.db.data.Database;


public class DBOpenHelper extends SQLiteOpenHelper {
 
	private final static String TAG = "DBOpenHelper";
	
    private static final String DATABASE_NAME = "obd2.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public DBOpenHelper(Context context, String name,
                        CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    // Call first one
    @Override
    public void onCreate(SQLiteDatabase db) {

    	try {
    		db.execSQL(Database.CreateDB._CREATE);
            db.execSQL(Database.CreateDB._CREATE_MODE);
    	} catch (Exception e) {
    		Log.e(DBOpenHelper.TAG, "Exception in CREATE_SQL");
    	}
    }


    // Update Database when version is updated
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    	try {
    		db.execSQL("DROP TABLE IF EXISTS " + Database.CreateDB._TABLENAME);
            db.execSQL("DROP TABLE IF EXISTS " + Database.CreateDB._TABLENAME_MODE);
    	} catch (Exception e) {
    		Log.e(DBOpenHelper.TAG, "Exception in DROP_SQL");
    	}
        onCreate(db);
    } 
}