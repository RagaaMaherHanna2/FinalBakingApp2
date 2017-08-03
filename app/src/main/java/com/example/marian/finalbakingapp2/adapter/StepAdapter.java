package com.example.marian.finalbakingapp2.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marian.finalbakingapp2.R;
import com.example.marian.finalbakingapp2.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.IngredientStepViewHolder>
{

    public static final String STEPS = "steps";
    private final OnStepListener onStepListener;
    private Context context;
    private Recipe recipe;

    public interface OnStepListener
    {
        void onStepSelected(int position);
    }


    public StepAdapter(Context context, Recipe recipe, OnStepListener listener)
    {
        this.context = context;
        this.recipe = recipe;
        onStepListener = listener;
    }

    @Override
    public IngredientStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new IngredientStepViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(IngredientStepViewHolder holder, int position)
    {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return recipe.getSteps().size();
    }


    class IngredientStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tv_ingredient_step)
        TextView mIngredientStep;

        public IngredientStepViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        void onBind(int position)
        {
            mIngredientStep.setText(recipe.getSteps().get(position).getShortDescription());
        }


        @Override
        public void onClick(View v)
        {
            int clickedPosition = getAdapterPosition();
            onStepListener.onStepSelected(clickedPosition);

        }
    }
}
