package com.example.ahmed.thebakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.thebakingapp.Models.Ingredient;
import com.example.ahmed.thebakingapp.Models.Step;
import com.example.ahmed.thebakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyRecipesInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public class ViewHolder0 extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name)TextView ingTitle;

        public ViewHolder0(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.stepTitle)TextView stepTitle;
        @BindView(R.id.stepImage)ImageView stepImage;

        public ViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
        }
    }
    private Context context;
    List<Object> infoList;
    List<Ingredient> ingredientList = new ArrayList<>();
    List<Step> stepList = new ArrayList<>();
    boolean isDisplayed = false;
    private final int INGREIENT = 0, STEP = 1;


    public MyRecipesInfoAdapter(Context context, List<Object> infoList) {
        this.infoList = infoList;
        this.context = context;

        for (int i=0;i<infoList.size();i++){
            if (infoList.get(i) instanceof Ingredient)
                ingredientList.add((Ingredient) infoList.get(i));
            else
                stepList.add((Step) infoList.get(i));
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position))
            return INGREIENT;

        return STEP;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case INGREIENT:
                View v1 = inflater.inflate(R.layout.ingredient_item, parent, false);
                viewHolder = new ViewHolder0(v1);
                break;
            case STEP:
                View v2 = inflater.inflate(R.layout.step_item, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case INGREIENT:
                if (!isDisplayed){
                    isDisplayed = true;
                    for (Ingredient ingredient :ingredientList){
                        ViewHolder0 viewHolder0 = (ViewHolder0)holder;
                        String row = String.format(Locale.US, "%s %s %s %s %.1f %s %s %s",viewHolder0.ingTitle.getText(),
                                "-", ingredient.getIngredientName(),"   (", ingredient.getQuantity(),")   ", ingredient.getMeasure(),"\n");
                        viewHolder0.ingTitle.setText(row);
                    }
                }
                break;

            case STEP:
                ViewHolder2 viewHolder2 = (ViewHolder2)holder;
                Step step = stepList.get(position);
                viewHolder2.stepTitle.setText(step.getShortDescription());
                if (step.getThumbnailURL() == null){
                    Picasso.with(this.context)
                            .load(step.getThumbnailURL())
                            .into(viewHolder2.stepImage);
                }
                break;
        }

    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return infoList.size()-ingredientList.size();
    }
}