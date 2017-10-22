package com.example.ahmed.thebakingapp.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.thebakingapp.Connection.FetchRecipes;
import com.example.ahmed.thebakingapp.Data.DBUtils;
import com.example.ahmed.thebakingapp.Fragments.RecipesFragment;
import com.example.ahmed.thebakingapp.Models.Recipe;
import com.example.ahmed.thebakingapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipesActivity extends AppCompatActivity {

    public static List<Recipe> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        if (savedInstanceState == null){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            if (!pref.getBoolean("firstTime", false)) {

                try {
                    recipeList  = new FetchRecipes(this).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                bindData();

                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("firstTime", true);
                editor.commit();
            }
            else{
                DBUtils dbUtils = new DBUtils(this);
                recipeList = dbUtils.getAllRecipes();
            }
            bindData();
        }
    }

    public void bindData(){

        RecipesFragment moviesListFragment = new RecipesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.recipesFragmentContainer, moviesListFragment);
        fragmentTransaction.commit();
    }
}