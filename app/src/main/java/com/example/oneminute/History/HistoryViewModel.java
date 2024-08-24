package com.example.oneminute.History;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.oneminute.models.History;

import java.util.ArrayList;

public class HistoryViewModel extends AndroidViewModel {
    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }
    MutableLiveData<ArrayList<History>> histories=new MutableLiveData<>();


    public void setHistoryList(ArrayList<History> newHistory){

        histories.setValue(newHistory);
    }


    public LiveData<ArrayList<History>> getHistoryList (){
        return histories;
    }


}
