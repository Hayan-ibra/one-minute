package com.example.oneminute.restaurants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oneminute.models.History;
import com.example.oneminute.R;
import com.example.oneminute.models.Food;
import com.example.oneminute.models.Orders;
import com.example.oneminute.models.Rate;
import com.example.oneminute.models.RestaurantProfile;
import com.example.oneminute.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.Calendar;
import java.util.Date;

public class FoodSingleItem extends AppCompatActivity  implements OrderBottomFragment.MyCallback {
    TextView tv_name,tv_description,tv_ingredients,card_tv_price,card_tv_time,card_tv_type,tv_rate,getCard_tv_res,tv_description2;
    ImageView profile;

    CardView cardView,card_res;

    Button btn_order;


    //for transaction method

    DocumentSnapshot resDocReference;
    DocumentSnapshot userDocReference;





    public  static  float rate=0.0f;
    static  int returningState=0;



    Food food;
    Users users ;


    String restaurantName;

    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("RestaurantOwners");
    private CollectionReference collectionReferenceRate=db.collection("Rate_food");
    private CollectionReference collectionReferenceFood=db.collection("Foods");
    private CollectionReference collectionReferenceUsers =db.collection("Users");
    private CollectionReference collectionReferenceHistory =db.collection("History");
    private CollectionReference collectionReferenceOrder =db.collection("OrdersN");

    Double globalrate=0.0;

    RestaurantProfile restaurantProfile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_single_item);
        tv_name=findViewById(R.id.text_name_single_food);

        tv_description2=findViewById(R.id.text_description_single_food2);
        tv_description=findViewById(R.id.text_description_single_food);
        tv_ingredients=findViewById(R.id.text_ingredients_single_food);
        card_tv_price=findViewById(R.id.text_card_price_single_food);
        card_tv_time=findViewById(R.id.text_card_time_single_food);
        card_tv_type=findViewById(R.id.tv_type_single_food);
        cardView=findViewById(R.id.cardview_rate_single_food);
        tv_rate=findViewById(R.id.text_card_rate_single_food);
        getCard_tv_res=findViewById(R.id.text_card_restaurant_single_food);
        profile=findViewById(R.id.image_single_food);
        card_res=findViewById(R.id.card_res_food_single);
        btn_order=findViewById(R.id.button_order);


        Intent intent=getIntent();


        int from_where=intent.getIntExtra("from",2);



        food = (Food) intent.getSerializableExtra("ser");
        users =Users.getInstance();
        getCurrentRate();

        getRestaurantDAta();
        if(from_where==1){

        } else if (from_where==0) {

            card_res.setVisibility(View.GONE);

        }

        Glide.with(this).load(food.getFoodUrl()).into(profile);
        tv_ingredients.setText(food.getIngredients());
        if (food.getDescription()==null || food.getDescription().isEmpty()){
            tv_description.setVisibility(View.GONE);
            tv_description2.setVisibility(View.GONE);
        }else {
            tv_description.setText(food.getDescription());

        }

        tv_name.setText(food.getName());
        card_tv_type.setText(food.getType());
        card_tv_time.setText(food.getTime()+" min  ");
        card_tv_price.setText(food.getPrice().toString()+" $  ");


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodBottomSheet dialog=new FoodBottomSheet();
                dialog.show(getSupportFragmentManager(),"aaa");








            }
        });


        card_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte =new Intent(FoodSingleItem.this,RestaurantSingleProfile.class);
                inte.putExtra("res",restaurantProfile);
                startActivity(inte);
            }
        });

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderBottomFragment dialog2=new OrderBottomFragment(food.getPrice(),FoodSingleItem.this);
                dialog2.show(getSupportFragmentManager(),"aab");

            }
        });




    }



    void getCurrentRate(){
        final Double[] end_rate1 = {0.0};
        collectionReferenceRate.whereEqualTo("food_id",food.getRestaurantID()).whereEqualTo("name",food.getName()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    Double whole_rate=0.0;
                    Double count=0.0;
                    for (QueryDocumentSnapshot snapshot : value){

                        Rate ratesnap =snapshot.toObject(Rate.class);
                        whole_rate=whole_rate+ratesnap.getRate();
                        count=count+1;



                    }
                    Double end_rate=whole_rate/count;
                    end_rate1[0] =end_rate;
                    globalrate=end_rate;
                    Toast.makeText(FoodSingleItem.this, globalrate.toString(), Toast.LENGTH_SHORT).show();

                    tv_rate.setText(String.format("%.1f",end_rate1[0]).toString());

                }else {
                    Toast.makeText(FoodSingleItem.this, "no rate", Toast.LENGTH_SHORT).show();
                }





            }
        });


    }




    public  void uploadRate(){

        cardView.setClickable(false);

        Rate data=new Rate();
        data.setRate( Double.valueOf(rate));
        data.setFood_id(food.getRestaurantID());

        data.setName(food.getName());
        data.setUser_id(users.getUid());

        collectionReferenceRate.whereEqualTo("user_id",users.getUid()).whereEqualTo("name",food.getName()).whereEqualTo("food_id",food.getRestaurantID())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()) {

                            DocumentSnapshot documentSnapshot = value.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();


                            // Update the rate field using the document reference
                            collectionReferenceRate.document(documentId)
                                    .update("rate", Double.valueOf(rate)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(FoodSingleItem.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        } else {
                            collectionReferenceRate.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(FoodSingleItem.this, "Done", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
        collectionReferenceFood.whereEqualTo("restaurantID",food.getRestaurantID()).whereEqualTo("name",food.getName()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){

                    DocumentSnapshot documentSnapshot = value.getDocuments().get(0);
                    String documentId = documentSnapshot.getId();





                    // Update the rate field using the document reference
                    db.collection("Foods").document(documentId)
                            .update("rate",Double.valueOf(globalrate)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(FoodSingleItem.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });



                }

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cardView.setClickable(true);
            }
        },3000);





    }


    void getRestaurantDAta(){

        collectionReference.whereEqualTo("parent_user_id",food.getRestaurantID()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){


                    for (QueryDocumentSnapshot snapshot : value){

                        RestaurantProfile restaurant =snapshot.toObject(RestaurantProfile.class);

                        restaurantProfile=restaurant;

                        getCard_tv_res.setText(restaurant.getName()+"  ");
                        restaurantName=restaurantProfile.getName();



                    }

                }
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    public void onSuccess(Double price ,int quantity,String notes) {
        Double pricePoints=price*100;

        //to know if the restaurant is closed or not
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        String resTime=restaurantProfile.getTime_from_int();
        String timeFrom[]=resTime.split(":");
        String hourFromS=timeFrom[0];
        String minuteFromS=timeFrom[1];
        String timeTo[]=resTime.split(":");
        String hourToS=timeTo[0];
        String minuteToS=timeTo[1];
        int hourFrom= Integer.parseInt(hourFromS);
        int minutesFrom=Integer.parseInt(minuteFromS);
        int hourTo=Integer.parseInt(hourToS);
        int minuteTo=Integer.parseInt(minuteToS);

        if (hourFrom > hourTo){
        if (hourFrom< hour  && hour< hourTo){//12 4       14    14>12     14< 4
            PointsTransaction(pricePoints,quantity,notes);

        } else if (hourFrom==hour ) {
            if (minutesFrom<minute){
                PointsTransaction(pricePoints,quantity,notes);

            }
        }  else {
            Toast.makeText(this, "Restaurant currently closed", Toast.LENGTH_SHORT).show();
        }

        }else {

            if (hour>hourFrom || hour<hourTo){
                PointsTransaction(pricePoints,quantity,notes);
            } else if (hour==hourFrom) {
                if (minutesFrom<minute){
                    PointsTransaction(pricePoints,quantity,notes);

                }else {
                    Toast.makeText(this, "Restaurant currently closed", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Restaurant currently closed", Toast.LENGTH_SHORT).show();
            }

            // 13   4     15
            /*if (startHour < endHour) {
                // Same day range
                isInTimeRange = currentHour >= startHour && currentHour < endHour;
            } else {
                // Overnight range
                isInTimeRange = currentHour >= startHour || currentHour < endHour;
            }*/


        }

    }

    private void PointsTransaction( Double pricee,int quantity,String notes) {





        //String[] value2 = second.split("%2F%3F");

        String from=restaurantProfile.getWorkTime_from().trim();
        String to=restaurantProfile.getWorkTime_to().trim();





        String receiverID=food.getRestaurantID();
        String senderID=users.getUid();
        final Double[] respoints = new Double[1];
        final Double[] userPoints = new Double[1];


        collectionReferenceUsers.whereEqualTo("uid",receiverID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value!=null){
                    resDocReference = value.getDocuments().get(0);
                    for(QueryDocumentSnapshot snapshot : value){
                        Users users1=snapshot.toObject(Users.class);
                        respoints[0] =users1.getPoints();

                    }
                }
            }
        });
        collectionReferenceUsers.whereEqualTo("uid",senderID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value!=null){
                    userDocReference = value.getDocuments().get(0);
                    for(QueryDocumentSnapshot snapshot : value){
                        Users users2=snapshot.toObject(Users.class);
                        userPoints[0] = users2.getPoints();


                    }
                }
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (resDocReference!=null && userDocReference!=null){

                    String restaurantId = resDocReference.getId();
                    String userId=  userDocReference.getId();
                    final DocumentReference userRef = db.collection("Users").document(userId);
                    final DocumentReference restaurantRef = db.collection("Users").document(restaurantId);
                    db.runTransaction((Transaction.Function<Void>) transaction -> {
                        DocumentSnapshot userSnapshot = transaction.get(userRef);
                        DocumentSnapshot restaurantSnapshot = transaction.get(restaurantRef);

                        Double userPointss = userSnapshot.getDouble("points");
                        Double restaurantPoints = restaurantSnapshot.getDouble("points");

                        if (userPointss >= pricee) {
                            // Subtract points from user
                            transaction.update(userRef, "points", userPointss - pricee);

                            // Add points to restaurant
                            transaction.update(restaurantRef, "points", restaurantPoints + pricee);

                            requestOrder(senderID,receiverID,food.getName(),users.getUsername(),notes,quantity,pricee);
                            addToHistory(senderID,"food",food.getName(),pricee,quantity,food.getRestaurantID(),users.getUsername());

                        } else {
                            Toast.makeText(FoodSingleItem.this, "not enough", Toast.LENGTH_SHORT).show();
                            throw new FirebaseFirestoreException("Not enough points", FirebaseFirestoreException.Code.ABORTED);
                        }

                        return null;
                    }).addOnSuccessListener(aVoid -> {

                        // Transaction success
                        Log.d("FirestoreTransaction", "Transaction success!");
                        Toast.makeText(FoodSingleItem.this, "Success", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        // Transaction failur
                        Toast.makeText(FoodSingleItem.this, "Failed", Toast.LENGTH_SHORT).show();
                        Log.w("FirestoreTransaction", "Transaction failure.", e);
                    });
                }else {
                    Toast.makeText(FoodSingleItem.this, "Poor connection Try again later", Toast.LENGTH_SHORT).show();
                }


            }
        },200);
    }

    private void requestOrder(String userID, String resId, String foodName, String userName, String notes,int quantity,Double price) {


        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds=calendar.get(Calendar.SECOND);
        Orders order=new Orders();
        order.setFoodName(foodName);
        long state=0;
        order.setState(state);
        order.setResId(resId);
        order.setUserId(userID);
        order.setUsername(userName);
        order.setSpecialNotes(notes);
        order.setResReply(null);
        order.setTime(Timestamp.now());
        order.setTimeForDelete(hour+""+minute+""+seconds);
        order.setNumberOfItems(quantity);
        order.setImageUrl(food.getFoodUrl());
        order.setPrice(price);
        order.setPhoneNum(users.getPhoneNum());

        collectionReferenceOrder.add(order).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(FoodSingleItem.this, "Order set Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodSingleItem.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addToHistory(String userID, String type, String name, Double pricee,int quantity,String resId,String userName) {

        History history=new History();
        history.setType(type);
        history.setId(userID);
        history.setName(name);
        history.setValue(pricee);
        Timestamp timestamp =Timestamp.now();
        history.setDate(timestamp);
        history.setState(true);
        history.setQuantity(String.valueOf(quantity));
        history.setParentName(restaurantName);
        history.setCostumerName(userName);
        //new Timestamp(new Date())
        collectionReferenceHistory.add(history).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(FoodSingleItem.this, "H success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodSingleItem.this, "H failure", Toast.LENGTH_SHORT).show();

            }
        });




        History history2=new History();
        history2.setType(type);
        history2.setId(resId);
        history2.setName(name);
        history2.setValue(pricee);
        Timestamp timestamp2 =Timestamp.now();
        history2.setDate(timestamp2);
        history2.setState(false);
        history2.setQuantity(String.valueOf(quantity));
        history2.setParentName(users.getUsername());
        history.setCostumerName(userName);

        collectionReferenceHistory.add(history2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(FoodSingleItem.this, "H2 success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodSingleItem.this, "H2 failure", Toast.LENGTH_SHORT).show();

            }
        });





    }


}