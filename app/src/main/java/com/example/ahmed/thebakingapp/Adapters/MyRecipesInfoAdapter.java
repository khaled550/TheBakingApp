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

import java.util.List;
import java.util.Locale;

public class MyRecipesInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public class ViewHolder0 extends RecyclerView.ViewHolder {

        TextView ingTitle;

        public ViewHolder0(View view) {
            super(view);
            ingTitle = (TextView) view.findViewById(R.id.ingredient_name);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView stepTitle;
        ImageView stepImage;

        public ViewHolder2(View itemView) {
            super(itemView);
            stepTitle = (TextView) itemView.findViewById(R.id.stepTitle);
            stepImage = (ImageView) itemView.findViewById(R.id.stepImage);
        }

        @Override
        public void onClick(View v) {
        }
    }
    private Context context;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    boolean isDisplayed = false;
    private final int INGREIENT = 0, STEP = 1;


    public MyRecipesInfoAdapter(Context context, List<Ingredient> ingredientList, List<Step> stepList) {
        this.ingredientList = ingredientList;
        this.stepList = stepList;
        this.context = context;
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
                    for (Ingredient ingredient : ingredientList){
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
        return stepList.size();
    }
}
