package com.example.kyl3g.sunhacksnov2018.Search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.kyl3g.sunhacksnov2018.BottomNavigationViewHelper;
import com.example.kyl3g.sunhacksnov2018.Callback.RecipeListCallback;
import com.example.kyl3g.sunhacksnov2018.Callback.UserCallback;
import com.example.kyl3g.sunhacksnov2018.Objects.Recipes;
import com.example.kyl3g.sunhacksnov2018.Objects.User;
import com.example.kyl3g.sunhacksnov2018.Pantry.PantryPage;
import com.example.kyl3g.sunhacksnov2018.Profile.ProfilePage;
import com.example.kyl3g.sunhacksnov2018.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class Step1RecipeList extends AppCompatActivity {

    private ImageButton mNextButton;
    private ListView mHuntedRecipeList, mRecommendedRecipeList;
    private RelativeLayout mSlideLayout;

    BottomNavigationView navBar;
    BottomSheetBehavior mBottomSheetBehavior;

    private List<Recipes> recommendedRecipeList;
    private List<Recipes> huntedRecipeList;
    private FilteredRecipeAdapter fra;
    private HuntedRecipeAdapter hra;
    private static DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_step1);
        isReadPermissionGranted();
        isWritePermissionGranted();
        JsonAccessor.createJsonIfNotExists();
        JsonAccessor.JsonReset();
        initializeElements();
        initializeNavigationBar();


        recommendedRecipeList = new ArrayList<Recipes>();
        huntedRecipeList = new ArrayList<Recipes>();
        mRecommendedRecipeList = (ListView) findViewById(R.id.lvRecipeList);
        mHuntedRecipeList = (ListView) findViewById(R.id.lvHuntedRecipeList);
        fra = new FilteredRecipeAdapter(this, R.layout.search_step1_recipe_adapter, recommendedRecipeList);
        hra = new HuntedRecipeAdapter(this, R.layout.search_step1_grocerylist_adapter, huntedRecipeList);
        mRecommendedRecipeList.setAdapter(fra);
        mHuntedRecipeList.setAdapter(hra);
        Firebase.callCurrentUser(new UserCallback() {
            @Override
            public void callback(User callback) {
                final User user = callback;

                Log.d("Calling current User", "true");
                Firebase.readRecipes(new RecipeListCallback() {

                    @Override
                    public void callback(List<Recipes> callback) {
                        recommendedRecipeList.clear();
                        fra.clear();

                        for (int i = 0; i < callback.size(); i++) {
                            if (user.isCompatible(callback.get(i).getDietPattern()) && !user.checkDuplicateRecipes(callback.get(i))) {
                                recommendedRecipeList.add(callback.get(i));
                                fra.notifyDataSetChanged();
                                mRecommendedRecipeList.setAdapter(fra);
                            }
                        }
                    }
                });
            }
        });

        Firebase.autoUpdateCurrentUser(new UserCallback() {
            @Override
            public void callback(final User callback) {
                huntedRecipeList.clear();
                hra.clear();
                List<Recipes> huntedRecipes = callback.getHuntedRecipes();
                for(int i = 0; i < huntedRecipes.size(); i++){

                    huntedRecipeList.add(huntedRecipes.get(i));
                    hra.notifyDataSetChanged();
                    mHuntedRecipeList.setAdapter(hra);
                }

            }
        });

        mRecommendedRecipeList.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow NestedScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


    }

    private void initializeElements(){
        mSlideLayout = (RelativeLayout) findViewById(R.id.bottomSlide);
        mBottomSheetBehavior = BottomSheetBehavior.from(mSlideLayout);
        mNextButton = (ImageButton) findViewById(R.id.nextButton);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Step1RecipeList.this, Step2ApplyFilters.class));
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
                        startActivity(new Intent(Step1RecipeList.this, PantryPage.class));
                        item.setIcon(R.drawable.ic_pantry);
                        break;
                    case R.id.navigation_search:
                        startActivity(new Intent(Step1RecipeList.this, Step1RecipeList.class));
                        item.setIcon(R.drawable.ic_search);
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(Step1RecipeList.this, ProfilePage.class));
                        item.setIcon(R.drawable.ic_profile);
                        break;

                }
                return false;
            }
        });

    }

    public  boolean isWritePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Msg","Permission is granted");
                return true;
            } else {

                Log.v("Msg","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Msg","Permission is granted");
            return true;
        }
    }

    public boolean isReadPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Msg", "Permission is granted");
                return true;
            } else {

                Log.v("Msg", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Msg", "Permission is granted");
            return true;
        }
    }
}
