package com.example.kyl3g.sunhacksnov2018.Search;

import android.support.annotation.NonNull;

import com.example.kyl3g.sunhacksnov2018.Callback.BooleanCallback;
import com.example.kyl3g.sunhacksnov2018.Callback.GroceryListCallback;
import com.example.kyl3g.sunhacksnov2018.Callback.RecipeListCallback;
import com.example.kyl3g.sunhacksnov2018.Callback.ResultCallback;
import com.example.kyl3g.sunhacksnov2018.Callback.UserCallback;
import com.example.kyl3g.sunhacksnov2018.Objects.Grocery;
import com.example.kyl3g.sunhacksnov2018.Objects.Recipes;
import com.example.kyl3g.sunhacksnov2018.Objects.Result;
import com.example.kyl3g.sunhacksnov2018.Objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Firebase {
    private static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    private static DatabaseReference groceryRef = FirebaseDatabase.getInstance().getReference().child("Grocery");
    private static DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference().child("Recipes");


    public Firebase(){
    }

    public static void updateUser(User userToBeUpdated) {

        String id = userToBeUpdated.getId();
        userRef.child(id).setValue(userToBeUpdated);
    }

    public static void hasUser(final String userID, @NonNull final BooleanCallback finishedCallback) {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    User user = ds.getValue(User.class);
                    if (user.getId().equals(userID)) {
                        finishedCallback.callback(true);
                    } else {
                        finishedCallback.callback(false);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public static void callCurrentUser(@NonNull final UserCallback finishedCallback){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    final String currentUserID = firebaseUser.getUid();
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {


                                User user = ds.getValue(User.class);

                                if(currentUserID.equals(user.getId())){
                                    finishedCallback.callback(user);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //4  Toast.makeText(Profile.this, "USER ID\n"+currentUser,Toast.LENGTH_SHORT).show();

                } else {
                    //  Toast.makeText(Profile.this, "USER ID\n"+ currentUser,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void autoUpdateCurrentUser(@NonNull final UserCallback finishedCallback){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {
                    final String currentUserID = firebaseUser.getUid();
                    userRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {


                                User user = ds.getValue(User.class);

                                if(currentUserID.equals(user.getId())){
                                    finishedCallback.callback(user);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //4  Toast.makeText(Profile.this, "USER ID\n"+currentUser,Toast.LENGTH_SHORT).show();

                } else {
                    //  Toast.makeText(Profile.this, "USER ID\n"+ currentUser,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void getResult(@NonNull final ResultCallback finishedCallback)
    {
        userRef.child("Result").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Result groceries = dataSnapshot.getValue(Result.class);


                finishedCallback.callback(groceries.getGroceries());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void readUser(final String userID, @NonNull final UserCallback finishedCallback) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    User user = ds.getValue(User.class);
                    if (user.getId().equals(userID)) {
                        finishedCallback.callback(user);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public static void readGrocery( @NonNull final GroceryListCallback finishedCallback) {
        groceryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Grocery> groceries = new ArrayList<Grocery>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Grocery grocery = ds.getValue(Grocery.class);
                    groceries.add(grocery);
                }
                finishedCallback.callback(groceries);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public static void readRecipes(@NonNull final RecipeListCallback finishedCallback) {
        final List<Recipes> recipes = new ArrayList<Recipes>();
        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Recipes recipe = ds.getValue(Recipes.class);
                    recipes.add(recipe);
                }

                finishedCallback.callback(recipes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public static void readHuntedRecipes(@NonNull final RecipeListCallback finishedCallbacak){
        callCurrentUser(new UserCallback() {
            @Override
            public void callback(User callback) {
                finishedCallbacak.callback(callback.getHuntedRecipes());
            }
        });
    }

    public static void addHuntedRecipes(final Recipes recipes){
        callCurrentUser(new UserCallback() {
            @Override
            public void callback(User callback) {
                boolean flag = true;
                for(int i = 0; i < callback.getHuntedRecipes().size(); i++){
                    if(callback.getHuntedRecipes().get(i).getName().equals(recipes.getName())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    callback.addHuntedRecipes(recipes);
                    updateUser(callback);
                }
            }
        });
    }

    public static void removeHuntedRecipies(final Recipes recipe){
        callCurrentUser(new UserCallback() {
            @Override
            public void callback(User callback) {
                callback.removeHuntedRecipes(recipe);
                updateUser(callback);
            }
        });
    }


}
