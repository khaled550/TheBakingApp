package com.example.ahmed.thebakingapp.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ahmed.thebakingapp.Data.DBUtils;
import com.example.ahmed.thebakingapp.Models.Ingredient;
import com.example.ahmed.thebakingapp.Models.Recipe;
import com.example.ahmed.thebakingapp.Models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchRecipes extends AsyncTask<Void, Void, List<Recipe>> {

    private String TAG = FetchRecipes.class.getSimpleName();
    private List<Recipe> recipeList = new ArrayList<>();
    private List<Ingredient> ingredientList = new ArrayList<>();
    private List<Step> stepList = new ArrayList<>();
    private Context context;

    public FetchRecipes(Context context){
        this.context = context;
    }

    @Override
    protected List<Recipe> doInBackground(Void... arg0) {
        HttpHandler httpHandler = new HttpHandler();

        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        String jsonStr = httpHandler.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr != null) {
            try {

                JSONArray recipesArray = new JSONArray(jsonStr);
                Log.i("JSONArrayLeng", recipesArray.length() + "");
                for (int i = 0; i < recipesArray.length(); i++) {
                    Recipe recipe = new Recipe();
                    JSONObject r = recipesArray.getJSONObject(i);
                    recipe.setId(Integer.parseInt(r.getString("id")));
                    recipe.setName(r.getString("name"));
                    recipe.setServings(r.getInt("servings"));
                    recipe.setImage(r.getString("image"));

                    JSONArray ingredientsArray = r.getJSONArray("ingredients");

                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        JSONObject ingredients = ingredientsArray.getJSONObject(j);
                        int recipeId = Integer.parseInt(r.getString("id"));
                        double quantity = ingredients.getDouble("quantity");
                        String measure = ingredients.getString("measure");
                        String ingredient = ingredients.getString("ingredient");

                        Ingredient ingredientRecipe = new Ingredient(recipeId,measure,quantity,ingredient);

                        ingredientList.add(ingredientRecipe);
                    }

                    JSONArray stepsArray = r.getJSONArray("steps");

                    for (int m = 0; m < stepsArray.length(); m++) {
                        JSONObject steps = stepsArray.getJSONObject(m);
                        int recipeId = Integer.parseInt(r.getString("id"));
                        int id = steps.getInt("id");
                        String shortDescription = steps.getString("shortDescription");
                        String description = steps.getString("description");
                        String videoURL = steps.getString("videoURL");
                        String thumbnailURL = steps.getString("thumbnailURL");

                        Step stepRecipe = new Step(recipeId,id,shortDescription,description,videoURL,thumbnailURL);

                        stepList.add(stepRecipe);
                    }
                    recipeList.add(recipe);
                }

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            };

        }

        return recipeList;
    }

    @Override
    protected void onPostExecute(List<Recipe> result) {
        if (result == null){
            Toast.makeText(this.context, "Error Loading Data", Toast.LENGTH_LONG).show();
        }
        for (int i =0;i < result.size();i++){
            DBUtils dbUtils = new DBUtils(context);
            dbUtils.insertRecipe(result.get(i));
        }

        for (int j =0;j < ingredientList.size();j++){
            DBUtils dbUtils = new DBUtils(context);
            dbUtils.insertIngredient(ingredientList.get(j));
        }

        for (int l =0;l < stepList.size();l++){
            DBUtils dbUtils = new DBUtils(context);
            dbUtils.insertStep(stepList.get(l));
        }
    }
}
