package com.example.ahmed.thebakingapp.Data;

import android.provider.BaseColumns;

public class StepsContarct {

    public static class StepsEntity implements BaseColumns {

        public static final String TABLE_NAME = "steps";

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_STEP_ID = "step_id";
        public static final String COLUMN_DESC = "desc";
        public static final String COLUMN_SHORT_DESC = "short_desc";
        public static final String COLUMN_VIDEO_URL = "video";
        public static final String COLUMN_THUMB_URL = "thumb";

        static final String SQL_QUERY_CREATE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_RECIPE_ID + " INTEGER NOT NULL,"
                        + COLUMN_STEP_ID + " INTEGER UNIQUE NOT NULL,"
                        + COLUMN_VIDEO_URL + " TEXT NOT NULL,"
                        + COLUMN_SHORT_DESC + " TEXT NOT NULL,"
                        + COLUMN_DESC + " TEXT NOT NULL,"
                        + COLUMN_THUMB_URL + " TEXT NOT NULL"
                        + ")";
    }
}
