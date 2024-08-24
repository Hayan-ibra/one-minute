package com.example.oneminute.restaurants.order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oneminute.R;
import com.example.oneminute.models.Orders;
import com.example.oneminute.models.Users;

import java.util.ArrayList;

public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    OrderViewModel viewModel;
    Users user=Users.getInstance();



    OrderRecyclerAdapter adapter;


    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }


    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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

        View v=inflater.inflate(R.layout.fragment_order, container, false);
        RecyclerView rec=v.findViewById(R.id.order_recycler_orders);
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setHasFixedSize(true);
        adapter=new OrderRecyclerAdapter(user.getUsername(),getActivity());

        rec.setAdapter(adapter);
        viewModel=new ViewModelProvider(getActivity()).get(OrderViewModel.class);
        viewModel.getOrders().observe(getActivity(), new Observer<ArrayList<Orders>>() {
            @Override
            public void onChanged(ArrayList<Orders> orders) {
                Toast.makeText(getActivity(), ""+orders.size(), Toast.LENGTH_SHORT).show();
                adapter.setOrders(orders);


            }
        });




        return v;
    }
}