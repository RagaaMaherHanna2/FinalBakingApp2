package com.example.marian.finalbakingapp2.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.marian.finalbakingapp2.R;
import com.example.marian.finalbakingapp2.adapter.RecipeAdapter;
import com.example.marian.finalbakingapp2.model.Recipe;
import com.example.marian.finalbakingapp2.network.ApiClient;
import com.example.marian.finalbakingapp2.network.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.marian.finalbakingapp2.adapter.RecipeAdapter.RECIPE;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.rv_recipe)
    RecyclerView rv_recipe;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            rv_recipe.setLayoutManager(new GridLayoutManager(this, 1));
        }
        else
        {
            rv_recipe.setLayoutManager(new GridLayoutManager(this, 2));
        }

//              on saved Instance State
        if (savedInstanceState != null)
        {
            recipes = savedInstanceState.getParcelableArrayList(RECIPE);
            setRecipes();
        } else {
            loadRecipes();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE, recipes);
    }

    private void setRecipes()
    {
        progressBar.setVisibility(View.INVISIBLE);

        RecipeAdapter recipeAdapter = new RecipeAdapter(MainActivity.this, recipes);
        rv_recipe.setAdapter(recipeAdapter);
    }

    private void loadRecipes()
    {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Type TYPE = new TypeToken<ArrayList<Recipe>>()
        {}.getType();

        Call<JsonArray> call = apiInterface.getRecipe();

        call.enqueue(new Callback<JsonArray>()
        {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response)
            {
                recipes = new Gson().fromJson(response.body(), TYPE);
                setRecipes();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
            }
        });
    }


}
