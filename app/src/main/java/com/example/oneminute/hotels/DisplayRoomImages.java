package com.example.oneminute.hotels;

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
import android.widget.Toast;

import com.example.oneminute.R;
import com.example.oneminute.hotels.recyclers.ImagesRecyclerAdapter;
import com.example.oneminute.hotels.viewmodel.ImagesViewModel;
import com.example.oneminute.models.HotelRoom;
import com.example.oneminute.models.RoomImages;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DisplayRoomImages extends AppCompatActivity {
    RecyclerView recyclerView;
    ImagesRecyclerAdapter adapter;
    ImagesViewModel viewModel;
    HotelRoom room;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceRooms=db.collection("RoomImages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_room_images);
        recyclerView=findViewById(R.id.DisplayRoomImages_recycler);
        adapter=new ImagesRecyclerAdapter(this);
        viewModel=new ViewModelProvider(this).get(ImagesViewModel.class);
        Intent intent=getIntent();
        room= (HotelRoom) intent.getSerializableExtra("room1");
        //   intent01.putExtra("room1",room);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        getImagesData(room.getRoomId());

        viewModel.getImages().observe(this, new Observer<ArrayList<RoomImages>>() {
            @Override
            public void onChanged(ArrayList<RoomImages> roomImages) {
                adapter.setImages(roomImages);
            }
        });

    }

    private void getImagesData(String roomId) {



        collectionReferenceRooms.whereEqualTo("roomId", roomId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {

                    ArrayList<RoomImages> roomsimg = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : value) {

                        RoomImages roomI = snapshot.toObject(RoomImages.class);
                        roomsimg.add(roomI);
                    }


                    viewModel.setImages(roomsimg);
                } else {
                    // Handle the error
                    Toast.makeText(DisplayRoomImages.this, "ntohing found", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


}