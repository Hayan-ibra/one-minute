package com.example.oneminute.hotels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.hotels.bootomsheets.HotelRateBottomSheet;
import com.example.oneminute.hotels.recyclers.RoomsRecyclerAdapter;
import com.example.oneminute.hotels.viewmodel.HotelsViewModel;
import com.example.oneminute.hotels.viewmodel.RoomsViewModel;
import com.example.oneminute.models.HotelRate;
import com.example.oneminute.models.HotelRoom;
import com.example.oneminute.models.Hotels;
import com.example.oneminute.models.Rate;
import com.example.oneminute.models.Users;
import com.example.oneminute.restaurants.FoodBottomSheet;
import com.example.oneminute.restaurants.FoodSingleItem;
import com.example.oneminute.shopping.SingleItemActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SingleHotelActivity extends AppCompatActivity implements HotelRateBottomSheet.ItemOrderCallback {
    TextView tv_title, tv_rate, tv_location, tv_phone, tv_description, tv_city;
    ImageView iv_profile;
    CardView back;
    RecyclerView recyclerView;
    Hotels hotel;
    RoomsViewModel viewModel;
    HotelsViewModel hotelsViewModel;

    RoomsRecyclerAdapter adapter;
    Users users = Users.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceRooms = db.collection("HotelRooms");
    private CollectionReference collectionReferenceHotel = db.collection("HotelOwnersN");
    private CollectionReference collectionReferenceRate = db.collection("HotelRatesN");

    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceRate2 = db2.collection("HotelRates");

    DocumentReference referenceHotel;

    Double currentRate;
    Long numberOfPreviewers;

    Double oldRate;
    int conditionRate = 0;


    private Double globalrate = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_hotel);
        tv_title = findViewById(R.id.SingleHotelActivity_text_title);
        tv_rate = findViewById(R.id.SingleHotelActivity_text_rate);
        tv_location = findViewById(R.id.SingleHotelActivity_text_location);
        tv_phone = findViewById(R.id.SingleHotelActivity_text_phone);
        tv_description = findViewById(R.id.SingleHotelActivity_text_description);
        tv_city = findViewById(R.id.SingleHotelActivity_text_city);
        iv_profile = findViewById(R.id.SingleHotelActivity_profile_image);
        //back = findViewById(R.id.SingleHotelActivity_back_button);
        recyclerView = findViewById(R.id.SingleHotelActivity_recycler_rooms);


        Intent intent = getIntent();
        hotel = (Hotels) intent.getSerializableExtra("hotel");
        viewModel = new ViewModelProvider(this).get(RoomsViewModel.class);
        hotelsViewModel = new ViewModelProvider(this).get(HotelsViewModel.class);
        getdata(hotel.getHotelId());
        initiate(hotel);
        adapter = new RoomsRecyclerAdapter(this,hotel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        viewModel.getRooms().observe(this, new Observer<ArrayList<HotelRoom>>() {
            @Override
            public void onChanged(ArrayList<HotelRoom> hotelRooms) {
                adapter.setRooms(hotelRooms);

            }
        });


        tv_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HotelRateBottomSheet dialog = new HotelRateBottomSheet(SingleHotelActivity.this);
                dialog.show(getSupportFragmentManager(), "aab");

            }
        });





    }

    private void getdata(String hotelId) {
        ArrayList<HotelRoom> rooms = new ArrayList<>();
        collectionReferenceRooms.whereEqualTo("parentId", hotelId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        HotelRoom room = snapshot.toObject(HotelRoom.class);
                        rooms.add(room);
                    }

                    viewModel.setRooms(rooms);


                } else {
                    Toast.makeText(SingleHotelActivity.this, "nothing found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initiate(Hotels hotel) {
        tv_city.setText(hotel.getCity());
        tv_title.setText(hotel.getHotelName());
        tv_description.setText(hotel.getDescription());
        tv_rate.setText(String.format("%.1f",Math.ceil(hotel.getRate() * 100) / 100.0).toString());
        tv_location.setText(hotel.getLocation());
        tv_phone.setText(hotel.getPhoneNumber());
        Glide.with(SingleHotelActivity.this).load(hotel.getImageUrl()).into(iv_profile);

    }


    @Override
    public void onSuccess(Double submittedRate) {
        HotelRate rateee = new HotelRate();
        rateee.setSubRate(submittedRate);
        rateee.setRaterId(users.getUid());
        rateee.setHotelId(hotel.getHotelId());

        collectionReferenceRate.whereEqualTo("HotelId", hotel.getHotelId()).whereEqualTo("RaterId", users.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value.isEmpty()) {
                            conditionRate = 2;

                            Log.e("hayan","non exist rate");
                            Log.e("hayan","non exist rate"+submittedRate+users.getUid()+hotel.getHotelId());
                            collectionReferenceRate.add(rateee).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    Log.e("hayan","uploaded  new rate");
                                    Toast.makeText(SingleHotelActivity.this, "Successfully aded", Toast.LENGTH_SHORT).show();
                                    updateHotelRate(submittedRate);
                                }
                            });

                        } else {
                            Log.e("hayan","existing rate");
                            DocumentSnapshot snapshot = value.getDocuments().get(0);
                            DocumentReference documentReferencee = snapshot.getReference();
                            HotelRate rate = snapshot.toObject(HotelRate.class);
                            oldRate = rate.getSubRate();
                            conditionRate = 1;

                            db.runTransaction((Transaction.Function<Void>) transaction -> {
                                DocumentSnapshot snapshot11 = transaction.get(documentReferencee);
                                Map<String, Object> hotelRates = new HashMap<>();
                                hotelRates.put("HotelId", hotel.getHotelId());
                                hotelRates.put("subRate", currentRate);
                                hotelRates.put("RaterId", users.getUid());
                                transaction.set(documentReferencee, hotelRates);

                                return null;
                            }).addOnSuccessListener(aVoid -> {

                                Toast.makeText(SingleHotelActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                updateHotelRate(submittedRate);

                            }).addOnFailureListener(e -> {
                                // Transaction failure
                                Toast.makeText(SingleHotelActivity.this, "failure", Toast.LENGTH_SHORT).show();
                            });




                        }
                    }
                });

        Log.e("hayan","entered the method");

        collectionReferenceHotel.whereEqualTo("hotelId", hotel.getHotelId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    Log.e("hayan","found hotel");
                    for (QueryDocumentSnapshot snapshot : value) {
                        Hotels hotel = snapshot.toObject(Hotels.class);
                        currentRate = hotel.getRate();
                        numberOfPreviewers = hotel.getNumberOfPreviews();
                        referenceHotel = snapshot.getReference();


                    }
                }else {
                    Log.e("hayan","no hotel");
                }

            }
        });
        // private String HotelId;
        //    private String RaterId;
        //    private Double subRate;











    }

    void  updateHotelRate(Double submittedRate){
        if (conditionRate == 1) {

            db.runTransaction((Transaction.Function<Void>) transaction -> {
                // Get the snapshots
                Log.e("hayan","inside transaction");
                DocumentSnapshot hotelSnapShot = transaction.get(referenceHotel);
                Double rate = hotelSnapShot.getDouble("rate");
                Long numofprev = hotelSnapShot.getLong("numberOfPreviews");
                double f1 = rate * numofprev;
                double f2 = (f1 - rate) / (numofprev - 1);
                //System.out.println("f2 = "+f2);
                double f3 = (f2 * (numofprev - 1)) + submittedRate;
                double f4 = f3 / numofprev;
                //System.out.println("f4= "+f4);

                System.out.println("number of raters : " + numberOfPreviewers);
                transaction.update(referenceHotel, "rate", f4);
                transaction.update(referenceHotel, "numberOfPreviews", numofprev);

                return null;
            }).addOnSuccessListener(aVoid -> {
                // Transaction success
                System.out.println("Points transferred and item ordered successfully!");
                Log.e("hayan","success ");
                Toast.makeText(SingleHotelActivity.this, "Points transferred and item ordered successfully!", Toast.LENGTH_SHORT).show();

                //addToHistory(senderID,"food",food.getName(),pricee,quantity,food.getRestaurantID(),users.getUsername());
            }).addOnFailureListener(e -> {
                // Transaction failure
                Log.e("hayan","failure");
                System.err.println("Failed to complete transaction: " + e.getMessage());
                Toast.makeText(SingleHotelActivity.this, "Failed to complete transaction:", Toast.LENGTH_SHORT).show();
            });

        } else if (conditionRate == 2) {
            db.runTransaction((Transaction.Function<Void>) transaction -> {
                // Get the snapshots
                Log.e("hayan","transaction 2");
                DocumentSnapshot hotelSnapShot = transaction.get(referenceHotel);

                Double rate = hotelSnapShot.getDouble("rate");
                Long numofprev = hotelSnapShot.getLong("numberOfPreviews");

                double f1 = rate * numberOfPreviewers;

                double f2 = (f1 + submittedRate) / (numberOfPreviewers + 1);


                //System.out.println("f4= "+f4);

                System.out.println("number of raters : " + numberOfPreviewers);
                transaction.update(referenceHotel, "rate", f2);
                transaction.update(referenceHotel, "numberOfPreviews", numofprev + 1);

                return null;
            }).addOnSuccessListener(aVoid -> {
                // Transaction success
                System.out.println("Points transferred and item ordered successfully!");
                Log.e("hayan","transaction 2 success");
                Toast.makeText(SingleHotelActivity.this, "Points transferred and item ordered successfully!", Toast.LENGTH_SHORT).show();

                //addToHistory(senderID,"food",food.getName(),pricee,quantity,food.getRestaurantID(),users.getUsername());
            }).addOnFailureListener(e -> {
                // Transaction failure
                Log.e("hayan","transaction 2 success");
                System.err.println("Failed to complete transaction: " + e.getMessage());
                Toast.makeText(SingleHotelActivity.this, "Failed to complete transaction:", Toast.LENGTH_SHORT).show();
            });


        }
    }

    void uploadNewRate(Double submittedRate){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);

    }
}