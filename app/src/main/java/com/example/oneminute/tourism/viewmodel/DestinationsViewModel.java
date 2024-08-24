package com.example.oneminute.tourism.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.oneminute.models.TouristDestinations;

import java.util.ArrayList;

public class DestinationsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<TouristDestinations>> destinations;
    public DestinationsViewModel(@NonNull Application application) {
        super(application);
        destinations=new MutableLiveData<>(new ArrayList<>());
    }



    public LiveData<ArrayList<TouristDestinations>> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<TouristDestinations> destinationNew) {
       destinations.setValue(destinationNew);
    }
}
