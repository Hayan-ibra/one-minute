package com.example.oneminute.restaurants.order;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oneminute.HomeActivity;
import com.example.oneminute.models.Orders;
import com.example.oneminute.models.Users;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OrderRepository {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceOrders=db.collection("OrdersN");
    private MutableLiveData<ArrayList<Orders>> itemsLiveData = new MutableLiveData<>();
    Users user =Users.getInstance();

    public LiveData<ArrayList<Orders>> getItems() {
        if (itemsLiveData.getValue() == null) {
            fetchItemsFromFirestore();
        }
        return itemsLiveData;
    }

    private void fetchItemsFromFirestore() {
        collectionReferenceOrders.whereEqualTo("userId",user.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    ArrayList<Orders> orderList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        Orders order = document.toObject(Orders.class);
                        orderList.add(order);
                    }

                    Collections.sort(orderList, new Comparator<Orders>() {
                        @Override
                        public int compare(Orders o1, Orders o2) {
                            return o2.getTime().compareTo(o1.getTime());
                        }
                    });
                    itemsLiveData.setValue(orderList);
                } else {


                }



            }
        });

    }




}
