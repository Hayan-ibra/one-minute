package com.example.oneminute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oneminute.History.PointsHistory;
import com.example.oneminute.addPoints.AddPointsActivity;
import com.example.oneminute.hotels.FragmentReservation;
import com.example.oneminute.hotels.Hotels_fragment;
import com.example.oneminute.models.Users;
import com.example.oneminute.restaurants.Restaurant_fragment;
import com.example.oneminute.restaurants.order.OrderFragment;
import com.example.oneminute.restaurants.viewmodel.PointsViewModel;
import com.example.oneminute.shopping.Shopping_fragment;
import com.example.oneminute.tourism.Tourism_fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,Restaurant_fragment.OnFragmentClicked   {
    //extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener

    private DrawerLayout drawerLayout ;
    private  ActionBarDrawerToggle toggle;
    FloatingActionButton fab;
    Toolbar toolbar;

    PointsViewModel viewModel;


    //
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference =db.collection("Users");
    private FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firebase connection
    String fragName="";








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);










        viewModel = new ViewModelProvider(HomeActivity.this).get(PointsViewModel.class);

        toolbar=findViewById(R.id.tollbar);
         setSupportActionBar(toolbar);



        drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView navigationView =findViewById(R.id.nav_View);
        navigationView.setNavigationItemSelectedListener(this);

        View headrerview=navigationView.getHeaderView(0);
        ImageView profile=headrerview.findViewById(R.id.nav_img_profile);
        TextView text_name=headrerview.findViewById(R.id.nav_teext_welcome);
        TextView text_uid=headrerview.findViewById(R.id.nav_teext_uid);






        Users users=Users.getInstance();
        String url =users.getUrl();

        users.getUid();
        users.getAccountType();
        viewModel.setPoints(users.getPoints());
        viewModel.getPoints().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                Toast.makeText(HomeActivity.this, ""+users.getPoints(), Toast.LENGTH_SHORT).show();
            }
        });

        text_name.setText("Welcome "+ users.getUsername());
        text_uid.setText("Points "+ users.getPoints());

        if(url != null){
            //using glide
            Glide.with(this).load(url).fitCenter().into(profile);

        }else {
            profile.setImageResource(R.drawable.download);
        }
        Drawable custom_hamburger = ContextCompat.getDrawable(this, R.drawable.baseline_dehaze_24);



        toggle=new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open_nav,R.string.close_nav);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(custom_hamburger);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        if(savedInstanceState == null){

            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            int state = sharedPreferences.getInt("state", 1);

            if (state==1){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Restaurant_fragment()).commit();
                navigationView.setCheckedItem(R.id.nav_restaurants);
                toolbar.setTitle("Restaurants");

            } else if (state==2) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Shopping_fragment()).commit();
                navigationView.setCheckedItem(R.id.nav_stores);
                toolbar.setTitle("Shopping");

            } else if (state==3) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Hotels_fragment()).commit();
                navigationView.setCheckedItem(R.id.nav_hotels);
                toolbar.setTitle("Hotels");


            } else if (state==4) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Tourism_fragment()).commit();
                navigationView.setCheckedItem(R.id.nav_tourism);
                toolbar.setTitle("Tourism");

            }


        }




    }

    @Override
    public void onFragmentInteraction() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        fragName=currentFragment.getClass().getSimpleName().toString();

        //Restaurant_fragment.PutName();





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.menu_my_orders){
            Toast.makeText(this, "yupee", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.nav_restaurants){
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("state", 1);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Restaurant_fragment()).commit();
            toolbar.setTitle("Restaurants");
            fragName="Restaurant_fragment";





        } else if (item.getItemId()==R.id.nav_hotels) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("state", 3);
            editor.apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Hotels_fragment()).commit();
            toolbar.setTitle("Hotels");
            fragName="Hotels_fragment";


        }
        else if (item.getItemId()==R.id.nav_tourism) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("state", 4);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Tourism_fragment()).commit();
            toolbar.setTitle("Tourism");
            fragName="Tourism_fragment";

        } else if (item.getItemId()==R.id.nav_stores) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("state", 2);
            editor.apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Shopping_fragment()).commit();
            toolbar.setTitle("Shopping");


        }
        else if (item.getItemId()==R.id.history) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PointsHistory()).commit();
            toolbar.setTitle("History");


        }
        else if (item.getItemId()==R.id.nav_orders) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new OrderFragment()).commit();
            toolbar.setTitle("Orders");


        }else if (item.getItemId()==R.id.nav_reservations) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentReservation()).commit();
            toolbar.setTitle("Reservations");


        } else if (item.getItemId()==R.id.nav_add_points) {

            startActivity(new Intent(HomeActivity.this, AddPointsActivity.class));


        } else if (item.getItemId()==R.id.nav_add_sign_out) {
            firebaseAuth.signOut();
            Users users=Users.getInstance();
            users.setEmail(null);
            users.setUid(null);
            users.setUsername(null);
            users.setPoints(0.0);
            users.setAccountType(null);
            users.setPhoneNum(null);
            users.setGender(null);
            users.setUrl(null);
            startActivity(new Intent(HomeActivity.this,MainActivity.class));

        } else  {



        }
        drawerLayout.closeDrawer(GravityCompat.START);


        return true;

    }



    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();

        }
    }


}