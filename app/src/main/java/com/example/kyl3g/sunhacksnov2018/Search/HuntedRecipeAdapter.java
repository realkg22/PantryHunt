package com.example.kyl3g.sunhacksnov2018.Search;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kyl3g.sunhacksnov2018.Objects.Recipes;
import com.example.kyl3g.sunhacksnov2018.R;

import java.util.List;

public class HuntedRecipeAdapter extends ArrayAdapter<Recipes> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private List<Recipes> huntedRecipes;



    static class ViewHolder {
        TextView name;

    }
    public HuntedRecipeAdapter(Context context, int resource, List<Recipes> huntedRecipes){
        super(context, resource, huntedRecipes);
        mContext = context;
        mResource = resource;
        this.huntedRecipes = huntedRecipes;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        final View result;
        final FilteredRecipeAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new FilteredRecipeAdapter.ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.tvHuntedRecipeName);

            convertView.setTag(holder);

            result = convertView;

        } else {
            holder = (FilteredRecipeAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        ImageView removeButton = (ImageView) convertView.findViewById(R.id.ivRemoveRecipe);

        //Make sure if the recipe is removed from the list as well.
        removeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               Firebase.removeHuntedRecipies(getItem(position));
            }
        });
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);
        result.startAnimation(animation);


        holder.name.setText(getItem(position).getName());


        return convertView;
    }
}
