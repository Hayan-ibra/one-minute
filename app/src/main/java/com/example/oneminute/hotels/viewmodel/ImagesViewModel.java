package com.example.oneminute.hotels.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.oneminute.models.RoomImages;

import java.util.ArrayList;

public class ImagesViewModel extends AndroidViewModel {






    private MutableLiveData<ArrayList<RoomImages>> images;

    public ImagesViewModel(@NonNull Application application) {
        super(application);

        images=new MutableLiveData<>(new ArrayList<>());
        //images= (MutableLiveData<ArrayList<RoomImages>>) repository.getRoomImages();

    }

    public void setImages(ArrayList<RoomImages> imagesss) {
        images.setValue(imagesss);
    }

    /*
        public LiveData<ArrayList<RoomImages>> searchItems(String keyword) {
            return repository.getRoomImages(keyword);
        }
    */
    public LiveData<ArrayList<RoomImages>> getImages() {
        return images;
    }


}
