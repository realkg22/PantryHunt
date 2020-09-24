package com.example.kyl3g.sunhacksnov2018.Search;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kyl3g.sunhacksnov2018.Objects.Recipes;
import com.example.kyl3g.sunhacksnov2018.R;

import java.util.List;

public class FilteredRecipeAdapter extends ArrayAdapter<Recipes> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private List<Recipes> filteredRecipes;



    static class ViewHolder {
        TextView name;

    }
    public FilteredRecipeAdapter(Context context, int resource, List<Recipes> filteredRecipes){
        super(context, resource, filteredRecipes);
        mContext = context;
        mResource = resource;
        this.filteredRecipes = filteredRecipes;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        final View result;
        final FilteredRecipeAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new FilteredRecipeAdapter.ViewHolder();

            holder.name = (TextView) convertView.findViewById(R.id.tvFilteredRecipeName);

            convertView.setTag(holder);

            result = convertView;

        } else {
            holder = (FilteredRecipeAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

        Button addButton = (Button) convertView.findViewById(R.id.btnAddRecipe);

        //ADD FRIEND TO PARTY
        addButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Add to party when clicked
                Firebase.addHuntedRecipes(getItem(position));
                filteredRecipes.remove(position);
                notifyDataSetChanged();

            }
        });
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);
        result.startAnimation(animation);

        holder.name.setText(getItem(position).getName());


        return convertView;
    }
}
