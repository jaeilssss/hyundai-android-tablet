package com.obigo.hkmotors.common.db.data;

import android.provider.BaseColumns;

/**
 * DataBase Table
 */
public final class Obd2Database {
     
    public static final class CreateDB implements BaseColumns {

        public static final String _TABLENAME 		= "ev_param";

        public static final String _TABLENAME_MODE  = "ev_param_mode";

        public static final int ID_IDX 	            = 0;
        public static final int TITLE_IDX 		    = 1;
        public static final int DATE_IDX 		    = 2;

        public static final int PARAM_IDX 	        = 3;
        public static final int RESP_IDX 		    = 4;

        public static final String TITLE 		    = "title";
        public static final String DATE       		= "date";

        public static final String PARAM 		    = "param";
        public static final String RESP       		= "resp";

        public static final String _CREATE 			= "create table " + _TABLENAME + "("
                										+ _ID + " integer primary key autoincrement, "  
                										+ TITLE + " text not null , "
                										+ DATE + " text not null , "
                                                        + PARAM + " text not null , "
                                                        + RESP + " text not null );";

        public static final String _CREATE_MODE		= "create table " + _TABLENAME_MODE + "("
                                                        + _ID + " integer primary key autoincrement, "
                                                        + TITLE + " text not null , "
                                                        + PARAM + " text not null , "
                                                        + RESP + " text not null );";
    }
}
