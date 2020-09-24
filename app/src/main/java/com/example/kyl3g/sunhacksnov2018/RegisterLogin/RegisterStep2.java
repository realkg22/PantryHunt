package com.example.kyl3g.sunhacksnov2018.RegisterLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.kyl3g.sunhacksnov2018.Objects.DietPattern;
import com.example.kyl3g.sunhacksnov2018.Objects.User;
import com.example.kyl3g.sunhacksnov2018.R;
import com.example.kyl3g.sunhacksnov2018.Search.Step1RecipeList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterStep2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    Bundle b;
    private String id, email, fullname;
    private boolean whiteMeat, redMeat, seafood;

    private CheckBox cbWhiteMeat, cbRedMeat, cbSeafood;
    private Button mFinishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_step2);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        b = this.getIntent().getExtras();

        initializeElements();
    }

    private void initializeElements(){
        cbWhiteMeat = (CheckBox) findViewById(R.id.cbWhiteMeat);
        cbRedMeat = (CheckBox) findViewById(R.id.cbRedMeat);
        cbSeafood = (CheckBox) findViewById(R.id.cbSeafood);
        mFinishBtn = (Button) findViewById(R.id.finishBtn);

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(b != null){
                    email = (String) b.getString("username");
                    fullname = (String) b.getString("fullname");
                }

                id = mUser.getUid();//gets unique id
                DietPattern diet = new DietPattern(whiteMeat,redMeat,seafood);
                User user = new User(id, email, fullname, diet);
                //  mDatabase.child("Users").child(id).child("diet").setValue(diet);
                //mDatabase.child("Users").child(id).child("friendsList").setValue(list);

                mDatabase.child("Users").child(id).setValue(user);
                Toast.makeText(RegisterStep2.this, "Account Created!",
                        Toast.LENGTH_LONG).show();

                Intent intent = new Intent(RegisterStep2.this, Step1RecipeList.class);
                finish();
                startActivity(intent);
            }

        });
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()) {
            case R.id.cbWhiteMeat:
                if(checked) {whiteMeat = true;}
                else{ whiteMeat = false; }
                break;
            case R.id.cbRedMeat:
                if(checked) {redMeat = true;}
                else{ redMeat = false; }
                break;
            case R.id.cbSeafood:
                if(checked) {seafood = true;}
                else{ seafood = false; }
                break;
        }
    }



}
