package com.example.ahmed.thebakingapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.thebakingapp.Activities.RecipeInfoActivity;
import com.example.ahmed.thebakingapp.Adapters.MyRecipesAdapter;
import com.example.ahmed.thebakingapp.Adapters.RecyclerTouchListener;
import com.example.ahmed.thebakingapp.R;

import static com.example.ahmed.thebakingapp.Activities.RecipesActivity.recipeList;

public class RecipesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyRecipesAdapter myRecipesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recipesRecycler);

        myRecipesAdapter = new MyRecipesAdapter(getActivity(), recipeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(myRecipesAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity()
                , recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), RecipeInfoActivity.class)
                        .putExtra("current_id",recipeList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
}
