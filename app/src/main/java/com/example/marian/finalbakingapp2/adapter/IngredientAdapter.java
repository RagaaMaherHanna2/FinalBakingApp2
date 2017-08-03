package com.example.marian.finalbakingapp2.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marian.finalbakingapp2.R;
import com.example.marian.finalbakingapp2.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> mIngredients;

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position)
    {
        holder.onBind(position);
    }
    @Override
    public int getItemCount() {
        return mIngredients.size() ;
    }


   public class IngredientViewHolder extends RecyclerView.ViewHolder
   {

        @BindView(R.id.tv_ingredient)
        TextView mIngredient;
        @BindView(R.id.tv_measure)
        TextView mMeasure;
        @BindView(R.id.tv_quantity)
        TextView mQuantity;


        public IngredientViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(int position)
        {
            mIngredient.setText(mIngredients.get(position).getAll());
            mMeasure.setText(mIngredients.get(position).getMeasure());
            mQuantity.setText(Integer.toString(mIngredients.get(position).getQuantity()));
        }


    }

}
