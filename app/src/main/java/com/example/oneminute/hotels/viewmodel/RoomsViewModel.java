package com.example.oneminute.hotels.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oneminute.models.HotelRoom;


import java.util.ArrayList;

public class RoomsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<HotelRoom>> rooms;

    public RoomsViewModel(@NonNull Application application) {
        super(application);
        rooms=new MutableLiveData<>(new ArrayList<>());
    }


    public LiveData<ArrayList<HotelRoom>> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<HotelRoom> roomsNew) {
        rooms.setValue(roomsNew);
    }
}
