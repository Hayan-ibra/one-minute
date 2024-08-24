package com.example.oneminute.hotels;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.oneminute.R;
import com.example.oneminute.hotels.recyclers.HotelsRecyclerAdapter;
import com.example.oneminute.hotels.viewmodel.HotelsViewModel;
import com.example.oneminute.models.Hotels;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Hotels_fragment extends Fragment {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceHotel=db.collection("HotelOwnersN");

    HotelsViewModel viewModel;


    int search_state=0;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Hotels_fragment() {
        // Required empty public constructor
    }



    public static Hotels_fragment newInstance(String param1, String param2) {
        Hotels_fragment fragment = new Hotels_fragment();
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

        View v =inflater.inflate(R.layout.fragment_hotels_fragment, container, false);
        RecyclerView recyclerView=v.findViewById(R.id.Hotels_fragment_recycler_view);
        viewModel=new ViewModelProvider(getActivity()).get(HotelsViewModel.class);
        android.widget.SearchView searchView=v.findViewById(R.id.Hotels_fragment_search);
        ImageView searchBy=v.findViewById(R.id.Hotels_fragment_search_by_icon);


        HotelsRecyclerAdapter adapter=new HotelsRecyclerAdapter(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        getData();

        viewModel.getHotels().observe(getActivity(), new Observer<ArrayList<Hotels>>() {
            @Override
            public void onChanged(ArrayList<Hotels> hotels) {


                adapter.setHotels(hotels);
            }
        });

        searchViewDetails(searchView);
        searchByIconChanger(searchBy,searchView);



        return v;

    }

    private void searchByIconChanger(ImageView searchBy, SearchView searchView) {
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
        ArrayList<Hotels> hotels=new ArrayList<>();
        collectionReferenceHotel.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                        Hotels hotelsSnap=snapshot.toObject(Hotels.class);
                        hotels.add(hotelsSnap);

                    }
                    viewModel.setHotels(hotels);



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

        ArrayList<Hotels> hotels=new ArrayList<>();

        collectionReferenceHotel.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){

                    for(QueryDocumentSnapshot snapshot :queryDocumentSnapshots ){
                        Hotels hotelSnap =snapshot.toObject(Hotels.class);

                        if(search_state==1){
                            int result =calculate(hotelSnap.getCity(),s);
                            if (result <= 3) {
                                // Document is similar enough, display it
                                // You can add it to a list of results or display it directly
                                hotels.add(hotelSnap);

                            }


                        } else if (search_state==2) {
                            int result =calculate(hotelSnap.getHotelName(),s);
                            if (result <= 3) {
                                // Document is similar enough, display it
                                // You can add it to a list of results or display it directly
                                hotels.add(hotelSnap);

                            }

                        }else {
                            Toast.makeText(getActivity(), "Please choose filter", Toast.LENGTH_SHORT).show();
                        }





                    }
                    viewModel.setHotels(hotels);

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