package com.example.marian.finalbakingapp2.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marian.finalbakingapp2.R;
import com.example.marian.finalbakingapp2.activity.StepActivity;
import com.example.marian.finalbakingapp2.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipes;
    private Context context;
    public static final String RECIPE = "recipe";

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes)
    {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder,int position)
    {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

    @BindView(R.id.iv_recipe_image)
    ImageView imageView;
    @BindView(R.id.tv_recipe_name)
    TextView name;

        public RecipeViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void onBind(int position)
        {
            if (!recipes.isEmpty()) {
                if (!recipes.get(position).getImageUrl().isEmpty()) {
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(recipes.get(position).getImageUrl())
                            .into(imageView);
                }
                else
                {
                    imageView.setImageResource(R.drawable.recipe_icon);
                }
                name.setText(recipes.get(position).getName());
            }
        }

        @Override
        public void onClick(View v)
        {
            Intent intent = new Intent(context, StepActivity.class);
            intent.putExtra(RECIPE, recipes.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}
