package com.example.oneminute.restaurants.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.oneminute.models.Orders;

import java.util.ArrayList;

public class OrderViewModel extends AndroidViewModel {
    private OrderRepository repository;
    private LiveData<ArrayList<Orders>> itemsLiveData;


    public OrderViewModel(@NonNull Application application) {

        super(application);
        repository = new OrderRepository();
        itemsLiveData = repository.getItems();
    }




    public LiveData<ArrayList<Orders>> getOrders() {
        return itemsLiveData;
    }

}
