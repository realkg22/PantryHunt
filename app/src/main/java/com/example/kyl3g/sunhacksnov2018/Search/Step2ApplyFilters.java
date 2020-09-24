package com.example.kyl3g.sunhacksnov2018.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.kyl3g.sunhacksnov2018.BottomNavigationViewHelper;
import com.example.kyl3g.sunhacksnov2018.Callback.GroceryListCallback;
import com.example.kyl3g.sunhacksnov2018.Objects.Grocery;
import com.example.kyl3g.sunhacksnov2018.Pantry.PantryPage;
import com.example.kyl3g.sunhacksnov2018.Profile.ProfilePage;
import com.example.kyl3g.sunhacksnov2018.R;

import java.util.ArrayList;
import java.util.List;

public class Step2ApplyFilters extends AppCompatActivity {

    BottomNavigationView navBar;
    private boolean distanceFilter, priceFilter;
    private RadioButton mDistanceRadio, mPriceRadio;
    private Spinner mNumGroceriesSpinner;
    private int numRestaurants;

    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_step2);


        initializeElements();
        initializeNavigationBar();

    }

    private void initializeElements(){
        mDistanceRadio = (RadioButton) findViewById(R.id.radioDistance);
        mPriceRadio = (RadioButton) findViewById(R.id.radioPrice);
        mNumGroceriesSpinner = (Spinner) findViewById(R.id.spinnerNumGroceries);
        mNextButton = (Button) findViewById(R.id.nextButton);

        Integer[] spinnerItems = new Integer[]{1,2,3,4};
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<Integer>(this,R.layout.support_simple_spinner_dropdown_item,spinnerItems);
        mNumGroceriesSpinner.setAdapter(spinnerAdapter);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numRestaurants = Integer.parseInt(mNumGroceriesSpinner.getSelectedItem().toString());
                if (distanceFilter && !priceFilter)
                {

                    Intent intent = new Intent(Step2ApplyFilters.this, Step3Map.class);
                    intent.putExtra("RadioResult", 0);
                    intent.putExtra("NumOfStore", numRestaurants);

                    startActivity(intent);
/*
                    Firebase.readGrocery(new GroceryListCallback() {
                        @Override
                        public void callback(List<Grocery> callback) {
                            ArrayList<Grocery> groceries = (ArrayList)callback;

                            for (int i = 0;i < groceries.size();i++ )
                            {
                                double lon1 = -111.933693;
                                double lon2 = groceries.get(i).getLongCoordinate();
                                double lat1 = 33.418562;
                                double lat2 = groceries.get(i).getLatCoordinate();
                                double dlon = Math.abs(lon2 - lon1);
                                double dlat = Math.abs(lat2 - lat1);
                                double a = 12430*dlat/180;

                                double b = 24901*dlon/360*Math.cos((lat1+lat2)/2);

                                double d = Math.pow((a*a+b*b),.5);
                                groceries.get(i).setDistance(d);

                            }
                            groceries.sort(new DistanceComparator());
                            String distance = "";
                            for (int x =0; x < groceries.size();x++)
                            {
                                distance += groceries.get(x).getDistance() + "; ";
                            }





                        }
                    });
                    */
                }
                else if (priceFilter && !distanceFilter)
                {
                    Intent intent = new Intent(Step2ApplyFilters.this, Step3Map.class);
                    intent.putExtra("RadioResult", 1);
                    intent.putExtra("NumOfStore", numRestaurants);

                    startActivity(intent);

                }
                else{}
              //  startActivity(new Intent(Step2ApplyFilters.this, Step3Map.class));
            }
        });

    }

    private void initializeNavigationBar(){
        navBar = (BottomNavigationView) findViewById(R.id.navigationBar);
        BottomNavigationViewHelper.disableShiftMode(navBar);
        Menu menu = navBar.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_pantry:
                        startActivity(new Intent(Step2ApplyFilters.this, PantryPage.class));
                        item.setIcon(R.drawable.ic_pantry);
                        break;
                    case R.id.navigation_search:
                        startActivity(new Intent(Step2ApplyFilters.this, Step1RecipeList.class));
                        item.setIcon(R.drawable.ic_search);
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(Step2ApplyFilters.this, ProfilePage.class));
                        item.setIcon(R.drawable.ic_profile);
                        break;

                }
                return false;
            }
        });
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.radioDistance:
                if(checked) {distanceFilter = true;}
                else {distanceFilter = false;}
                break;
            case R.id.radioPrice:
                if(checked) {priceFilter = true;}
                else {priceFilter = false;}
                break;
        }
    }
}
