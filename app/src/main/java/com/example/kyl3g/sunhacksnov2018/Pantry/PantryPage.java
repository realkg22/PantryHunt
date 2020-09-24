package com.example.kyl3g.sunhacksnov2018.Pantry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kyl3g.sunhacksnov2018.BottomNavigationViewHelper;
import com.example.kyl3g.sunhacksnov2018.Profile.ProfilePage;
import com.example.kyl3g.sunhacksnov2018.R;
import com.example.kyl3g.sunhacksnov2018.Search.Step1RecipeList;
import com.example.kyl3g.sunhacksnov2018.Search.Step2ApplyFilters;

import java.util.ArrayList;
import java.util.List;

public class PantryPage extends AppCompatActivity {

    BottomNavigationView navBar;
    private ListView mPantryList;

    List<String> pantryList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantry_list);

        pantryList = new ArrayList<String>();
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pantryList);

        pantryList.add("Mayonnaise");
        pantryList.add("Olive Oil");
        pantryList.add("Eggs");
        pantryList.add("Brown Sugar");
        pantryList.add("Vinegar");
        pantryList.add("Salt");
        pantryList.add("Pepper");
        pantryList.add("Enchilada Seasoning");
        pantryList.add("Soy Sauce");
        pantryList.add("Flour");
        pantryList.add("Sriracha");
        pantryList.add("Ketchup");

        mPantryList = (ListView) findViewById(R.id.lvPantryList);
        mPantryList.setAdapter(itemAdapter);

        initializeNavigationBar();

    }

    private void initializeNavigationBar(){
        navBar = (BottomNavigationView) findViewById(R.id.navigationBar);
        BottomNavigationViewHelper.disableShiftMode(navBar);
        Menu menu = navBar.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_pantry:
                        startActivity(new Intent(PantryPage.this, PantryPage.class));
                        item.setIcon(R.drawable.ic_pantry);
                        break;
                    case R.id.navigation_search:
                        startActivity(new Intent(PantryPage.this, Step1RecipeList.class));
                        item.setIcon(R.drawable.ic_search);
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(PantryPage.this, ProfilePage.class));
                        item.setIcon(R.drawable.ic_profile);
                        break;

                }
                return false;
            }
        });
    }

}
