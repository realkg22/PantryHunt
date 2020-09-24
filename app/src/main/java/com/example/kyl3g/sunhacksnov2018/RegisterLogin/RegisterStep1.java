package com.example.kyl3g.sunhacksnov2018.RegisterLogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kyl3g.sunhacksnov2018.Objects.DietPattern;
import com.example.kyl3g.sunhacksnov2018.Objects.User;
import com.example.kyl3g.sunhacksnov2018.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterStep1 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    private EditText mNameInput, mPasswordInput, mVerifyPassword, mEmailInput;
    private Button mNextButton;

    String id;
    DietPattern diet;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_step1);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initializeElements();

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mNameInput.getText().toString().matches("") || mEmailInput.getText().toString().matches(""))
                {
                    Toast.makeText(RegisterStep1.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(checkValidEmail(mEmailInput.getText().toString()) == false)
                {
                    Toast.makeText(RegisterStep1.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mPasswordInput.getText().toString().equals(mVerifyPassword.getText().toString()))
                {
                    Toast.makeText(RegisterStep1.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mAuth.createUserWithEmailAndPassword(mEmailInput.getText().toString(), mPasswordInput.getText().toString()).addOnCompleteListener(RegisterStep1.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(RegisterStep1.this, "Account Created",
                                        Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterStep1.this, RegisterStep2.class);
                                intent.putExtra("username", mEmailInput.getText().toString());
                                intent.putExtra("fullname", mNameInput.getText().toString());
                                intent.putExtra("password", mPasswordInput.getText().toString());

                                mUser = FirebaseAuth.getInstance().getCurrentUser();
                                id = mUser.getUid();//gets unique id


                                User user = new User(id, mEmailInput.getText().toString(), mNameInput.getText().toString(), diet);

                                mDatabase.child("Users").child(id).setValue(user);
                                startActivity(intent);


                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(RegisterStep1.this, "Provided email has been registered already.",
                                        Toast.LENGTH_LONG).show();

                            }

                            // ...
                        }
                    });

                }
            }
        });




    }

    private void initializeElements(){

        mNameInput = (EditText) findViewById(R.id.enterName);
        mVerifyPassword = (EditText) findViewById(R.id.enterPassword);
        mPasswordInput = (EditText) findViewById(R.id.enterPassword2);
        mEmailInput = (EditText) findViewById(R.id.enterEmail);
        mNextButton = (Button) findViewById(R.id.nextButton);

    }

    public static boolean checkValidEmail(String email){
        String symbols = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(symbols, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
