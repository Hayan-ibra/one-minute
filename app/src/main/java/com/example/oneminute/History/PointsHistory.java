package com.example.oneminute.History;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oneminute.R;
import com.example.oneminute.models.History;
import com.example.oneminute.models.Rate;
import com.example.oneminute.models.Users;
import com.example.oneminute.restaurants.viewmodel.RestaurantViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PointsHistory extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    HistoryRecyclerAdapter adapter;

    ArrayList<History> histories;


    HistoryViewModel viewModel;


    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db =FirebaseFirestore.getInstance();

    private CollectionReference collectionReferenceHistory =db.collection("History");


    public PointsHistory() {
        // Required empty public constructor
    }



    public static PointsHistory newInstance(String param1, String param2) {
        PointsHistory fragment = new PointsHistory();
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

        View  v=inflater.inflate(R.layout.fragment_points_history, container, false);
        // Inflate the layout for this fragment

        RecyclerView rec=v.findViewById(R.id.history_points_recycler);

        Users user=Users.getInstance();
        user.getUid();




        viewModel = new ViewModelProvider(getActivity()).get(HistoryViewModel.class);
        getHistory( user.getUid());


        adapter=new HistoryRecyclerAdapter(histories,getActivity());


        rec.setAdapter(adapter);
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));



        viewModel.getHistoryList().observe(getActivity(), new Observer<ArrayList<History>>() {
            @Override
            public void onChanged(ArrayList<History> histories) {
                adapter.notifyDataSetChanged();
            }
        });








        return v;
    }


    void getHistory(String userId){

        histories=new ArrayList<>();
        collectionReferenceHistory.whereEqualTo("id",userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    for (QueryDocumentSnapshot snapshot: value){
                        History history =snapshot.toObject(History.class);
                        histories.add(history);



                    }

                    Collections.sort(histories, new Comparator<History>() {
                        @Override
                        public int compare(History o1, History o2) {
                            return o2.getDate().compareTo(o1.getDate());
                        }
                    });
                    viewModel.setHistoryList(histories);

                }else {
                    Toast.makeText(getActivity(), "History is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }






}