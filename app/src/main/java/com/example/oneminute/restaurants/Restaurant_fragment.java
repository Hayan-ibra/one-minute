package com.example.oneminute.restaurants;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.oneminute.R;
import com.example.oneminute.models.Food;
import com.example.oneminute.models.RestaurantProfile;
import com.example.oneminute.models.TypesModelClass;
import com.example.oneminute.restaurants.viewmodel.RestaurantViewModel;
import com.example.oneminute.restaurants.restaurants_recycler_adapters.FoodAdapter;
import com.example.oneminute.restaurants.restaurants_recycler_adapters.RestaurantsAdapter;
import com.example.oneminute.restaurants.restaurants_recycler_adapters.TypesAdapter;
import com.example.oneminute.shopping.adapters.GridSpacingItemDecoration;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Restaurant_fragment extends Fragment {


    RestaurantViewModel viewModel;
    //firebase authentication

    int search_state=0;


    RestaurantsAdapter  adapter;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private String searchBy="meals";
    private String display="Meals";

    ArrayList<RestaurantProfile> restaurants =new ArrayList<>();
    //firebase firestore

    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private FirebaseFirestore db1 =FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("RestaurantOwners");
    private CollectionReference collectionFoodReference=db1.collection("Foods");

    //firebase storage for image storage
    private StorageReference storageReference;

    ArrayList <Food> foods=new ArrayList<>();
    FoodAdapter foodAdapter;




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Restaurant_fragment() {
        // Required empty public constructor
    }
    static String fragName="";

    public static void PutName(String name){


        fragName=String.valueOf(name);
    }

    private  OnFragmentClicked listenerrr;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentClicked){
            listenerrr =(OnFragmentClicked) context;
        }else {
            throw new ClassCastException("your activity does not implements the listener ");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listenerrr=null;
        //when destroying the fragment reset listener
    }

    public static Restaurant_fragment newInstance(String param1, String param2) {
        Restaurant_fragment fragment = new Restaurant_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant_fragment, container, false);









        RecyclerView rv_types=v.findViewById(R.id.recycler_types_restaurants);
        RecyclerView rv_restaurants=v.findViewById(R.id.recycler_restaurants_restaurants);
        RecyclerView rec_food=v.findViewById(R.id.recycler_foods_restaurants);
        TextView tv_title=v.findViewById(R.id.textView_types_restaurants);
        SwipeRefreshLayout refreshLayout=v.findViewById(R.id.swipe_layout);

        viewModel= new ViewModelProvider(getActivity()).get(RestaurantViewModel.class);

        viewModel.getDataList().observe(getActivity(), new Observer<ArrayList<Food>>() {
            @Override
            public void onChanged(ArrayList<Food> foods) {

                foodAdapter.notifyDataSetChanged();

            }
        });

        viewModel.getRestaurants().observe(getActivity(), new Observer<ArrayList<RestaurantProfile>>() {
            @Override
            public void onChanged(ArrayList<RestaurantProfile> restaurantProfiles) {
                adapter.notifyDataSetChanged();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restaurants.clear();
                getRestaurantsData();
                initRestaurants(rv_restaurants);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Refresh complete
                        refreshLayout.setRefreshing(false); // Stop the animation
                    }
                }, 2000);


            }
        });

        ImageView searchBy=v.findViewById(R.id.search_by_icon);
        searchBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (search_state<=2){
                    search_state=search_state+1;
                }else {
                    search_state=1;
                }
                switch (search_state){
                    case 0:

                        break;
                    case 1:
                        searchBy.setImageResource(R.drawable.baseline_location_on_24);
                        break;
                    case 2:
                        searchBy.setImageResource(R.drawable.baseline_translate_24);
                        break;
                    default:
                }
            }
        });


        restaurants.clear();
        initRestaurants(rv_restaurants);
        initTypes(rv_types,rec_food,tv_title);
        initiateFood(rec_food,tv_title);
        android.widget.SearchView searchView=v.findViewById(R.id.search);
        searchViewDetails(searchView);
        storageReference= FirebaseStorage.getInstance().getReference();



        listenerrr.onFragmentInteraction();


        Toast.makeText(getActivity(), fragName, Toast.LENGTH_SHORT).show();

        return v;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        Toolbar toolbar = getActivity().findViewById(R.id.tollbar);

        toolbar.inflateMenu(R.menu.menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.menu_my_orders){
            Toast.makeText(getActivity(), "yupee", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchViewDetails(android.widget.SearchView searchView){

        searchView.setIconified(true);
        searchView.setSubmitButtonEnabled(true);//search with button
        searchView.setFilterTouchesWhenObscured(true);

        //searchView.



        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchBy=String.valueOf(s);
                SearchRestaurant();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });




    }


    public void  initiateFood(RecyclerView recyclerView,TextView textView){


        collectionFoodReference.whereEqualTo("type",searchBy).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    //getting all snapshots

                    foods.clear();

                    for (QueryDocumentSnapshot snapshot : value){

                        Food food =snapshot.toObject(Food.class);
                        foods.add(food);


                    }


                    foodAdapter=new FoodAdapter(getActivity(), foods, new FoodAdapter.Listenerr() {
                        @Override
                        public void onclicked(int count, Food food1) {
                            Intent intent=new Intent(getActivity(), FoodSingleItem.class);
                            String name=food1.getName();
                            String res=food1.getRestaurantID();

                            intent.putExtra("from",1);

                            intent.putExtra("ser",food1);
                            startActivity(intent);

                        }
                    });




                    recyclerView.setAdapter(foodAdapter);
                    //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    recyclerView.setHasFixedSize(true);


                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    int spacingInPixels =16;
                    //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
                    textView.setText(display);
                    foodAdapter.notifyDataSetChanged();




                }else {
                    foods.clear();
                    recyclerView.setAdapter(foodAdapter);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    recyclerView.setHasFixedSize(true);
                    textView.setText(display);
                    foodAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Emptyyy", Toast.LENGTH_SHORT).show();
                }

            }


        });



    }


    @Override
    public void onStart() {
        super.onStart();
        //
        getRestaurantsData();

    }


    private void getRestaurantsData() {

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    restaurants.clear();
                    for(QueryDocumentSnapshot restaurantss :queryDocumentSnapshots ){
                        RestaurantProfile res =restaurantss.toObject(RestaurantProfile.class);
                        restaurants.add(res);
                        adapter.notifyDataSetChanged();

                    }
                    //recycler



                }
                else {
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();

                }
            }
        });





    }


    public  void initRestaurants(RecyclerView recyclerView){

        adapter=new RestaurantsAdapter(restaurants, getActivity(), new TypesAdapter.Listenerr() {
            @Override
            public void onclicked(int count,String tag,String displayName) {




            }
        });



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        adapter.notifyDataSetChanged();




    }


    void initTypes(RecyclerView rec,RecyclerView rec_food,TextView textView){



        // Inflate the layout for this fragment
        ArrayList<TypesModelClass> models=new ArrayList<>();
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


        TypesAdapter adapter=new TypesAdapter(models, getActivity(), new TypesAdapter.Listenerr() {
            @Override
            public void onclicked(int count,String tag,String displayName) {


                searchBy=String.valueOf(tag);
                display=String.valueOf(displayName);
                initiateFood(rec_food,textView);



            }
        });
        rec.setAdapter(adapter);
        rec.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

    }


    public  void  SearchRestaurant(){
        String searchByWhat="";
        if (search_state==1){
            searchByWhat="city";

        } else if (search_state==2) {
            searchByWhat="name";
        }


        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    restaurants.clear();
                    for(QueryDocumentSnapshot restaurantss :queryDocumentSnapshots ){
                        RestaurantProfile res =restaurantss.toObject(RestaurantProfile.class);

                        if(search_state==1){
                            int result =calculate(res.getCity(),searchBy);
                            if (result <= 3) {
                                // Document is similar enough, display it
                                // You can add it to a list of results or display it directly
                                restaurants.add(res);


                            }


                        } else if (search_state==2) {
                            int result =calculate(res.getName(),searchBy);
                            if (result <= 3) {
                                // Document is similar enough, display it
                                // You can add it to a list of results or display it directly
                                restaurants.add(res);

                            }

                        }else {
                            Toast.makeText(getActivity(), "Please choose filter", Toast.LENGTH_SHORT).show();
                        }





                    }
                    viewModel.setRestaurants(restaurants);
                    adapter.notifyDataSetChanged();
                    //recycler



                }
                else {
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();

                }
            }
        });





    }


    public static int calculate(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;

        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;

        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }

        return dp[s1.length()][s2.length()];
    }


    //listener.onFragmentInteraction(name);


    public interface OnFragmentClicked {
        void onFragmentInteraction();
    }


}