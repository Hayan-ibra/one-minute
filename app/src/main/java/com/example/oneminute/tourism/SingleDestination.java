package com.example.oneminute.tourism;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codesgood.views.JustifiedTextView;
import com.example.oneminute.R;
import com.example.oneminute.hotels.recyclers.ImagesRecyclerAdapter;
import com.example.oneminute.hotels.viewmodel.ImagesViewModel;
import com.example.oneminute.models.RoomImages;
import com.example.oneminute.models.TouristDestinations;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SingleDestination extends AppCompatActivity {

    private TouristDestinations destinations;

    private RecyclerView recyclerView;
    private JustifiedTextView tv_decription;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private FloatingActionButton floatingActionButton;
    private MaterialToolbar toolbar;

    ImagesViewModel imagesViewModel;
    TextView tv_title;

    ArrayList<RoomImages> imagesArrayList;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceRooms=db.collection("RoomImages");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_destination);

        toolbar=findViewById(R.id.SingleDestination_tool);
        tv_decription=findViewById(R.id.SingleDestination_text_description);
        recyclerView=findViewById(R.id.SingleDestination_recycler);
        collapsingToolbarLayout=findViewById(R.id.SingleDestination_collapse);
        tv_title=findViewById(R.id.SingleDestination_text_title);
        imagesViewModel=new ViewModelProvider(this).get(ImagesViewModel.class);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        destinations= (TouristDestinations) intent.getSerializableExtra("dest");
        tv_decription.setText(destinations.getDescription());
        tv_title.setText(destinations.getName());

        collapsingToolbarLayout.setTitle(destinations.getName());

        toolbar.setTitle(destinations.getName());
        getData();
        ImagesRecyclerAdapter adapter=new ImagesRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        ////////////////////////////////////////////////////
        RoomImages roomImages1=new RoomImages();
        imagesArrayList=new ArrayList<>();
        roomImages1.setRoomId(destinations.getId());
        roomImages1.setImageURL(destinations.getImageUrl());
        roomImages1.setImageId(destinations.getId());
        imagesArrayList.add(roomImages1);
        adapter.setImages(imagesArrayList);
        ///////////////////////////////////////////////////

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        imagesViewModel.getImages().observe(this, new Observer<ArrayList<RoomImages>>() {
            @Override
            public void onChanged(ArrayList<RoomImages> roomImages) {
                imagesArrayList.clear();
                imagesArrayList.addAll(roomImages);
                RoomImages roomImages1=new RoomImages();
                roomImages1.setRoomId(destinations.getId());
                roomImages1.setImageURL(destinations.getImageUrl());
                roomImages1.setImageId(destinations.getId());
                imagesArrayList.add(roomImages1);

                adapter.setImages(imagesArrayList);
            }
        });


    }


    private void getData() {


        collectionReferenceRooms.whereEqualTo("roomId", destinations.getId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {

                    ArrayList<RoomImages> roomsimg = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : value) {

                        RoomImages roomI = snapshot.toObject(RoomImages.class);
                        roomsimg.add(roomI);
                    }


                    imagesViewModel.setImages(roomsimg);
                } else {
                    // Handle the error
                    Toast.makeText(SingleDestination.this, "ntohing found", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}