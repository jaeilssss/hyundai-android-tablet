package com.obigo.hkmotors.common.db;

import android.content.Context;

import com.obigo.hkmotors.common.db.helper.Obd2DBOpenHelper;


/**
 * Utility for the database
 */
public class DBUtil {

	/**
	 * Get the count of row
	 *
	 * @param context - context
	 */
	public static int getRowCount(Context context) {
		int count = 0;
		Obd2DBOpenHelper mHelper = new Obd2DBOpenHelper(context);
		mHelper.open();

		count = mHelper.getRowCount();
		mHelper.close();

		return count;
	}

	/**
	 * Create or Insert information about EV parameters
	 *
	 * @param context - context
	 * @param title - title
	 * @param date - date : "2017.10.11 13:40"
	 * @param param - param for 7 parameters : "78:5:3:4:89:120:23"
	 * @param resp - resp for 5 response values : "3.4:2.1:3.3:2.4:5.0"
	 */
	public static void insertDB(Context context, String title, String date, String param, String resp) {
		Obd2DBOpenHelper mHelper = new Obd2DBOpenHelper(context);
		mHelper.open();

		//create the field of table
		mHelper.insert(title, date, param, resp);
		mHelper.close();
	}

	/**
	 * Create or Insert information about EV parameters
	 *
	 * @param context - context
	 * @param title - title
	 * @param param - param for 7 parameters : "78:5:3:4:89:120:23"
	 * @param resp - resp for 5 response values : "3.4:2.1:3.3:2.4:5.0"
	 */
	public static void insertModeDB(Context context, String title, String param, String resp) {
		Obd2DBOpenHelper mHelper = new Obd2DBOpenHelper(context);
		mHelper.open();

		//create the field of table
		mHelper.insertMode(title, param, resp);
		mHelper.close();
	}

	/**
	 * Update title by uniqu id
	 *
	 * @param context - context
	 * @param id - unique id
	 * @param title - title to update
	 */
	public static void updateDB(Context context, int id, String title) {
		Obd2DBOpenHelper mHelper = new Obd2DBOpenHelper(context);
		mHelper.open();

		mHelper.update(id, title);
		mHelper.close();
	}

	/**
	 * Delete information by unique id
	 *
	 * @param context - context
	 * @param id - unique id
	 */
	public static void deleteDB(Context context, int id) {
		Obd2DBOpenHelper mHelper = new Obd2DBOpenHelper(context);
		mHelper.open();

		mHelper.delete(id);
		mHelper.close();
	}

	/**
	 * Delete multiple items by ids
	 *
	 * @param context - context
	 * @param ids - ids means 1,2,3
	 */
	public static void deleteMultipleDB(Context context, String ids) {
		Obd2DBOpenHelper mHelper = new Obd2DBOpenHelper(context);
		mHelper.open();

		mHelper.deleteM(ids);
		mHelper.close();
	}
}
