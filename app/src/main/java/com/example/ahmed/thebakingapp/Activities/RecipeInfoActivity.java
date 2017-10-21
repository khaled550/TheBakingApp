package com.example.ahmed.thebakingapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.ahmed.thebakingapp.Fragments.MediaFragment;
import com.example.ahmed.thebakingapp.Fragments.RecipesInfoFragment;
import com.example.ahmed.thebakingapp.R;
import com.example.ahmed.thebakingapp.Widgets.IngredientsWidget;

public class RecipeInfoActivity extends AppCompatActivity {

    public boolean twoPane;
    public static final String BASE_FRAGMENT = "BSE_FRAGMENT";
    public static final String INFO_FRAGMENT = "INFO_FRAGMENT";
    String lastFragment;
    int currentId = 0;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        twoPane = findViewById(R.id.LinearLayout)!=null;
        linearLayout = (LinearLayout) findViewById(R.id.LinearLayout);

        if (savedInstanceState!=null) {
            lastFragment = savedInstanceState.getString("lastSinglePaneFragment");
        }

        Intent intent = getIntent();
        currentId = intent.getIntExtra("current_id", 0);
        Bundle bundle = new Bundle();
        bundle.putInt("current_id", currentId);

        FragmentManager fm = getSupportFragmentManager();
        if (!RecipesInfoFragment.isBigScreen(this) && fm.findFragmentById(R.id.recipesInfoFragmentContainerSingle)==null) {
            RecipesInfoFragment baseFragment = getDetatchedMasterFragment(false);
            baseFragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.recipesInfoFragmentContainerSingle, baseFragment, BASE_FRAGMENT).commit();

            if (lastFragment != null && lastFragment.equals(INFO_FRAGMENT)) {
                openSinglePaneDetailFragment();
            }
        }
        if (RecipesInfoFragment.isBigScreen(this) && fm.findFragmentById(R.id.recipesInfoFragmentContainerDual)==null) {
            RecipesInfoFragment baseFragment = getDetatchedMasterFragment(true);
            baseFragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.recipesInfoFragmentContainerDual, baseFragment, BASE_FRAGMENT).commit();
        }
        if (RecipesInfoFragment.isBigScreen(this) && fm.findFragmentById(R.id.stepMediaContainer)==null) {
            MediaFragment infoFragment = getDetatchedDetailFragment();
            fm.beginTransaction().add(R.id.stepMediaContainer, infoFragment, INFO_FRAGMENT).commit();
        }
    }

    private void openSinglePaneDetailFragment() {

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        MediaFragment detailFragment = getDetatchedDetailFragment();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.recipesInfoFragmentContainerSingle, detailFragment, INFO_FRAGMENT);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private RecipesInfoFragment getDetatchedMasterFragment(boolean popBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        RecipesInfoFragment baseFragment = (RecipesInfoFragment) getSupportFragmentManager().findFragmentByTag(BASE_FRAGMENT);
        if (baseFragment == null) {
            baseFragment = new RecipesInfoFragment();
        } else {
            if (popBackStack) {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fm.beginTransaction().remove(baseFragment).commit();
            fm.executePendingTransactions();
        }
        return baseFragment;
    }

    private MediaFragment getDetatchedDetailFragment() {
        FragmentManager fm = getSupportFragmentManager();
        MediaFragment infoFragment = (MediaFragment) getSupportFragmentManager().findFragmentByTag(INFO_FRAGMENT);
        if (infoFragment == null) {
            infoFragment = new MediaFragment();
        } else {
            fm.beginTransaction().remove(infoFragment).commit();
            fm.executePendingTransactions();
        }
        return infoFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_info_widget, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.setWid) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("RecipeIdWidget", currentId).apply();
            IngredientsWidget.refreshIngredientsWidgets(this);
            Snackbar.make(linearLayout,"Recipe Igredients Added to Widget",Snackbar.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

}