package com.example.ahmed.thebakingapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ahmed.thebakingapp.Models.Ingredient;
import com.example.ahmed.thebakingapp.Models.Recipe;
import com.example.ahmed.thebakingapp.Models.Step;

import java.util.ArrayList;
import java.util.List;

public class DBUtils extends SQLiteOpenHelper {
    private String TAG = DBUtils.class.getSimpleName();

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public DBUtils(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IngredientsContract.IngredientEntity.SQL_QUERY_CREATE);
        db.execSQL(StepsContarct.StepsEntity.SQL_QUERY_CREATE);
        db.execSQL(RecipesContract.RecipesEntity.SQL_QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipesContract.RecipesEntity.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StepsContarct.StepsEntity.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StepsContarct.StepsEntity.TABLE_NAME);
        onCreate(db);
    }

    public void insertRecipe(Recipe recipe)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(RecipesContract.RecipesEntity.COLUMN_RECIPE_ID, recipe.getId());
        contentValues.put(RecipesContract.RecipesEntity.COLUMN_NAME, recipe.getName());
        contentValues.put(RecipesContract.RecipesEntity.COLUMN_SERVINGS,recipe.getServings());
        contentValues.put(RecipesContract.RecipesEntity.COLUMN_IMAGE,recipe.getImage());

        db.insert(RecipesContract.RecipesEntity.TABLE_NAME, null, contentValues);
    }

    public void insertIngredient(Ingredient ingredient)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(IngredientsContract.IngredientEntity.COLUMN_RECIPE_ID, ingredient.getRecipeId());
        contentValues.put(IngredientsContract.IngredientEntity.COLUMN_INGREDIENT,ingredient.getIngredientName());
        contentValues.put(IngredientsContract.IngredientEntity.COLUMN_QUANTITY,ingredient.getQuantity());
        contentValues.put(IngredientsContract.IngredientEntity.COLUMN_MEASURE,ingredient.getMeasure());

        db.insert(IngredientsContract.IngredientEntity.TABLE_NAME, null, contentValues);
    }

    public void insertStep(Step step)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(StepsContarct.StepsEntity.COLUMN_RECIPE_ID, step.getRecipeId());
        contentValues.put(StepsContarct.StepsEntity.COLUMN_STEP_ID,step.getId());
        contentValues.put(StepsContarct.StepsEntity.COLUMN_SHORT_DESC,step.getShortDescription());
        contentValues.put(StepsContarct.StepsEntity.COLUMN_DESC,step.getDescription());
        contentValues.put(StepsContarct.StepsEntity.COLUMN_VIDEO_URL,step.getVideoURL());
        contentValues.put(StepsContarct.StepsEntity.COLUMN_THUMB_URL,step.getThumbnailURL());

        db.insert(StepsContarct.StepsEntity.TABLE_NAME, null, contentValues);
    }

    public Recipe getSingleRecipe(int recipeId){

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + RecipesContract.RecipesEntity.TABLE_NAME + " WHERE "
                + RecipesContract.RecipesEntity.COLUMN_RECIPE_ID + " = " + recipeId;

        Log.e(TAG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Recipe obj = new Recipe();

        obj.setId(Integer.parseInt(cursor.getString(0)));
        obj.setName(cursor.getString(1));
        obj.setServings(Integer.parseInt(cursor.getString(2)));
        obj.setImage(cursor.getString(3));

        return obj;
    }

    public List<Step> getRecipeSteps(int recipeId){
        List<Step> steps = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + StepsContarct.StepsEntity.TABLE_NAME + " WHERE "
                + StepsContarct.StepsEntity.COLUMN_RECIPE_ID + " = " + recipeId;

        Log.e(TAG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do{
                Step obj = new Step();
                obj.setRecipeId(Integer.parseInt(cursor.getString(1)));
                obj.setId(Integer.parseInt(cursor.getString(2)));
                obj.setVideoURL(cursor.getString(3));
                obj.setShortDescription(cursor.getString(4));
                obj.setDescription(cursor.getString(5));
                obj.setThumbnailURL(cursor.getString(6));

                steps.add(obj);
            }
            while (cursor.moveToNext());
        }
        return steps;
    }

    public List<Ingredient> getRecipeIngrdients(int recipeId){
        List<Ingredient> ingredients = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + IngredientsContract.IngredientEntity.TABLE_NAME + " WHERE "
                + IngredientsContract.IngredientEntity.COLUMN_RECIPE_ID + " = " + recipeId;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        do {
            Ingredient obj = new Ingredient();

            obj.setRecipeId(Integer.parseInt(c.getString(1)));
            obj.setIngredientName(c.getString(2));
            obj.setQuantity(c.getFloat(3));
            obj.setMeasure(c.getString(4));

            ingredients.add(obj);
        }
        while (c.moveToNext());

        return ingredients;
    }
}
