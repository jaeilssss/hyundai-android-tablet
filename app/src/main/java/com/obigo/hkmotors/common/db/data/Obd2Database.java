package com.obigo.hkmotors.common.db.data;

import android.provider.BaseColumns;

/**
 * DataBase Table
 */
public final class Obd2Database {
     
    public static final class CreateDB implements BaseColumns {

        public static final String _TABLENAME 		= "my_car";
        public static final String _TABLENAME_MODE  = "my_car_mode";

        public static final int ID_IDX 	            = 0;
        public static final int TITLE_IDX 		    = 1;
        public static final int DATE_IDX 		    = 2;

        public static final int PARAM_IDX 	        = 3;
        public static final int RESP_IDX 		    = 4;

        public static final String TITLE 		    = "title";
        public static final String DATE       		= "date";

        public static final String SIGNAL1		    = "signal1";
        public static final String SIGNAL2          = "signal2";
        public static final String RESP       		= "resp";

        public static final String _CREATE 			= "create table " + _TABLENAME + "("
                										+ _ID + " integer primary key autoincrement, "  
                										+ TITLE + " text not null , "
                										+ DATE + " text not null , "
                                                        + SIGNAL1 + " text not null , "
                                                        + SIGNAL2 + " text not null );";

        public static final String _CREATE_MODE		= "create table " + _TABLENAME_MODE + "("
                                                        + _ID + " integer primary key autoincrement, "
                                                        + TITLE + " text not null , "
                                                        + SIGNAL1 + " text not null , "
                                                        + SIGNAL2 + " text not null );";
    }
}
