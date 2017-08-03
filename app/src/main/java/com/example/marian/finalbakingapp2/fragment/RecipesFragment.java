package com.example.marian.finalbakingapp2.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.marian.finalbakingapp2.R;
import com.example.marian.finalbakingapp2.adapter.IngredientAdapter;
import com.example.marian.finalbakingapp2.adapter.StepAdapter;
import com.example.marian.finalbakingapp2.model.Ingredient;
import com.example.marian.finalbakingapp2.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.marian.finalbakingapp2.adapter.RecipeAdapter.RECIPE;

public class RecipesFragment extends Fragment
{


    private ArrayList<Ingredient> ingredients;
    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;
    private Recipe recipe;

    @BindView(R.id.rv_ingredients_steps)
    RecyclerView ingredientAndStepsRV;
    @BindView(R.id.rv_ingredients)
    RecyclerView ingredientRV;


    private StepAdapter.OnStepListener onStepListener;

    public RecipesFragment()
    {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            recipe = savedInstanceState.getParcelable(RECIPE);
        }
        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);

        recipe = getActivity().getIntent().getParcelableExtra(RECIPE);
        ingredients = recipe.getAlls();

        stepAdapter = new StepAdapter(getContext(), recipe, onStepListener);
        ingredientAndStepsRV.setAdapter(stepAdapter);

        ingredientAdapter = new IngredientAdapter(ingredients);
        ingredientRV.setAdapter(ingredientAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE, recipe);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            onStepListener = (StepAdapter.OnStepListener) context;
        }
        catch (ClassCastException e)
        {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

}
