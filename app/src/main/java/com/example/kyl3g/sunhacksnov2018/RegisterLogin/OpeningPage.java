package com.example.kyl3g.sunhacksnov2018.RegisterLogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.kyl3g.sunhacksnov2018.R;
import com.example.kyl3g.sunhacksnov2018.Search.Step1RecipeList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class OpeningPage extends AppCompatActivity {

    private ImageView mAppLogo;
    private EditText mEmailInput, mPasswordInput;
    private Button mLoginBtn, mRegisterBtn;

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    private ViewSwitcher mViewSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_opening);
        initializeElements();

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();




    }

    private void initializeElements(){
        mAppLogo = (ImageView) findViewById(R.id.appLogo);
        mEmailInput = (EditText) findViewById(R.id.emailInput);
        mPasswordInput = (EditText) findViewById(R.id.passwordInput);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mRegisterBtn = (Button) findViewById(R.id.registerBtn);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        mViewSwitcher.showNext();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mEmailInput.getText().toString().trim();
                String pass = mPasswordInput.getText().toString().trim();


                //    r1 = new restaurnat(cheap,highcalorie,healthy);
                // myRef.child("restaurant").setValue(r1);


                if (!user.isEmpty() || !pass.isEmpty()) {//checks if either box is empty
                    mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast toast = Toast.makeText(OpeningPage.this, "Logging in", Toast.LENGTH_SHORT);
                                toast.show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(OpeningPage.this, Step1RecipeList.class);
                                finish();
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(OpeningPage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                else
                {
                    //if fails
                    Toast.makeText(OpeningPage.this, "One or more empty field",
                            Toast.LENGTH_SHORT).show();
                }




            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpeningPage.this, RegisterStep1.class));
            }
        });
    }


}
