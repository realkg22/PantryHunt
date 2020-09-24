package com.example.kyl3g.sunhacksnov2018.Search;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kyl3g.sunhacksnov2018.Objects.ResultObject;
import com.example.kyl3g.sunhacksnov2018.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;




/**
 * Created by adria on 6/12/2018.
 */

public class UpdatedGroceryListAdapter extends ArrayAdapter<ResultObject> {
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private ViewHolder holder;
    int row_index;
    TextView users;
    private DatabaseReference mRef;



    static class ViewHolder {
        TextView tvIngredientName;
        TextView tvGroceryName;
        TextView tvPrice;
        TextView tvAmount;

    }


    public UpdatedGroceryListAdapter(Context context, int resource, List<ResultObject> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String users = "";

        String ingredientName = getItem(position).getName() + "";
        String groceryName = getItem(position).getGrocery();
        double price = getItem(position).getPrice();
        int amount = getItem(position).getAmount();

        final View result;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder.tvIngredientName = (TextView) convertView.findViewById(R.id.tvIngredientName);
            holder.tvGroceryName = (TextView) convertView.findViewById(R.id.tvGroceryName);
            holder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);

            convertView.setTag(holder);

            result = convertView;

        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition)? R.anim.loading_down_anim: R.anim.loading_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.tvIngredientName.setText(ingredientName);
        //holder.Direction.setText(users);
        holder.tvGroceryName.setText(groceryName);
        holder.tvPrice.setText("$" + price + "");
        holder.tvAmount.setText(amount + " item");

        return convertView;
    }






}
