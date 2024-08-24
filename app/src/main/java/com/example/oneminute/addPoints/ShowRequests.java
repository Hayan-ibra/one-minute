package com.example.oneminute.addPoints;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.oneminute.R;
import com.example.oneminute.addPoints.adapter.RequestsAdapter;
import com.example.oneminute.models.RequestPoints;
import com.example.oneminute.models.Users;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShowRequests extends AppCompatActivity {
    RecyclerView recyclerView;

    RequestsAdapter adapter;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("PointsRequest");
    ArrayList<RequestPoints> requestPoints=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_requests);
        recyclerView=findViewById(R.id.ShowRequests_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getdata();
        adapter=new RequestsAdapter(this,requestPoints);
        recyclerView.setAdapter(adapter);

    }

    private void getdata() {

        requestPoints.clear();
        collectionReference.whereEqualTo("userId", Users.getInstance().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value!=null){
                    for (QueryDocumentSnapshot snapshot:value){
                        RequestPoints request=snapshot.toObject(RequestPoints.class);
                        requestPoints.add(request);
                    }

                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(ShowRequests.this, "Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}