package com.example.ahmed.thebakingapp.Widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.ahmed.thebakingapp.Data.DBUtils;
import com.example.ahmed.thebakingapp.Models.Ingredient;
import com.example.ahmed.thebakingapp.Models.Recipe;
import com.example.ahmed.thebakingapp.R;

import java.util.List;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    public static final String IDS_KEY ="Ingredientswidgetids";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Recipe recipe;
        List<Ingredient> ingredients;
        DBUtils dBsqlite = new DBUtils(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int recipeIdWidget = preferences.getInt("RecipeIdWidget", 1);
        Log.e("ID",String.valueOf(recipeIdWidget));
        recipe = dBsqlite.getSingleRecipe(recipeIdWidget);
        ingredients = dBsqlite.getRecipeIngrdients(recipeIdWidget);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        for (int i =0;i<appWidgetIds.length;i++){

            views.setTextViewText(R.id.widgetRecipeTitle, recipe.getName());
            views.removeAllViews(R.id.widgetIngContainer);

            for (Ingredient ingredient : ingredients) {
                RemoteViews ingView = new RemoteViews(context.getPackageName(), R.layout.widget_item);
                String row = String.format(Locale.US, "%s %s %s %.1f %s %s","*"
                        , ingredient.getIngredientName(),"   (", ingredient.getQuantity(),")   ", ingredient.getMeasure());
                ingView.setTextViewText(R.id.widgetIngredientName, row);
                views.addView(R.id.widgetIngContainer, ingView);
            }
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int[] ids={};

        if (intent.hasExtra(IDS_KEY)) {
            ids = intent.getExtras().getIntArray(IDS_KEY);
            this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
        } else super.onReceive(context, intent);
    }

    public static void refreshIngredientsWidgets(Context context) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(
                new ComponentName(context,IngredientsWidget.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(IDS_KEY, ids);
        context.sendBroadcast(updateIntent);
    }
}

