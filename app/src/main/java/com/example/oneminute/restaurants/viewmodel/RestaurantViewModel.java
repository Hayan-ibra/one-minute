package com.example.oneminute.restaurants.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.oneminute.models.Food;
import com.example.oneminute.models.RestaurantProfile;

import java.util.ArrayList;
import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {
    public RestaurantViewModel(@NonNull Application application) {
        super(application);
    }

    MutableLiveData<ArrayList<Food>> foods=new MutableLiveData<>();
    MutableLiveData<ArrayList<RestaurantProfile>> restaurants=new MutableLiveData<>();


    public void setDataList(ArrayList<Food> newFoods){

        foods.setValue(newFoods);
    }


    public LiveData<ArrayList<Food>> getDataList (){
        return foods;
    }


    public  void  setRestaurants(ArrayList<RestaurantProfile> newRestaurants){
        restaurants.setValue(newRestaurants);
    }

    public LiveData<ArrayList<RestaurantProfile>> getRestaurants(){
        return restaurants;
    }

}
