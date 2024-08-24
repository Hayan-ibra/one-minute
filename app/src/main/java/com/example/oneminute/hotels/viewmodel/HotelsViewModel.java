package com.example.oneminute.hotels.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oneminute.models.Hotels;

import java.util.ArrayList;

public class HotelsViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Hotels>> hotels;


    public HotelsViewModel(@NonNull Application application) {
        super(application);
        hotels=new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<ArrayList<Hotels>> getHotels() {
        return hotels;
    }

    public void setHotels(ArrayList<Hotels> hotelsNew) {
        hotels.setValue(hotelsNew);
    }


}
