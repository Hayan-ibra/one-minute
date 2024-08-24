package com.example.oneminute.restaurants.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PointsViewModel extends AndroidViewModel {

    MutableLiveData<Double> points=new MutableLiveData<>();

    public PointsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Double> getPoints() {
        return points;
    }



    public void setPoints(Double newPoints) {
        points.setValue(newPoints);



    }





}
