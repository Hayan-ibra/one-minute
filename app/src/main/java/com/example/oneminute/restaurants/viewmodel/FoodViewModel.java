package com.example.oneminute.restaurants.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oneminute.models.Food;

import java.util.ArrayList;

public class FoodViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<Food>> foods=new MutableLiveData<>();

    public FoodViewModel(@NonNull Application application) {
        super(application);
    }


    public void setFoodList(ArrayList<Food> newFoods){

        foods.setValue(newFoods);
    }


    public LiveData<ArrayList<Food>> getFoodList (){
        return foods;
    }
}
