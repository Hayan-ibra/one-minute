package com.example.oneminute.shopping.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oneminute.models.Food;
import com.example.oneminute.models.StoreItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItemsViewModel extends AndroidViewModel {

    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceItems=db.collection("StoreItems");
    private MutableLiveData<ArrayList<StoreItem>> items=new MutableLiveData<>();



    public ItemsViewModel(@NonNull Application application) {
        super(application);

    }
    public LiveData<ArrayList<StoreItem>> getItems(List<String> cati) {
        if (items.getValue() == null) {
            searchItems(cati);
        }
        return items;
    }

    public void setItems(ArrayList<StoreItem> itemss) {

        items.setValue(itemss);
    }


    public void searchItems(List<String> cati){

        ArrayList<StoreItem> it=new ArrayList<>();
        collectionReferenceItems.whereArrayContainsAny("catigory", cati).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                it.clear();
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        StoreItem item=snapshot.toObject(StoreItem.class);
                        it.add(item);

                    }
                    setItems(it);


                }
            }
        });
    }



}
