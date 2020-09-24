package com.example.kyl3g.sunhacksnov2018.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.kyl3g.sunhacksnov2018.BottomNavigationViewHelper;
import com.example.kyl3g.sunhacksnov2018.Callback.UserCallback;
import com.example.kyl3g.sunhacksnov2018.Objects.User;
import com.example.kyl3g.sunhacksnov2018.Pantry.PantryPage;
import com.example.kyl3g.sunhacksnov2018.R;
import com.example.kyl3g.sunhacksnov2018.Search.Firebase;
import com.example.kyl3g.sunhacksnov2018.Search.Step1RecipeList;
import com.example.kyl3g.sunhacksnov2018.Search.Step3Map;

public class ProfilePage extends AppCompatActivity {

    BottomNavigationView navBar;
    private TextView mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        mUsername = (TextView) findViewById(R.id.username);

        initializeNavigationBar();

        Firebase.callCurrentUser(new UserCallback() {
            @Override
            public void callback(User callback) {
                mUsername.setText(callback.getName());
            }
        });

    }

    private void initializeNavigationBar(){
        navBar = (BottomNavigationView) findViewById(R.id.navigationBar);
        BottomNavigationViewHelper.disableShiftMode(navBar);
        Menu menu = navBar.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_pantry:
                        startActivity(new Intent(ProfilePage.this, PantryPage.class));
                        item.setIcon(R.drawable.ic_pantry);
                        break;
                    case R.id.navigation_search:
                        startActivity(new Intent(ProfilePage.this, Step1RecipeList.class));
                        item.setIcon(R.drawable.ic_search);
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(ProfilePage.this, ProfilePage.class));
                        item.setIcon(R.drawable.ic_profile);
                        break;

                }
                return false;
            }
        });
    }


}
