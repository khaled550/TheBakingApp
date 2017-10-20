package com.example.ahmed.thebakingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.thebakingapp.Activities.MediaActivity;
import com.example.ahmed.thebakingapp.Adapters.MyRecipesInfoAdapter;
import com.example.ahmed.thebakingapp.Adapters.RecyclerTouchListener;
import com.example.ahmed.thebakingapp.Data.DBUtils;
import com.example.ahmed.thebakingapp.Models.Ingredient;
import com.example.ahmed.thebakingapp.Models.Step;
import com.example.ahmed.thebakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipesInfoFragment extends Fragment {

    List<Ingredient> ingredientList = new ArrayList<>();
    public static List<Step> stepList = new ArrayList<>();
    List<Object> items = new ArrayList<>();
    RecyclerView recyclerView;
    MyRecipesInfoAdapter myRecipesInfoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_info, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        int currentRecipeId = bundle.getInt("current_id");
        DBUtils dbUtils = new DBUtils(getActivity());
        ingredientList = dbUtils.getRecipeIngrdients(currentRecipeId);
        stepList = dbUtils.getRecipeSteps(currentRecipeId);

        items.addAll(ingredientList);
        items.addAll(stepList);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recipesInfoRecycler);

        myRecipesInfoAdapter = new MyRecipesInfoAdapter(getActivity(), ingredientList, stepList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(myRecipesInfoAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity()
                , recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Step step = stepList.get(position);

                if (isBigScreen(getActivity())){
                    FragmentManager fm = getChildFragmentManager();

                    MediaFragment mediaFragment = new MediaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("STEP_ID",step.getId());
                    bundle.putString("DESC",step.getDescription());
                    bundle.putString("STEP_URL",step.getVideoURL());
                    bundle.putString("THUMB_URL",step.getThumbnailURL());
                    mediaFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.stepMediaContainer, mediaFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else {
                    Intent intent = new Intent(getActivity(), MediaActivity.class)
                            .putExtra("STEP_ID",step.getId())
                            .putExtra("DESC",step.getDescription())
                            .putExtra("STEP_URL",step.getVideoURL())
                            .putExtra("THUMB_URL",step.getThumbnailURL());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    public static boolean isBigScreen(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
