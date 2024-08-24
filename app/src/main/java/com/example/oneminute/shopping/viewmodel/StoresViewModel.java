package com.example.oneminute.shopping.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.oneminute.models.Store;
import com.example.oneminute.models.StoreItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StoresViewModel extends AndroidViewModel {
    private MutableLiveData<ArrayList<Store>> stores=new MutableLiveData<>();
    public StoresViewModel(@NonNull Application application) {
        super(application);
    }

    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceStores=db.collection("StoreOwnersN");


    public LiveData<ArrayList<Store>> getStores(String cat) {
        if (stores.getValue() == null) {
            searchStores(cat);
        }
        return stores;
    }

    public void setStores(ArrayList<Store> storess) {
       stores.setValue(storess);
    }


    public void searchStores(String cat){
        ArrayList<Store> st=new ArrayList<>();
        collectionReferenceStores.whereArrayContains("storeType",cat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()){
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        Store store=snapshot.toObject(Store.class);
                        st.add(store);

                    }
                    setStores(st);

                }

            }
        });
    }

    }
