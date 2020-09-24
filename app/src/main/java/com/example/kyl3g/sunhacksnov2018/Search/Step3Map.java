package com.example.kyl3g.sunhacksnov2018.Search;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.kyl3g.sunhacksnov2018.BottomNavigationViewHelper;
import com.example.kyl3g.sunhacksnov2018.Callback.GroceryListCallback;
import com.example.kyl3g.sunhacksnov2018.Callback.RecipeListCallback;
import com.example.kyl3g.sunhacksnov2018.Objects.Grocery;
import com.example.kyl3g.sunhacksnov2018.Objects.ItemTag;
import com.example.kyl3g.sunhacksnov2018.Objects.Recipes;
import com.example.kyl3g.sunhacksnov2018.Objects.ResultObject;
import com.example.kyl3g.sunhacksnov2018.Objects.IngredientsInfo;
import com.example.kyl3g.sunhacksnov2018.Objects.InventoryInfo;
import com.example.kyl3g.sunhacksnov2018.Objects.SumTag;
import com.example.kyl3g.sunhacksnov2018.Pantry.PantryPage;
import com.example.kyl3g.sunhacksnov2018.Profile.ProfilePage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.example.kyl3g.sunhacksnov2018.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Step3Map extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener{

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 12f;
    private static final int PLACE_PICKER_REQUEST = 1;


    //widgets
    BottomNavigationView navBar;
    private RelativeLayout mSlideLayout;
    BottomSheetBehavior mBottomSheetBehavior;
    private ListView mUpdatedGroceryList;
    private List<ResultObject> newGroceryList;
    private UpdatedGroceryListAdapter ugla;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    private Marker mMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_step3);

        getLocationPermission();

    }

    private void init(){
        Log.d(TAG, "init: initializing");
        initializeNavigationBar();

        mSlideLayout = (RelativeLayout) findViewById(R.id.bottomSlide);
        mBottomSheetBehavior = BottomSheetBehavior.from(mSlideLayout);

        newGroceryList = new ArrayList<ResultObject>();
        mUpdatedGroceryList = (ListView) findViewById(R.id.lvUpdatedGroceryList);
        ugla = new UpdatedGroceryListAdapter(Step3Map.this, R.layout.search_step3_finalgrocerylistitem_adapter, newGroceryList);

        IngredientsInfo ingTest = new IngredientsInfo("onion", 2);
        InventoryInfo invTest = new InventoryInfo("onion", 2.88);
        Grocery grocTest = new Grocery("Fry's", 33.46423, -111.92244, null);
    //    newGroceryList.add(new ResultObject(ingTest, invTest, grocTest));

        Bundle b = getIntent().getExtras();

        if (null != b)
        {       //0 when distance is true;
            final int numOfStores = b.getInt("NumOfStore");
            if(b.getInt("RadioResult") == 0 ){
                Log.d("PASS", "DISTANCE TRUE");
                    Firebase.readGrocery(new GroceryListCallback() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void callback(List<Grocery> callback) {
                            final ArrayList<Grocery> groceries = (ArrayList) callback;

                            for (int i = 0; i < groceries.size(); i++) {
                                double lon1 = -111.933693;
                                double lon2 = groceries.get(i).getLongCoordinate();
                                double lat1 = 33.418562;
                                double lat2 = groceries.get(i).getLatCoordinate();
                                double dlon = Math.abs(lon2 - lon1);
                                double dlat = Math.abs(lat2 - lat1);
                                double a = 12430 * dlat / 180;

                                double b = 24901 * dlon / 360 * Math.cos((lat1 + lat2) / 2);

                                double d = Math.pow((a * a + b * b), .5);
                                groceries.get(i).setDistance(d);

                            }

                            for (int q = 0; q < numOfStores; q++)
                            {
                                String snippet = "Name: " + groceries.get(q).getName() + "\n";


                                MarkerOptions options = new MarkerOptions()
                                        .position(new LatLng(groceries.get(q).getLatCoordinate(), groceries.get(q).getLongCoordinate()))
                                        .title(groceries.get(q).getName())
                                        .snippet(snippet);
                                mMarker = mMap.addMarker(options);

                            }



                            Firebase.readHuntedRecipes(new RecipeListCallback() {
                                @Override
                                public void callback(List<Recipes> callback) {
                                    Log.d("HuntedRecipes", callback.toString());
                                    groceries.sort(new DistanceComparator());

                                    final List<IngredientsInfo> finalIngredientList = new ArrayList<IngredientsInfo>();

                                    finalIngredientList.add(new IngredientsInfo("lettuce", 0));
                                    finalIngredientList.add(new IngredientsInfo("onion", 0));
                                    finalIngredientList.add(new IngredientsInfo("tomato", 0));
                                    finalIngredientList.add(new IngredientsInfo("wheat bread", 0));
                                    finalIngredientList.add(new IngredientsInfo("chicken breast", 0));
                                    finalIngredientList.add(new IngredientsInfo("chicken thighs legs", 0));
                                    finalIngredientList.add(new IngredientsInfo("mushroom", 0));
                                    finalIngredientList.add(new IngredientsInfo("garlic", 0));
                                    finalIngredientList.add(new IngredientsInfo("gruyere cheese", 0));
                                    finalIngredientList.add(new IngredientsInfo("flour tortillas", 0));
                                    finalIngredientList.add(new IngredientsInfo("spaghetti pasta", 0));
                                    finalIngredientList.add(new IngredientsInfo("alfredo sauce", 0));
                                    finalIngredientList.add(new IngredientsInfo("bread crumbs", 0));
                                    finalIngredientList.add(new IngredientsInfo("taco seasoning", 0));
                                    finalIngredientList.add(new IngredientsInfo("cilantro", 0));
                                    finalIngredientList.add(new IngredientsInfo("ground beef", 0));
                                    finalIngredientList.add(new IngredientsInfo("flank steaks", 0));
                                    finalIngredientList.add(new IngredientsInfo("cheddar cheese", 0));
                                    finalIngredientList.add(new IngredientsInfo("celery", 0));
                                    finalIngredientList.add(new IngredientsInfo("lemon", 0));
                                    finalIngredientList.add(new IngredientsInfo("potatoes", 0));
                                    finalIngredientList.add(new IngredientsInfo("cod", 0));
                                    finalIngredientList.add(new IngredientsInfo("salmon", 0));
                                    finalIngredientList.add(new IngredientsInfo("aspargus", 0));
                                    finalIngredientList.add(new IngredientsInfo("teriyaki", 0));
                                    finalIngredientList.add(new IngredientsInfo("shrimp", 0));

                                    List<Recipes> recipes = callback;
                                    Log.d("recipes", recipes.toString());
                                    for (int i = 0; i < callback.size(); i++) {
                                        List<IngredientsInfo> ingredients = recipes.get(i).getIngredientsInfos();
                                        for (int j = 0; j < ingredients.size(); j++) {

                                            IngredientsInfo ingredient = ingredients.get(j);
                                            Log.d("ingredient", ingredient.getAmount()+"");
                                            if (ingredient.getName().equals("lettuce")) {
                                                finalIngredientList.get(0).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("onion")) {
                                                finalIngredientList.get(1).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("tomato")) {
                                                finalIngredientList.get(2).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("wheat bread")) {
                                                finalIngredientList.get(3).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("chicken thighs legs")) {
                                                finalIngredientList.get(4).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("chicken thighs legs")) {
                                                finalIngredientList.get(5).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("mushroom")) {
                                                finalIngredientList.get(6).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("garlic")) {
                                                finalIngredientList.get(7).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("gruyere cheese")) {
                                                finalIngredientList.get(8).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("flour tortillas")) {
                                                finalIngredientList.get(9).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("spaghetti pasta")) {
                                                finalIngredientList.get(10).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("alfredo sauce")) {
                                                finalIngredientList.get(11).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("bread crumbs")) {
                                                finalIngredientList.get(12).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("taco seasoning")) {
                                                finalIngredientList.get(13).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("cilantro")) {
                                                finalIngredientList.get(14).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("ground beef")) {
                                                finalIngredientList.get(15).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("flank steak")) {
                                                finalIngredientList.get(16).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("cheddar cheese")) {
                                                finalIngredientList.get(17).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("celery")) {
                                                finalIngredientList.get(18).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("lemon")) {
                                                finalIngredientList.get(19).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("potatoes")) {
                                                finalIngredientList.get(20).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("cod")) {
                                                finalIngredientList.get(21).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("salmon")) {
                                                finalIngredientList.get(22).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("asparagus")) {
                                                finalIngredientList.get(23).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("teriyaki")) {
                                                finalIngredientList.get(24).addAmount(ingredient.getAmount());
                                            } else if (ingredient.getName().equals("shrimp")) {
                                                finalIngredientList.get(25).addAmount(ingredient.getAmount());
                                            }
                                        }
                                    }
                                    for(int i = 0; i < finalIngredientList.size(); i++)
                                    Log.d("final Ingredients", finalIngredientList.get(i).getAmount()+"");
                                    String distance = "";
                                    for (int x = 0; x < groceries.size(); x++) {
                                        distance += groceries.get(x).getDistance() + "; ";
                                    }

                                    ItemTag[][] tags = new ItemTag[numOfStores][25];
                                    for(int x = 0; x < numOfStores; x++){
                                        for(int z = 0; z < 25; z++){
                                            ItemTag itemTag = new ItemTag(groceries.get(x).getName(), (double)finalIngredientList.get(z).getAmount() * groceries.get(x).getInventory().get(z).getPrice(), finalIngredientList.get(z).getAmount());
                                            tags[x][z] = itemTag;
                                            Log.d("tags", tags[x][z].getAmount() + "\t" + tags[x][z].getPrice() + "\t" + tags[x][z].getStore());
                                        }
                                    }

//                                    for (int x = 0; x < numOfStores; x++) {
//                                        Grocery g = groceries.get(x);
//                                        for (int z = 0; z < 25; z++) {
//                                            tags[x][z].setAmount(finalIngredientList.get(z).getAmount());
//                                            tags[x][z].setPrice((double)finalIngredientList.get(z).getAmount() * g.getInventory().get(z).getPrice());
//                                            tags[x][z].setStore(groceries.get(x).getName());
//                                            Log.d("tags", tags[x][z].getAmount() + "\t" + tags[x][z].getPrice() + "\t" + tags[x][z].getStore());
//                                        }
//                                    }
                                    ResultObject[] resultObjects = new ResultObject[25];
                                    for(int i = 0; i < 25; i++){
                                        ResultObject resultObject = new ResultObject("", 0, "", 0);
                                        resultObjects[i] = resultObject;
                                    }
                                    for (int i = 0; i < 25; i++) {
                                        String storeName = "";
                                        double min = 100000;
                                        int amount = 0;
                                        for (int j = 0; j < numOfStores; j++) {
                                            if (tags[j][i].getPrice() < min && tags[j][i].getPrice() != 0) {
                                                Log.d("minimum set", ""+min);
                                                min = tags[j][i].getPrice();
                                                storeName = tags[j][i].getStore();
                                                amount = tags[j][i].getAmount();

                                            }
                                        }
                                        Log.d("result Object", min + "\t" + amount + "\t" + storeName);
                                        resultObjects[i].setPrice(min);
                                        resultObjects[i].setAmount(amount);
                                        resultObjects[i].setGrocery(storeName);
                                        if (i == 0) {
                                            resultObjects[i].setName("lettuce");
                                        } else if (i == 1) {
                                            resultObjects[i].setName("onion");
                                        } else if (i == 2) {
                                            resultObjects[i].setName("tomato");
                                        } else if (i == 3) {
                                            resultObjects[i].setName("wheat bread");
                                        } else if (i == 4) {
                                            resultObjects[i].setName("chicken breast");
                                        } else if (i == 5) {
                                            resultObjects[i].setName("chicken thighs legs");
                                        } else if (i == 6) {
                                            resultObjects[i].setName("mushroom");
                                        } else if (i == 7) {
                                            resultObjects[i].setName("garlic");
                                        } else if (i == 8) {
                                            resultObjects[i].setName("gruyere cheese");
                                        } else if (i == 9) {
                                            resultObjects[i].setName("flour tortillas");
                                        } else if (i == 10) {
                                            resultObjects[i].setName("spaghetti pasta");
                                        } else if (i == 11) {
                                            resultObjects[i].setName("alfredo sauce");
                                        } else if (i == 12) {
                                            resultObjects[i].setName("bread crumbs");
                                        } else if (i == 13) {
                                            resultObjects[i].setName("taco seasoning");
                                        } else if (i == 14) {
                                            resultObjects[i].setName("cilantro");
                                        } else if (i == 15) {
                                            resultObjects[i].setName("ground beef");
                                        } else if (i == 16) {
                                            resultObjects[i].setName("flank steaks");
                                        } else if (i == 17) {
                                            resultObjects[i].setName("cheddar cheese");
                                        } else if (i == 18) {
                                            resultObjects[i].setName("celery");
                                        } else if (i == 19) {
                                            resultObjects[i].setName("lemon");
                                        } else if (i == 20) {
                                            resultObjects[i].setName("potatoes");
                                        } else if (i == 21) {
                                            resultObjects[i].setName("cod");
                                        } else if (i == 22) {
                                            resultObjects[i].setName("salmon");
                                        } else if (i == 23) {
                                            resultObjects[i].setName("asparagus");
                                        } else if (i == 24) {
                                            resultObjects[i].setName("teriyaki");
                                        } else if (i == 25) {
                                            resultObjects[i].setName("shrimp");
                                        } else {
                                        }


                                    }
                                    for (int i = 0; i < 25; i++) {
                                        if(resultObjects[i].getAmount() != 0 ) {
                                            newGroceryList.add(resultObjects[i]);
                                            ugla.notifyDataSetChanged();
                                            mUpdatedGroceryList.setAdapter(ugla);
                                        }
                                    }
                                }

                            });

                        }
                    });


            }
            //1 when price is true;
            else if(b.getInt("RadioResult") == 1){
                Log.d("PASS", "PRICE TRUE");




                Firebase.readHuntedRecipes(new RecipeListCallback() {
                    @Override
                    public void callback(List<Recipes> callback) {
                        Log.d("HuntedRecipes", callback.toString());
                        final List<IngredientsInfo> finalIngredientList = new ArrayList<IngredientsInfo>();

                        finalIngredientList.add(new IngredientsInfo("lettuce", 0));
                        finalIngredientList.add(new IngredientsInfo("onion", 0));
                        finalIngredientList.add(new IngredientsInfo("tomato", 0));
                        finalIngredientList.add(new IngredientsInfo("wheat bread", 0));
                        finalIngredientList.add(new IngredientsInfo("chicken breast", 0));
                        finalIngredientList.add(new IngredientsInfo("chicken thighs legs", 0));
                        finalIngredientList.add(new IngredientsInfo("mushroom", 0));
                        finalIngredientList.add(new IngredientsInfo("garlic", 0));
                        finalIngredientList.add(new IngredientsInfo("gruyere cheese", 0));
                        finalIngredientList.add(new IngredientsInfo("flour tortillas", 0));
                        finalIngredientList.add(new IngredientsInfo("spaghetti pasta", 0));
                        finalIngredientList.add(new IngredientsInfo("alfredo sauce", 0));
                        finalIngredientList.add(new IngredientsInfo("bread crumbs", 0));
                        finalIngredientList.add(new IngredientsInfo("taco seasoning", 0));
                        finalIngredientList.add(new IngredientsInfo("cilantro", 0));
                        finalIngredientList.add(new IngredientsInfo("ground beef", 0));
                        finalIngredientList.add(new IngredientsInfo("flank steaks", 0));
                        finalIngredientList.add(new IngredientsInfo("cheddar cheese", 0));
                        finalIngredientList.add(new IngredientsInfo("celery", 0));
                        finalIngredientList.add(new IngredientsInfo("lemon", 0));
                        finalIngredientList.add(new IngredientsInfo("potatoes", 0));
                        finalIngredientList.add(new IngredientsInfo("cod", 0));
                        finalIngredientList.add(new IngredientsInfo("salmon", 0));
                        finalIngredientList.add(new IngredientsInfo("aspargus", 0));
                        finalIngredientList.add(new IngredientsInfo("teriyaki", 0));
                        finalIngredientList.add(new IngredientsInfo("shrimp", 0));

                        List<Recipes> recipes = callback;
                        Log.d("recipes", recipes.toString());
                        for (int i = 0; i < callback.size(); i++) {
                            List<IngredientsInfo> ingredients = recipes.get(i).getIngredientsInfos();
                            for (int j = 0; j < ingredients.size(); j++) {

                                IngredientsInfo ingredient = ingredients.get(j);
                                Log.d("ingredient", ingredient.getAmount()+"");
                                if (ingredient.getName().equals("lettuce")) {
                                    finalIngredientList.get(0).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("onion")) {
                                    finalIngredientList.get(1).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("tomato")) {
                                    finalIngredientList.get(2).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("wheat bread")) {
                                    finalIngredientList.get(3).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("chicken thighs legs")) {
                                    finalIngredientList.get(4).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("chicken thighs legs")) {
                                    finalIngredientList.get(5).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("mushroom")) {
                                    finalIngredientList.get(6).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("garlic")) {
                                    finalIngredientList.get(7).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("gruyere cheese")) {
                                    finalIngredientList.get(8).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("flour tortillas")) {
                                    finalIngredientList.get(9).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("spaghetti pasta")) {
                                    finalIngredientList.get(10).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("alfredo sauce")) {
                                    finalIngredientList.get(11).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("bread crumbs")) {
                                    finalIngredientList.get(12).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("taco seasoning")) {
                                    finalIngredientList.get(13).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("cilantro")) {
                                    finalIngredientList.get(14).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("ground beef")) {
                                    finalIngredientList.get(15).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("flank steak")) {
                                    finalIngredientList.get(16).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("cheddar cheese")) {
                                    finalIngredientList.get(17).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("celery")) {
                                    finalIngredientList.get(18).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("lemon")) {
                                    finalIngredientList.get(19).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("potatoes")) {
                                    finalIngredientList.get(20).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("cod")) {
                                    finalIngredientList.get(21).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("salmon")) {
                                    finalIngredientList.get(22).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("asparagus")) {
                                    finalIngredientList.get(23).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("teriyaki")) {
                                    finalIngredientList.get(24).addAmount(ingredient.getAmount());
                                } else if (ingredient.getName().equals("shrimp")) {
                                    finalIngredientList.get(25).addAmount(ingredient.getAmount());
                                }

                            }
                        }
                        Firebase.readGrocery(new GroceryListCallback() {

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void callback(List<Grocery> callback) {
                                //populating price chart

                                ItemTag[][] tags1 = new ItemTag[5][25];
                                for(int i = 0; i < 5 ; i++){
                                    for(int j = 0; j < 25; j++){
                                        ItemTag itemTag = new ItemTag("", 0, 0);
                                        tags1[i][j] = itemTag;
                                    }
                                }
                                for(int i = 0 ; i < 25 ; i ++){
                                    tags1[0][i].setStore("Frys");
                                    tags1[0][i].setAmount(finalIngredientList.get(i).getAmount());
                                    tags1[0][i].setPrice((double)finalIngredientList.get(i).getAmount() * callback.get(0).getInventory().get(i).getPrice());
                                }
                                for(int i = 0 ; i < 25 ; i ++){
                                    tags1[1][i].setStore("Walmart");
                                    tags1[1][i].setAmount(finalIngredientList.get(i).getAmount());
                                    tags1[1][i].setPrice((double)finalIngredientList.get(i).getAmount() * callback.get(1).getInventory().get(i).getPrice());
                                }
                                for(int i = 0 ; i < 25 ; i ++){
                                    tags1[2][i].setStore("Food City");
                                    tags1[2][i].setAmount(finalIngredientList.get(i).getAmount());
                                    tags1[2][i].setPrice((double)finalIngredientList.get(i).getAmount() * callback.get(2).getInventory().get(i).getPrice());
                                }for(int i = 0 ; i < 25 ; i ++){
                                    tags1[3][i].setStore("Safeway");
                                    tags1[3][i].setAmount(finalIngredientList.get(i).getAmount());
                                    tags1[3][i].setPrice((double)finalIngredientList.get(i).getAmount() * callback.get(3).getInventory().get(i).getPrice());
                                }
                                for(int i = 0 ; i < 25 ; i ++){
                                    tags1[4][i].setStore("Food City");
                                    tags1[4][i].setAmount(finalIngredientList.get(i).getAmount());
                                    tags1[4][i].setPrice((double)finalIngredientList.get(i).getAmount() * callback.get(4).getInventory().get(i).getPrice());
                                }
                                double[][] deviations = new double[5][25];
                                double[] mins = new double[25];
                                //populating mins and delta prices
                                for(int i = 0; i < 25; i++){
                                    int count = 0;
                                    for(int k = 0; k < 5; k++) {
                                        if(tags1[k][i].getPrice() == 0){
                                            count++;
                                        }
                                    }
                                    if(count == 5){
                                        mins[i] = 0;
                                    }else{
                                        mins[i] = 100000;
                                    }
                                    for(int j = 0; j < 5; j++){
                                        if(tags1[j][i].getPrice() < mins[i] && tags1[j][i].getAmount() != 0){
                                            mins[i] = tags1[j][i].getPrice();
                                        }
                                    }
                                    for(int j = 0; j < 5; j++){
                                        deviations[j][i] = tags1[j][i].getPrice() - mins[i];
                                    }
                                }
                                List<SumTag> sums = new ArrayList<SumTag>();

                                for(int i = 0; i < 5; i++){
                                    SumTag sumTag = new SumTag();
                                    sums.add(sumTag);
                                    for(int j = 0; j < 25; j++){
                                        sums.get(i).addSum(deviations[i][j]);
                                        sums.get(i).setStore(tags1[i][j].getStore());
                                    }
                                }
                                for(int i = 0; i < sums.size(); i++){
                                    Log.d("Sums before sort", ""+sums.get(i).getSum());
                                }
                                sums.sort(new DeltaSumComparator());

                                for(int i = 0; i < sums.size(); i++){
                                    Log.d("Sums after sort", ""+sums.get(i).getSum());
                                }

                                for(int i = 0; i < callback.size(); i++){
                                    Log.d("Grocery from Callback",callback.get(i).getName());
                                }

                                List<Grocery> groceries = new ArrayList<Grocery>();
                                for(int i = 0 ; i < sums.size(); i++){
                                    innerloop:
                                    for(int j = 0; j < callback.size(); j++){
                                        if(sums.get(i).getStore().equals(callback.get(j).getName())) {
                                            groceries.add(callback.get(j));
                                            break innerloop;
                                        }
                                    }
                                }
                                for(int i = 0; i < groceries.size(); i++){
                                    Log.d("Sorted Groceries",groceries.get(i).getName());
                                }

                                ItemTag[][] tags = new ItemTag[numOfStores][25];
                                for(int x = 0; x < numOfStores; x++){
                                    for(int z = 0; z < 25; z++){
                                        ItemTag itemTag = new ItemTag(groceries.get(x).getName(), (double)finalIngredientList.get(z).getAmount() * groceries.get(x).getInventory().get(z).getPrice(), finalIngredientList.get(z).getAmount());
                                        tags[x][z] = itemTag;
                                        Log.d("tags", tags[x][z].getAmount() + "\t" + tags[x][z].getPrice() + "\t" + tags[x][z].getStore());
                                    }
                                }


                                ResultObject[] resultObjects = new ResultObject[25];
                                for(int i = 0; i < 25; i++){
                                    ResultObject resultObject = new ResultObject("", 0, "", 0);
                                    resultObjects[i] = resultObject;
                                }
                                for (int i = 0; i < 25; i++) {
                                    String storeName = "";
                                    double min = 100000;
                                    int amount = 0;
                                    for (int j = 0; j < numOfStores; j++) {
                                        if (tags[j][i].getPrice() < min && tags[j][i].getPrice() != 0) {
                                            Log.d("minimum set", ""+min);
                                            min = tags[j][i].getPrice();
                                            storeName = tags[j][i].getStore();
                                            amount = tags[j][i].getAmount();

                                        }
                                    }
                                    Log.d("result Object", min + "\t" + amount + "\t" + storeName);
                                    resultObjects[i].setPrice(min);
                                    resultObjects[i].setAmount(amount);
                                    resultObjects[i].setGrocery(storeName);
                                    if (i == 0) {
                                        resultObjects[i].setName("lettuce");
                                    } else if (i == 1) {
                                        resultObjects[i].setName("onion");
                                    } else if (i == 2) {
                                        resultObjects[i].setName("tomato");
                                    } else if (i == 3) {
                                        resultObjects[i].setName("wheat bread");
                                    } else if (i == 4) {
                                        resultObjects[i].setName("chicken breast");
                                    } else if (i == 5) {
                                        resultObjects[i].setName("chicken thighs legs");
                                    } else if (i == 6) {
                                        resultObjects[i].setName("mushroom");
                                    } else if (i == 7) {
                                        resultObjects[i].setName("garlic");
                                    } else if (i == 8) {
                                        resultObjects[i].setName("gruyere cheese");
                                    } else if (i == 9) {
                                        resultObjects[i].setName("flour tortillas");
                                    } else if (i == 10) {
                                        resultObjects[i].setName("spaghetti pasta");
                                    } else if (i == 11) {
                                        resultObjects[i].setName("alfredo sauce");
                                    } else if (i == 12) {
                                        resultObjects[i].setName("bread crumbs");
                                    } else if (i == 13) {
                                        resultObjects[i].setName("taco seasoning");
                                    } else if (i == 14) {
                                        resultObjects[i].setName("cilantro");
                                    } else if (i == 15) {
                                        resultObjects[i].setName("ground beef");
                                    } else if (i == 16) {
                                        resultObjects[i].setName("flank steaks");
                                    } else if (i == 17) {
                                        resultObjects[i].setName("cheddar cheese");
                                    } else if (i == 18) {
                                        resultObjects[i].setName("celery");
                                    } else if (i == 19) {
                                        resultObjects[i].setName("lemon");
                                    } else if (i == 20) {
                                        resultObjects[i].setName("potatoes");
                                    } else if (i == 21) {
                                        resultObjects[i].setName("cod");
                                    } else if (i == 22) {
                                        resultObjects[i].setName("salmon");
                                    } else if (i == 23) {
                                        resultObjects[i].setName("asparagus");
                                    } else if (i == 24) {
                                        resultObjects[i].setName("teriyaki");
                                    } else if (i == 25) {
                                        resultObjects[i].setName("shrimp");
                                    } else {
                                    }


                                }
                                for (int i = 0; i < 25; i++) {
                                    if(resultObjects[i].getAmount() != 0 ) {
                                        newGroceryList.add(resultObjects[i]);
                                        ugla.notifyDataSetChanged();
                                        mUpdatedGroceryList.setAdapter(ugla);
                                    }
                                }

                                for (int q = 0; q < numOfStores; q++)
                                {
                                    String snippet = "Name: " + groceries.get(q).getName() + "\n";


                                    MarkerOptions options = new MarkerOptions()
                                            .position(new LatLng(groceries.get(q).getLatCoordinate(), groceries.get(q).getLongCoordinate()))
                                            .title(groceries.get(q).getName())
                                            .snippet(snippet);
                                    mMarker = mMap.addMarker(options);

                                }





                            }
                        });
                    }
                });



            }

        }



        mUpdatedGroceryList.setAdapter(ugla);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        hideSoftKeyboard();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            }
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(Step3Map.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(Step3Map.this));

        if(placeInfo != null){
            try{
                String snippet = "Address: " + placeInfo.getAddress() + "\n" +
                        "Phone Number: " + placeInfo.getPhoneNumber() + "\n" +
                        "Website: " + placeInfo.getWebsiteUri() + "\n" +
                        "Price Rating: " + placeInfo.getRating() + "\n";

                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(placeInfo.getName())
                        .snippet(snippet);
                mMarker = mMap.addMarker(options);

            }catch (NullPointerException e){
                Log.e(TAG, "moveCamera: NullPointerException: " + e.getMessage() );
            }
        }else{
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyboard();
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(Step3Map.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*
        --------------------------- google places API autocomplete suggestions -----------------
     */



    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
//                mPlace.setAttributions(place.getAttributions().toString());
//                Log.d(TAG, "onResult: attributions: " + place.getAttributions());
                mPlace.setId(place.getId());
                Log.d(TAG, "onResult: id:" + place.getId());
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());

                Log.d(TAG, "onResult: place: " + mPlace.toString());
            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlace);

            places.release();
        }
    };

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
                        startActivity(new Intent(Step3Map.this, PantryPage.class));
                        item.setIcon(R.drawable.ic_pantry);
                        break;
                    case R.id.navigation_search:
                        startActivity(new Intent(Step3Map.this, Step1RecipeList.class));
                        item.setIcon(R.drawable.ic_search);
                        break;
                    case R.id.navigation_profile:
                        startActivity(new Intent(Step3Map.this, ProfilePage.class));
                        item.setIcon(R.drawable.ic_profile);
                        break;

                }
                return false;
            }
        });
    }
}





