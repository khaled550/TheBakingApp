package com.example.ahmed.thebakingapp.Data;

import android.provider.BaseColumns;

public class RecipesContract {

    public static class RecipesEntity implements BaseColumns {

        public static final String TABLE_NAME = "recipes";

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_SERVINGS = "servings";

        static final String SQL_QUERY_CREATE =
                "CREATE TABLE " + RecipesEntity.TABLE_NAME + " ("
                        + RecipesEntity.COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY,"
                        + RecipesEntity.COLUMN_NAME + " TEXT NOT NULL UNIQUE,"
                        + RecipesEntity.COLUMN_SERVINGS + " INTEGER NOT NULL,"
                        + RecipesEntity.COLUMN_IMAGE + " TEXT NOT NULL"
                        + ")";
    }
}