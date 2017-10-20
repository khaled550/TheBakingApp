package com.example.ahmed.thebakingapp.Data;

import android.provider.BaseColumns;

public class IngredientsContract {

    public static class IngredientEntity implements BaseColumns {

        public static final String TABLE_NAME = "ingredients";

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";

        static final String SQL_QUERY_CREATE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_RECIPE_ID + " INTEGER NOT NULL,"
                        + COLUMN_INGREDIENT + " TEXT UNIQUE NOT NULL,"
                        + COLUMN_QUANTITY + " REAL NOT NULL,"
                        + COLUMN_MEASURE + " TEXT NOT NULL"
                        + ")";
    }
}
