package com.example.ahmed.thebakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.thebakingapp.Models.Recipe;
import com.example.ahmed.thebakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.ViewHolder>{

    private List<Recipe> recipeList;
    private Context context;
    private int currentId;

    public MyRecipesAdapter(Context context, List<Recipe> recipesList) {
        this.recipeList = recipesList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_name)TextView recipeTitle;
        @BindView(R.id.recipeImage)ImageView recipeImage;
        @BindView(R.id.recipeServings)TextView recipeServings;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onClick(View v) {
        }
    }

    @Override
    public MyRecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyRecipesAdapter.ViewHolder holder, int position) {

        currentId = recipeList.get(position).getId();
        Recipe recipe = recipeList.get(position);
        if (recipe.getImage() == null){
            Picasso.with(this.context)
                    .load(recipe.getImage())
                    .into(holder.recipeImage);
        }

        holder.recipeServings.setText(String.format(Locale.US, "%s %d", "Servings:", recipe.getServings()));
        holder.recipeTitle.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public int getItemPosition(){
        return currentId;
    }
}
