package com.example.oneminute.shopping.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oneminute.models.StoreItem;

import java.util.ArrayList;
import java.util.List;

public class SingleStoreItemViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<StoreItem>> items=new MutableLiveData<>();
    public SingleStoreItemViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<StoreItem>> getItems() {
        return items;
    }

    public void setItems(ArrayList<StoreItem> itemss) {

        items.setValue(itemss);
    }

}
