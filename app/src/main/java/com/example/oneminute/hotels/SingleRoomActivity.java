package com.example.oneminute.hotels;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
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
import com.example.oneminute.hotels.bootomsheets.DatePickerFragment;
import com.example.oneminute.hotels.bootomsheets.ReserveBottomSheet;
import com.example.oneminute.hotels.viewmodel.TimeStampsViewModel;
import com.example.oneminute.models.HotelRoom;
import com.example.oneminute.models.Hotels;
import com.example.oneminute.models.Reservation;
import com.example.oneminute.models.Users;
import com.example.oneminute.restaurants.FoodSingleItem;
import com.example.oneminute.shopping.SingleItemActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SingleRoomActivity extends AppCompatActivity implements ReserveBottomSheet.openDateFragment,ReserveBottomSheet.ItemReserveCallback{
    ImageView iv_profile;
    TextView tv_title,tv_price,tv_type,tv_services;
    MaterialButton btn_show,btn_reserve;
    Users users=Users.getInstance();

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceHotel=db.collection("HotelOwnersN");
    private CollectionReference collectionReferenceRoom=db.collection("HotelRooms");
    private CollectionReference collectionReferenceReservations=db.collection("ReservationsN");
    private CollectionReference collectionReferenceReservationsHistory=db.collection("ReservationsHistory");
    private CollectionReference collectionReferenceUsers =db.collection("Users");
    private CollectionReference collectionReferenceHistory =db.collection("History");

    DocumentSnapshot hotelDocReference;
    DocumentSnapshot userDocReference;
    HotelRoom room;
    Hotels hotels;
    int casee=0;
    int count=0;
    Reservation reservation;
    ArrayList<Timestamp> timestampGroup=new ArrayList<>();
    TimeStampsViewModel viewModel;
    int dayCount=1;
    DocumentReference reservationRef ;
    // Step 2: Get the generated ID
    String reservationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_room);
        iv_profile=findViewById(R.id.SingleRoomActivity_imageview);
        tv_title=findViewById(R.id.SingleRoomActivity_text_title);
        tv_type=findViewById(R.id.SingleRoomActivity_text_type);
        tv_price=findViewById(R.id.SingleRoomActivity_text_price);
        tv_services=findViewById(R.id.SingleRoomActivity_text_services);
        btn_reserve=findViewById(R.id.SingleRoomActivity_button_reserve);
        btn_show=findViewById(R.id.SingleRoomActivity_bbutton_show_images);
        viewModel=new ViewModelProvider(this).get(TimeStampsViewModel.class);
        Intent intent=getIntent();
        room= (HotelRoom) intent.getSerializableExtra("room");
        hotels= (Hotels) intent.getSerializableExtra("hotel");
        initialize(room);
        timestampGroup.clear();
        btn_reserve.setEnabled(false);
        getReservationData();

        viewModel.getTimestamps().observe(this, new Observer<ArrayList<Timestamp>>() {
            @Override
            public void onChanged(ArrayList<Timestamp> timestamps) {
                timestampGroup.clear();
                timestampGroup.addAll(timestamps);
                editButtonState(timestampGroup);
            }
        });


        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01=new Intent(SingleRoomActivity.this,DisplayRoomImages.class);
                intent01.putExtra("room1",room);
                startActivity(intent01);

            }
        });

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* HotelRateBottomSheet dialog = new HotelRateBottomSheet(SingleHotelActivity.this);
                dialog.show(getSupportFragmentManager(), "aab");*/

                ReserveBottomSheet dialog=new ReserveBottomSheet(SingleRoomActivity.this,SingleRoomActivity.this);
                dialog.show(getSupportFragmentManager(),"ddd");



            }
        });
    }

    private void editButtonState(ArrayList<Timestamp> timestampGroup) {
        Date currentDate = new Date();
        for (Timestamp userDate :timestampGroup){
            Date dateToObserve =userDate.toDate();
            if (userDate.toDate().getDay()==(currentDate.getDay()) && userDate.toDate().getMonth()==(currentDate.getMonth())&& userDate.toDate().getYear()==(currentDate.getYear())) {
                btn_reserve.setEnabled(false);
                btn_reserve.setText("Already reserved");
            }


            }


    }

    /* private void showDatePickerDialog() {
         DatePickerFragment newFragment = new DatePickerFragment();
         newFragment.setOnDateSetListener(dateSetListener);
         newFragment.show(getSupportFragmentManager(), "datePicker");
     }
     private final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
         Calendar calendar = Calendar.getInstance();
         calendar.set(year, month, dayOfMonth);
         Date selectedDate = calendar.getTime();

         // Convert to Firestore Timestamp
         Timestamp timestamp = new Timestamp(selectedDate);

         // Upload to Firestore
         uploadDateToFirestore(timestamp);
     };
     private void uploadDateToFirestore(Timestamp timestamp) {

         Toast.makeText(this, "slelcted "+timestamp, Toast.LENGTH_SHORT).show();
         collectionReferenceReservations.whereEqualTo("hotelId",hotels.getHotelId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
             @Override
             public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                 if (!value.isEmpty()){
                     ArrayList<Timestamp> date=new ArrayList<>();
                     for (QueryDocumentSnapshot snapshot :value){
                         Reservation reservation=snapshot.toObject(Reservation.class);
                         date.addAll(reservation.getDate());
                     }


                 }else {

                 }
             }
         });

     }
 */
    private void initialize(HotelRoom room) {
        Glide.with(SingleRoomActivity.this).load(room.getImageUrl()).into(iv_profile);
        tv_title.setText("Room : "+room.getRoomNum());
        tv_price.setText(room.getPricePerDay()+" $");
        tv_type.setText(room.getType());
        String services="";
        List<String> serv=  room.getServices();
        for (int i=0 ; i<serv.size();i++){
            //services=services+serv.get(i)+", ";
            if(i==serv.size()-1){
                services=services+serv.get(i)+" ";

            }else {
                services=services+serv.get(i)+", ";;

            }
        }
        tv_services.setText(services);
    }



    void getReservationData(){

        ArrayList<Timestamp> tss=new ArrayList<>();
        collectionReferenceReservations.whereEqualTo("roomId",room.getRoomId()).get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()){

                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                        Reservation reservation=snapshot.toObject(Reservation.class);
                        tss.addAll(reservation.getDate());
                    }

                    btn_reserve.setEnabled(true);
                    viewModel.setTimestamps(tss);
                }else {
                    casee=9;
                    btn_reserve.setEnabled(true);
                    /*


                     */

                }

            }
        });


        collectionReferenceUsers.whereEqualTo("uid",hotels.getHotelId()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                hotelDocReference = value.getDocuments().get(0);
                if(value!=null){
                    for(QueryDocumentSnapshot snapshot : value){
                        Users users1=snapshot.toObject(Users.class);
                       // respoints[0] =users1.getPoints();

                    }
                }
            }
        });
        collectionReferenceUsers.whereEqualTo("uid",users.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userDocReference = value.getDocuments().get(0);
                if(value!=null){
                    for(QueryDocumentSnapshot snapshot : value){
                        Users users2=snapshot.toObject(Users.class);
                       // userPoints[0] = users2.getPoints();

                    }
                }
            }
        });



    }


    @Override
    public void onSuccess(ArrayList<Timestamp> timestamp,int dayc) {

        dayCount = dayc;
        Double priceCC = dayc * room.getPricePerDay();
        Toast.makeText(this, "" + priceCC, Toast.LENGTH_SHORT).show();

        if (priceCC < users.getPoints()) {
            compareTimestamps(timestamp, timestampGroup);
            reservation = new Reservation();
            reservation.setRoomId(room.getRoomId());
            reservation.setUserId(users.getUid());
            reservation.setRoomName(room.getRoomNum());
            reservation.setHotelName(hotels.getHotelName());
            reservation.setDate(timestamp);
            reservation.setUserName(users.getUsername());
            reservation.setEndPrice(priceCC*100);
            String docId=UUID.randomUUID().toString();
            reservation.setDocId(docId);

        } else {
            Toast.makeText(this, "Insufficient funds", Toast.LENGTH_SHORT).show();
        }
        }



    public void compareTimestamps(ArrayList<Timestamp> userDates, ArrayList<Timestamp> firestoreDates) {
        Log.e("hayan","firestore date size : "+firestoreDates.size());
        for (Timestamp userDate : userDates) {
            for (Timestamp firestoreDate : firestoreDates) {
                Log.e("hayan",firestoreDate.toDate().toString());
                //userDate.toDate().equals(firestoreDate.toDate())
                ///userDate.toDate().getDay()==(firestoreDate.toDate().getDay()) && userDate.toDate().getMonth()==(firestoreDate.toDate().getMonth()&& userDate.toDate().getYear()==(firestoreDate.toDate().getYear())
                if (userDate.toDate().getDay()==(firestoreDate.toDate().getDay()) && userDate.toDate().getMonth()==(firestoreDate.toDate().getMonth())&& userDate.toDate().getYear()==(firestoreDate.toDate().getYear())) {
                    casee=1;
                    count=count+1;
                    System.out.println("Overlap detected on date: " + userDate.toDate());
                    Toast.makeText(this, "Overlap detected on date: " + userDate.toDate(), Toast.LENGTH_SHORT).show();



                    //return  ;  // Prevent reservation
                }
            }
        }


     reserveNow(userDates);


    }

    private void reserveNow(ArrayList<Timestamp> timestamp) {
        String documentId;
        Date currentDate = new Date();
        for (int i=0 ; i<timestamp.size();i++){
            Date date2=timestamp.get(i).toDate();
            long millisecondsBetween = currentDate.getTime() - date2.getTime();
            long daysBetween = TimeUnit.MILLISECONDS.toDays(millisecondsBetween);
            Log.e("hayan","days : "+daysBetween);

            if (currentDate.after(date2)){
                casee=2;
            }
            if (daysBetween < -30){
                casee=3;
            }
        }


        if (count==0){
            PointsTransaction(room.getPricePerDay(),dayCount,timestamp);
          /*  Reservation reservation=new Reservation();
            reservation.setRoomId(room.getRoomId());
            reservation.setUserId(users.getUid());
            reservation.setDate(timestamp);*/
           /* collectionReferenceReservations.add(reservation).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {

                    PointsTransaction(room.getPricePerDay(),dayCount);
                    Toast.makeText(SingleRoomActivity.this, "Reserved successfullyyy"+timestampGroup.size()+"count"+count, Toast.LENGTH_SHORT).show();
                }
            });*/
            // Create a reference to the document
           /* documentId=UUID.randomUUID().toString();
            reservationRef = collectionReferenceReservations.document(documentId);

            // Create a map to hold the document data
            Map<String, Object> data = new HashMap<>();
            data.put("roomId", room.getRoomId());
            data.put("userId", users.getUid());
            data.put("date",timestamp);*/
/*
    private String roomId;
    private String userId;

    private ArrayList<Timestamp> date;
 */
            // Set the document with the data
          /*  reservationRef.set(data).addOnSuccessListener(aVoid -> {
                // Document successfully written

                System.out.println("Document successfully written with ID: " + documentId);
            }).addOnFailureListener(e -> {
                // Document write failed
                System.err.println("Error writing document: " + e);
            });*/


        } else if (casee==9) {



            PointsTransaction(room.getPricePerDay(),dayCount,timestamp);
           // Log.e("hayan","empty");
           /* Reservation reservation=new Reservation();
            reservation.setRoomId(room.getRoomId());
            reservation.setUserId(users.getUid());
            reservation.setDate(timestamp);
            Log.e("hayan","outside");
            collectionReferenceReservations.add(reservation).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    PointsTransaction(room.getPricePerDay(),dayCount);
                    Log.e("hayan","inside");
                    Toast.makeText(SingleRoomActivity.this, "Reserved successfully"+timestampGroup.size(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("hayan","failure");
                    Toast.makeText(SingleRoomActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });*/
          /* documentId=UUID.randomUUID().toString();
            reservationRef = collectionReferenceReservations.document(documentId);

            // Create a map to hold the document data
            Map<String, Object> data = new HashMap<>();
            data.put("roomId", room.getRoomId());
            data.put("userId", users.getUid());
            data.put("date",timestamp);
            // Set the document with the data
            reservationRef.set(data).addOnSuccessListener(aVoid -> {
                PointsTransaction(room.getPricePerDay(),dayCount,documentId,timestamp);
                // Document successfully written
                System.out.println("Document successfully written with ID: " + documentId);
            }).addOnFailureListener(e -> {
                // Document write failed
                System.err.println("Error writing document: " + e);
            });*/
        } else if (casee==3) {
            Toast.makeText(SingleRoomActivity.this, "sorry you cant reserve after month", Toast.LENGTH_SHORT).show();
            casee=0;
        } else if (casee==1) {
            Toast.makeText(SingleRoomActivity.this, "sorry its reserved", Toast.LENGTH_SHORT).show();
            casee=0;
        } else if (casee==2) {
            Toast.makeText(SingleRoomActivity.this, "Sorry you cant reserve in the past", Toast.LENGTH_SHORT).show();
            casee=0;

        }

    }

    @Override
    public void openDialog(Calendar calendar) {
        Toast.makeText(this, ""+calendar, Toast.LENGTH_SHORT).show();

    }



    private void PointsTransaction( Double price,int quantity,ArrayList<Timestamp> timestamp) {

        // Create a reference to the document
        String documentId=UUID.randomUUID().toString();
        reservationRef = collectionReferenceReservations.document(documentId);

        // Create a map to hold the document data
        Map<String, Object> data = new HashMap<>();
        data.put("roomId", room.getRoomId());
        data.put("userId", users.getUid());
        data.put("date",timestamp);
        data.put("roomName",room.getRoomNum());
        data.put("hotelName",hotels.getHotelName());
        data.put("userName",users.getUsername());
        data.put("endPrice",(price*quantity)*100);
        String docId=UUID.randomUUID().toString();
        data.put("docId",docId);


        String documentHistoryId=UUID.randomUUID().toString();
        DocumentReference referenceHistory = collectionReferenceHistory.document(documentHistoryId);
        Map<String, Object> history = new HashMap<>();
        history.put("id", users.getUid());
        history.put("costumerName", users.getUsername());
        history.put("parentName",hotels.getHotelName());
        history.put("quantity",String.valueOf(quantity));
        history.put("name","Room : "+room.getRoomNum());
        history.put("type","reservation");
        history.put("date",Timestamp.now());
        history.put("value",(price*quantity)*100);
        history.put("state",true);

        String documentHistoryId2=UUID.randomUUID().toString();
        DocumentReference referenceHistoryHotel = collectionReferenceHistory.document(documentHistoryId2);
        Map<String, Object> historyHotel = new HashMap<>();
        historyHotel.put("id", hotels.getHotelId());
        historyHotel.put("costumerName",hotels.getHotelName() );
        historyHotel.put("parentName",users.getUsername());
        historyHotel.put("quantity",String.valueOf(quantity));
        historyHotel.put("name","Room : "+room.getRoomNum());
        historyHotel.put("type","reservation");
        historyHotel.put("date",Timestamp.now());
        historyHotel.put("value",(price*quantity)*100);
        historyHotel.put("state",false);






        Double pricee=(price*quantity)*100;

        //String receiverID=hotels.getHotelId();
        //String senderID=users.getUid();

        final Double[] respoints = new Double[1];
        final Double[] userPoints = new Double[1];







        String hotelId = hotelDocReference.getId();
        String userId=  userDocReference.getId();
        final DocumentReference userRef = db.collection("Users").document(userId);
        final DocumentReference hotelRef = db.collection("Users").document(hotelId);


        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot userSnapshot = transaction.get(userRef);
            DocumentSnapshot hoteltSnapshot = transaction.get(hotelRef);

            Double userPointss = userSnapshot.getDouble("points");
            Double hotelPoints = hoteltSnapshot.getDouble("points");


            //Toast.makeText(SingleRoomActivity.this, "hotel points : "+hotelPoints+" user points : "+userPointss, Toast.LENGTH_SHORT).show();
            if (userPointss >= pricee) {
                // Subtract points from user
                transaction.update(userRef, "points", userPointss - pricee);

                // Add points to restaurant
                transaction.update(hotelRef, "points", hotelPoints + pricee);

                transaction.set(reservationRef,data);
                transaction.set(referenceHistory,history);
                transaction.set(referenceHistoryHotel,historyHotel);

                //requestOrder(senderID,receiverID,food.getName(),users.getUsername(),notes,quantity,pricee);
                //addToHistory(senderID,"food",food.getName(),pricee,quantity,food.getRestaurantID(),users.getUsername());

            } else {
                /*db.collection("ReservationsN").document(documentId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SingleRoomActivity.this, "all have been deleted", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SingleRoomActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
                //
                throw new FirebaseFirestoreException("Not enough points", FirebaseFirestoreException.Code.ABORTED);*/
                Toast.makeText(SingleRoomActivity.this, "not enough", Toast.LENGTH_SHORT).show();

            }

            return null;
        }).addOnSuccessListener(aVoid -> {

            Toast.makeText(this, "Reserved successfully", Toast.LENGTH_SHORT).show();

            // Transaction success
            Log.d("hayan", "Transaction success!");
            //Toast.makeText(SingleRoomActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {

           /* db.collection("ReservationsN").document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(SingleRoomActivity.this, "all have been deleted", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SingleRoomActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            });
            // unDoUploading();
            // Transaction failur
            //Toast.makeText(SingleRoomActivity.this, "Failed cause :"+e, Toast.LENGTH_SHORT).show();
            Log.w("hayan", "Transaction failure.", e);*/
        });


    }

   /* private void unDoUploading() {
        collectionReferenceReservations.orderBy("timestampField", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Toast.makeText(SingleRoomActivity.this, "inside", Toast.LENGTH_SHORT).show();
                            // Do something with the most recent document

                            // Step 1: Generate a new document reference with a unique ID
                            reservationRef  = document.getReference();

                            reservationRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(SingleRoomActivity.this, "reservation have been canceled", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SingleRoomActivity.this, "shiiiiit", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    } else {
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SingleRoomActivity.this, "Failed to retreive date", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timestampGroup.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timestampGroup.clear();
    }
}