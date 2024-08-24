package com.example.oneminute.hotels.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oneminute.models.Hotels;
import com.google.firebase.Timestamp;


import java.util.ArrayList;

public class TimeStampsViewModel extends AndroidViewModel{

    private MutableLiveData<ArrayList<Timestamp>> timestamps;

    public TimeStampsViewModel(@NonNull Application application) {
        super(application);
        timestamps=new MutableLiveData<>(new ArrayList<>());
    }



    public LiveData<ArrayList<Timestamp>> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(ArrayList<Timestamp> timestampsNew) {
        timestamps.setValue(timestampsNew);
    }


}


