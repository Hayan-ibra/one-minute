package com.example.oneminute.tourism;


import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.oneminute.R;
import com.example.oneminute.models.TouristDestinations;
import com.example.oneminute.tourism.adapters.DestinationsRecyclerAdapter;
import com.example.oneminute.tourism.viewmodel.DestinationsViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Tourism_fragment extends Fragment {
    Toolbar toolbar;
    RecyclerView recyclerView;

    DestinationsViewModel viewModel;
    android.widget.SearchView searchView;
    ImageView searchBy;

    DestinationsRecyclerAdapter adapter;

    int search_state=0;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Tourist");




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private String mParam1;
    private String mParam2;

    public Tourism_fragment() {
        // Required empty public constructor
    }


    public static Tourism_fragment newInstance(String param1, String param2) {
        Tourism_fragment fragment = new Tourism_fragment();
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
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_tourism_fragment, container, false);

        recyclerView=v.findViewById(R.id.TouristMainActivity_recycler);
        searchView=v.findViewById(R.id.tourist_search);
        searchBy=v.findViewById(R.id.tourist_search_by_icon);
        adapter=new DestinationsRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        getData();
        searchViewDetails(searchView);
        searchByIconChanger(searchBy,searchView);


        viewModel=new ViewModelProvider(getActivity()).get(DestinationsViewModel.class);


        viewModel.getDestinations().observe(getActivity(), new Observer<ArrayList<TouristDestinations>>() {
            @Override
            public void onChanged(ArrayList<TouristDestinations> touristDestinations) {
                adapter.setDestinations(touristDestinations);
            }
        });


        return v;
    }


    private void searchByIconChanger(ImageView searchBy,android.widget.SearchView searchView) {
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
                        searchView.setQueryHint( "Search by city");
                        break;
                    case 2:
                        searchBy.setImageResource(R.drawable.baseline_translate_24);
                        searchView.setQueryHint("Search by name");
                        break;
                    default:
                }
            }
        });
    }

    private void getData() {
        ArrayList<TouristDestinations> destinations=new ArrayList<>();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                        TouristDestinations destinationsSnap=snapshot.toObject(TouristDestinations.class);
                        destinations.add(destinationsSnap);

                    }
                    viewModel.setDestinations(destinations);



                }else {
                    Toast.makeText(getActivity(), "Nothing Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void searchViewDetails(android.widget.SearchView searchView){

        searchView.setIconified(true);
        searchView.setSubmitButtonEnabled(true);//search with button
        searchView.setFilterTouchesWhenObscured(true);

        //searchView.

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                SearchHotels(s);

                return false;
            }



            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    private void SearchHotels(String s) {

        ArrayList<TouristDestinations> destinations=new ArrayList<>();

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){

                    for(QueryDocumentSnapshot snapshot :queryDocumentSnapshots ){
                        TouristDestinations destinationsSnap =snapshot.toObject(TouristDestinations.class);

                        if(search_state==1){
                            int result =calculate(destinationsSnap.getCity(),s);
                            if (result <= 3) {
                                // Document is similar enough, display it
                                // You can add it to a list of results or display it directly
                                destinations.add(destinationsSnap);

                            }


                        } else if (search_state==2) {
                            int result =calculate(destinationsSnap.getName(),s);
                            if (result <= 3) {
                                // Document is similar enough, display it
                                // You can add it to a list of results or display it directly
                                destinations.add(destinationsSnap);

                            }

                        }else {
                            Toast.makeText(getActivity(), "Please choose filter", Toast.LENGTH_SHORT).show();
                        }





                    }
                    viewModel.setDestinations(destinations);

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
}