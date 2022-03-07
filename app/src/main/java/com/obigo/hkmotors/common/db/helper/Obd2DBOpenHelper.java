package com.obigo.hkmotors.common.db.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.obigo.hkmotors.common.db.data.Database;


public class Obd2DBOpenHelper {
 
	private final static String TAG = "Obd2DBOpenHelper";

    public static SQLiteDatabase mDB;
    private Obd2DatabaseHelper mObd2DBHelper;
    private Context mCtx;

    /**
	 * DatabaseHelper for information about obd2 parameters
	 */
    private class Obd2DatabaseHelper extends DBOpenHelper {
 
        // Constructor
    	// defulat db, version is used, so db name and version is null and zero.
        public Obd2DatabaseHelper(Context context, String name,
                                  CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
 
        // Call first one
        @Override
        public void onCreate(SQLiteDatabase db) {
        	super.onCreate(db);
 
        }
        
        // Update Database when version is updated
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	super.onUpgrade(db, oldVersion, newVersion);

        }
    }

    /**
	 * Coustructor for Obd2DBOpenHelper
	 *
	 * @param context
     */
    public Obd2DBOpenHelper(Context context){
        this.mCtx = context;
    }
    
    public Obd2DBOpenHelper open() throws SQLException {
		mObd2DBHelper = new Obd2DatabaseHelper(mCtx, null, null, 0);
        mDB = mObd2DBHelper.getWritableDatabase();
        return this;
    }

    /**
	 * Close for Obd2DBOpenHelper
	 */
    public void close(){
        mDB.close();
    }


    /**
	 * Insert information to Obd2Database
	 *
	 * @param title - title
	 * @param date - date
	 * @param signal1 - param for 7 parameters
	 * @param signal2 - resp for 5 response value
     * @return
     */
	public long insert(String title, String date, String signal1, String signal2) {
		
		ContentValues values = new ContentValues();

		values.put(Database.CreateDB.TITLE, title);
		values.put(Database.CreateDB.DATE, date);
		values.put(Database.CreateDB.SIGNAL1, signal1);
		values.put(Database.CreateDB.SIGNAL2, signal2);

		return mDB.insert(Database.CreateDB._TABLENAME, null, values);
	}

    /**
     * Insert information to Obd2Database
     *
     * @param title - title
     * @param param - param for 7 parameters
     * @param resp - resp for 5 response value
     * @return
     */
    public long insertMode(String title, String param, String resp) {

        ContentValues values = new ContentValues();

        values.put(Database.CreateDB.TITLE, title);
        values.put(Database.CreateDB.SIGNAL1, param);
        values.put(Database.CreateDB.SIGNAL2, resp);

        return mDB.insert(Database.CreateDB._TABLENAME_MODE, null, values);
    }

	/**
	 * Update information to Obd2Database
	 * (Only, title will be updated by unique id)
	 *
	 * @param id - unique id
	 * @param title - title
     * @return
     */
	public boolean update(int id, String title,String signal1, String signal2) {
		
		ContentValues values = new ContentValues();
		values.put(Database.CreateDB.TITLE, title);
		values.put(Database.CreateDB.SIGNAL1,signal1);
		values.put(Database.CreateDB.SIGNAL2,signal2);
		return mDB.update(Database.CreateDB._TABLENAME, values, "_id = " + id, null) > 0;
	}

	/**
	 * Delete column by unique id
     *
	 * @param id - unique id
	 */
	public boolean delete(int id){
        return mDB.delete(Database.CreateDB._TABLENAME, "_id = " + id, null) > 0;
	}

    /**
     * Delete multiple columns by unique id
     *
     * @param ids - {1,2,3}
     */
    public boolean deleteM(String ids){
        return mDB.delete(Database.CreateDB._TABLENAME, "_id IN (" + ids + ")", null) > 0;

    }

	/**
	 * Get whole column
     *
	 * @return
	 */
	public Cursor getAll(){
		return mDB.query(Database.CreateDB._TABLENAME, null, null, null, null, null, Database.CreateDB._ID + " ASC"); // ASC : 내림차순, DESC : 오름차순
	}

	/**
	 * Get whole column
	 *
	 * @return
	 */
	public Cursor getModeAll(){
		return mDB.query(Database.CreateDB._TABLENAME_MODE, null, null, null, null, null, Database.CreateDB._ID + " ASC"); // ASC : 내림차순, DESC : 오름차순
	}
	/**
	 * get the count of whole rows.
	 * @return
	 */
	public int getRowCount() {
		int count = 0;
		Cursor cursor = null;
		try {
			cursor = mDB.query(Database.CreateDB._TABLENAME, null, null, null, null, null, null);
			count = cursor.getCount();
			if (count < 0) {
				count = 0;
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return count;
	}

	/**
	 * get the count of whole rows.
	 * @return
	 */
	public int getModeRowCount() {
		int count = 0;
		Cursor cursor = null;
		try {
			cursor = mDB.query(Database.CreateDB._TABLENAME_MODE, null, null, null, null, null, null);
			count = cursor.getCount();
			if (count < 0) {
				count = 0;
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return count;
	}

	/**
	 * get the count of whole columns.
	 * @return
	 */
	public int getColumnCount() {
		int count = 0;
		Cursor cursor = null;
		try {
			cursor = mDB.query(Database.CreateDB._TABLENAME, null, null, null, null, null, null);
			count = cursor.getColumnCount();
			if (count < 0) {
				count = 0;
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return count;
	}

}