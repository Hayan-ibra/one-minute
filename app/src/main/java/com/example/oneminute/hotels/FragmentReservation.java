package com.example.oneminute.hotels;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oneminute.R;
import com.example.oneminute.hotels.recyclers.ReservationsRecyclerAdapter;
import com.example.oneminute.hotels.viewmodel.ReservationsViewModel;
import com.example.oneminute.models.History;
import com.example.oneminute.models.Reservation;
import com.example.oneminute.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class FragmentReservation extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ReservationsViewModel viewModel;
    Users user=Users.getInstance();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceReservations=db.collection("ReservationsN");


    private String mParam1;
    private String mParam2;

    public FragmentReservation() {
        // Required empty public constructor
    }


    public static FragmentReservation newInstance(String param1, String param2) {
        FragmentReservation fragment = new FragmentReservation();
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
        View v=inflater.inflate(R.layout.fragment_reservation, container, false);
        RecyclerView recyclerView=v.findViewById(R.id.fragment_reservations_recycler);
        viewModel=new ViewModelProvider(getActivity()).get(ReservationsViewModel.class);
        ReservationsRecyclerAdapter adapter=new ReservationsRecyclerAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        getReservationData();

        viewModel.getReservations().observe(getActivity(), new Observer<ArrayList<Reservation>>() {
            @Override
            public void onChanged(ArrayList<Reservation> reservations) {
                adapter.setReservations(reservations);
            }
        });



        return v;
    }

    private void getReservationData() {
        ArrayList <Reservation> res=new ArrayList<>();
        collectionReferenceReservations.whereEqualTo("userId",user.getUid()).get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                    Reservation reservation=snapshot.toObject(Reservation.class);
                    res.add(reservation);


                }

                    Collections.sort(res, new Comparator<Reservation>() {
                        @Override
                        public int compare(Reservation reservation, Reservation t1) {
                            return reservation.getDate().get(0).compareTo(t1.getDate().get(0));
                        }
                    });
                    viewModel.setReservations(res);}

            }
        });
    }
}