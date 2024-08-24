package com.example.oneminute.restaurants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.models.Food;
import com.example.oneminute.models.RestaurantProfile;
import com.example.oneminute.models.TypesModelClass;
import com.example.oneminute.restaurants.viewmodel.FoodViewModel;
import com.example.oneminute.restaurants.viewmodel.RestaurantViewModel;
import com.example.oneminute.restaurants.restaurants_recycler_adapters.FoodAdapter;
import com.example.oneminute.restaurants.restaurants_recycler_adapters.TypesAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RestaurantSingleProfile extends AppCompatActivity {

    MaterialToolbar toolbar;

    FloatingActionButton fab;


    //firebase firestore

    private FirebaseFirestore db =FirebaseFirestore.getInstance();

    private CollectionReference collectionFoodReference=db.collection("Foods");

    //firebase storage for image storage

    int meu_item_state =0 ;

    //com.google.android.material.appbar.CollapsingToolbarLayout
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;

    TextView tv_phone,tv_name,tv_location,tv_rec_title,tv_from,tv_to;
    ImageView image_profile;

    CardView cardView;

    RecyclerView recyclerView_food,recyclerView_types;

    String res_id;


    TypesAdapter adapter;
    FoodAdapter foodAdapter;
    ArrayList<TypesModelClass> models=new ArrayList<>();;
    ArrayList<TypesModelClass> filtered_models=new ArrayList<>();
    ArrayList<Food> foods=new ArrayList<>();
    ArrayList<Food> specificFood=new ArrayList<>();
    int count_a;


    String search_for="";
    RestaurantViewModel viewModel;

    FoodViewModel foodViewModel;



    RestaurantProfile restaurantProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_single_profile);

        toolbar=findViewById(R.id.toolbar_restaurants_single);
        image_profile=findViewById(R.id.image_restaurant_profile_restaurant_single);
        recyclerView_food=findViewById(R.id.recycler_food_restaurant_single);
        recyclerView_types=findViewById(R.id.recycler_types_restaurants_single);
        tv_rec_title=findViewById(R.id.textView_recycler_title_restaurant_single);
        collapsingToolbarLayout=findViewById(R.id.collapsing_restaurants_single);
        appBarLayout=findViewById(R.id.appbar_restaurant);
        tv_from=findViewById(R.id.text_time_from_single_food);
        tv_to=findViewById(R.id.text_time_to_single_food);
        tv_location=findViewById(R.id.text_location_single_food);
        tv_phone=findViewById(R.id.text_phone_number_single_food);

        Intent intent=getIntent();
        restaurantProfile= (RestaurantProfile) intent.getSerializableExtra("res");


        String name=restaurantProfile.getName();
        res_id=restaurantProfile.getParent_user_id();
        String city =restaurantProfile.getCity();
        String url=restaurantProfile.getUrl();
        String location=restaurantProfile.getLocation();
        String phoneNumber=restaurantProfile.getPhoneNum();
        double rate=restaurantProfile.getRate();
        String description=restaurantProfile.getDescription();
        String time_from=restaurantProfile.getWorkTime_from();
        String time_to=restaurantProfile.getWorkTime_to();
        collapsingToolbarLayout.setTitle(name);
        Glide.with(this).load(url).fitCenter().into(image_profile);
        tv_from.setText(time_from+"  ");
        tv_to.setText(time_to);
        tv_location.setText(location);
        tv_phone.setText(phoneNumber);
        //initiate types recycler

        initTypes();
        initiate();

        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));

                // Check if there's an app to handle the intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Start the activity
                    startActivity(intent);
                } else {
                    // Handle error: No app can handle the intent
                    Toast.makeText(RestaurantSingleProfile.this, "No app can handle this action", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initiate();
        getFood();


        viewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        foodViewModel=new ViewModelProvider(this).get(FoodViewModel.class);
        foodViewModel.getFoodList().observe(this, new Observer<ArrayList<Food>>() {
            @Override
            public void onChanged(ArrayList<Food> foods) {
                foodAdapter.notifyDataSetChanged();
            }
        });


        viewModel.getDataList().observe(this, new Observer<ArrayList<Food>>() {
            @Override
            public void onChanged(ArrayList<Food> foods) {
                foodAdapter.notifyDataSetChanged();

            }
        });





        setSupportActionBar(toolbar);


//getFood();




    }


    public void initiate(){


        //initiate food recycler
        foodAdapter=new FoodAdapter(this, specificFood, new FoodAdapter.Listenerr() {
            @Override
            public void onclicked(int count, Food food1) {

                Intent intent=new Intent(RestaurantSingleProfile.this, FoodSingleItem.class);



                String name1=food1.getName();
                String res1=food1.getRestaurantID();

                intent.putExtra("from",0);

                intent.putExtra("ser",food1);
                startActivity(intent);

            }
        });
        recyclerView_food.setAdapter(foodAdapter);
        recyclerView_food.setLayoutManager(new GridLayoutManager(this,2));




        //types adapter will handle the click and add items from food to specific foood
        TypesAdapter adapter=new TypesAdapter(filtered_models, this, new TypesAdapter.Listenerr() {
            @Override
            public void onclicked(int count,String tag,String displayName) {
                search_for=String.valueOf(tag);
                specificFood.clear();
                if(!foods.isEmpty()) {
                    for (int i = 0; i < foods.size(); i++) {
                        if (foods.get(i).getType().equals(search_for)) {
                            Food food = foods.get(i);
                            specificFood.add(food);
                        }
                    }
                    //foodAdapter.notifyDataSetChanged();
                    foodViewModel.setFoodList(specificFood);

                }
            }
        });
        recyclerView_types.setAdapter(adapter);
        recyclerView_types.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();

            }
        },1000);




    }
    void initTypes(){
        models.add(new TypesModelClass(R.drawable.meal,"Meals","meals"));
        models.add(new TypesModelClass(R.drawable.food,"Fast food" , "fast_food" ));
        models.add(new TypesModelClass(R.drawable.hamburger,"Burger", "burger" ));
        models.add(new TypesModelClass(R.drawable.ice_cream,"Dessert", "dessert" ));
        models.add(new TypesModelClass(R.drawable.fish_end,"Fish", "sea_food" ));
        models.add(new TypesModelClass(R.drawable.food_steak,"Western", "western_foods" ));
        models.add(new TypesModelClass(R.drawable.salad,"Salads", "salad" ));
        models.add(new TypesModelClass(R.drawable.sandwitch,"Sandwich",  "sandwich"));
        models.add(new TypesModelClass(R.drawable.soup,"Soup",  "soup"));
        models.add(new TypesModelClass(R.drawable.pizza,"Pizza","pizza"  ));
        models.add(new TypesModelClass(R.drawable.pasta,"Pasta", "pasta"  ));
        models.add(new TypesModelClass(R.drawable.beer,"Beverage",  "beverage" ));
        models.add(new TypesModelClass(R.drawable.glass_cocktail,"Cocktails", "cocktails"  ));
        models.add(new TypesModelClass(R.drawable.baseline_restaurant_menu_24,"Others",  "others" ));


    }


    public void getFood(){
        foods.clear();
        collectionFoodReference.whereEqualTo("restaurantID",res_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){

                    for (QueryDocumentSnapshot snapshot : value){

                        Food food =snapshot.toObject(Food.class);
                        foods.add(food);


                    }
                    viewModel.setDataList(foods);
                    checkFoodAvailability();

                }else {
                    Toast.makeText(RestaurantSingleProfile.this, "EEEMMMPPPTTTYY", Toast.LENGTH_SHORT).show();
                }

            }

        });

    }









    @Override
    protected void onStart() {
        super.onStart();
        Intent intent2=getIntent();
        res_id=intent2.getStringExtra("2");
       // getFood();

    }




    public void checkFoodAvailability() {
        filtered_models.clear();
        int available[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < foods.size(); i++) {

            if (foods.get(i).getType().equals("meals")) {
                available[0] = 1;
            } else if (foods.get(i).getType().equals("fast_food")) {
                available[1] = 1;
            } else if (foods.get(i).getType().equals("burger")) {
                available[2] = 1;
            } else if (foods.get(i).getType().equals("dessert")) {
                available[3] = 1;
            } else if (foods.get(i).getType().equals("sea_food")) {
                available[4] = 1;
            } else if (foods.get(i).getType().equals("western_foods")) {
                available[5] = 1;
            } else if (foods.get(i).getType().equals("salad")) {
                available[6] = 1;
            } else if (foods.get(i).getType().equals("sandwich")) {
                available[7] = 1;
            } else if (foods.get(i).getType().equals("soup")) {
                available[8] = 1;
            } else if (foods.get(i).getType().equals("pizza")) {
                available[9] = 1;
            } else if (foods.get(i).getType().equals("pasta")) {
                available[10] = 1;
            } else if (foods.get(i).getType().equals("beverage")) {
                available[11] = 1;
            } else if (foods.get(i).getType().equals("cocktails")) {
                available[12] = 1;
            } else if (foods.get(i).getType().equals("others")) {
                available[13] = 1;

            }
            //  Toast.makeText(this, available[i], Toast.LENGTH_SHORT).show();


        }
        String c = "";
        for (int i = 0; i < available.length; i++) {
            c = c + available[i];
        }
        Toast.makeText(this, c, Toast.LENGTH_SHORT).show();

        for (int i = 0; i < models.size(); i++) {
            if (available[i] == 1) {
                filtered_models.add(models.get(i));

            }
        }

    }


}