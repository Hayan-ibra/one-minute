package com.example.oneminute.shopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.models.History;
import com.example.oneminute.models.Store;
import com.example.oneminute.models.StoreItem;
import com.example.oneminute.models.Users;
import com.example.oneminute.restaurants.FoodSingleItem;
import com.example.oneminute.restaurants.OrderBottomFragment;
import com.example.oneminute.shopping.bottomsheet.OrderItemBottomSheetFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.List;

public class SingleItemActivity extends AppCompatActivity implements OrderItemBottomSheetFragment.ItemOrderCallback{
    ImageView iv;

    TextView tv_name,tv_quantity,tv_price,tv_genders,tv_catigories,tv_shopName;
    Button order_btn;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();

    private CollectionReference collectionReferenceStores=db.collection("StoreOwnersN");
    private CollectionReference collectionReferenceUsers=db.collection("Users");
    private CollectionReference collectionReferenceItems=db.collection("StoreItems");
    private CollectionReference collectionReferenceHistory =db.collection("History");

    DocumentReference storeReference;

    DocumentSnapshot snapshotUser;
    DocumentSnapshot snapshotShop;
    DocumentReference referenceUser;
    DocumentReference referenceShop;
    DocumentReference referenceItem;
    Store store;
    LinearLayout linearLayout;
    Users users=Users.getInstance();
    String itemId;
    String itemname;

    String storeID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        iv=findViewById(R.id.SingleItemActivity_imageview);
        tv_name=findViewById(R.id.SingleItemActivity_text_name);
        tv_quantity=findViewById(R.id.SingleItemActivity_quantity);
        tv_catigories=findViewById(R.id.SingleItemActivity_text_catigories);
        tv_genders=findViewById(R.id.SingleItemActivity_text_genders);
        tv_price=findViewById(R.id.SingleItemActivity_text_price);
        order_btn=findViewById(R.id.SingleItemActivity_button_order);
        tv_shopName=findViewById(R.id.SingleItemActivity_text_shopname);
        linearLayout=findViewById(R.id.SingleItemActivity_linear);



        Intent intent =getIntent();
        StoreItem data= (StoreItem) intent.getSerializableExtra("item");
        initiate(data);
        itemId=data.getItemId();
        itemname=data.getItemName();
        storeID=data.getParentId();
        if (data.getQuantity() < 1 ){
            order_btn.setEnabled(false);
        }



        getrestaurant(data.getParentId());



        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderItemBottomSheetFragment dialog2=new OrderItemBottomSheetFragment(data.getPrice(),data.getParentId(), SingleItemActivity.this);
                dialog2.show(getSupportFragmentManager(),"aab");

            }
        });








    }






    private void initiate(StoreItem data) {
        Glide.with(SingleItemActivity.this).load(data.getImageUrl()).into(iv);
        tv_name.setText(data.getItemName());
        tv_price.setText(data.getPrice()+" $");
        tv_quantity.setText(String.valueOf(data.getQuantity()));

        List<String> cats=data.getCatigory();
        String catigories="";
        for (int i =0 ; i<cats.size();i++){
            if (i==cats.size()-1){
                catigories=catigories+cats.get(i);

            }else {
                catigories=catigories+cats.get(i)+" , ";
            }
        }
        tv_catigories.setText(catigories);
        List<String>genders =data.getGender();
        String gender="";
        for (int i =0 ; i<genders.size();i++){
            if (i==genders.size()-1){
                gender=gender + genders.get(i);

            }else {
                gender=gender + genders.get(i)+" , ";
            }
        }
        tv_genders.setText(gender);
    }


    private void getrestaurant(String parentId){
        collectionReferenceStores.whereEqualTo("storeId",parentId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    DocumentSnapshot snapshot1=value.getDocuments().get(0);
                    store=value.getDocuments().get(0).toObject(Store.class);
                    storeReference=snapshot1.getReference();
                    tv_shopName.setText(store.getStoreName());


                }else {
                    Toast.makeText(SingleItemActivity.this, "empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onSuccess(Double pricee,String shopId,long count) {
        Toast.makeText(this, ""+count, Toast.LENGTH_SHORT).show();
        Double endPrice = pricee * 100;
        collectionReferenceUsers.whereEqualTo("uid", users.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    snapshotUser = value.getDocuments().get(0);
                    referenceUser = snapshotUser.getReference();

                }
            }
        });
        collectionReferenceUsers.whereEqualTo("uid", shopId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    snapshotShop = value.getDocuments().get(0);
                    referenceShop = snapshotShop.getReference();

                }
            }
        });
        collectionReferenceItems.whereEqualTo("itemId",itemId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                DocumentSnapshot snapshotItem=value.getDocuments().get(0);
                referenceItem=snapshotItem.getReference();

            }
        });
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                db.runTransaction((Transaction.Function<Void>) transaction -> {
                    // Get the snapshots
                    DocumentSnapshot fromUserSnapshot = transaction.get(referenceUser);
                    DocumentSnapshot toUserSnapshot = transaction.get(referenceShop);
                    DocumentSnapshot itemSnapshot = transaction.get(referenceItem);

                    // Get current points and quantity
                    Long fromUserPoints = fromUserSnapshot.getLong("points");
                    Long toUserPoints = toUserSnapshot.getLong("points");
                    Long currentQuantity = itemSnapshot.getLong("quantity");

                    // Check if the points and quantity are sufficient
                    if (fromUserPoints != null && fromUserPoints >= endPrice &&
                            currentQuantity != null && currentQuantity >= count) {

                        // Update points
                        transaction.update(referenceUser, "points", fromUserPoints - endPrice);
                        if (toUserPoints != null) {
                            transaction.update(referenceShop, "points", toUserPoints + endPrice);
                        } else {
                            transaction.update(referenceShop, "points", endPrice);
                        }

                        // Update item quantity
                        transaction.update(referenceItem, "quantity", currentQuantity - count);

                    } else {
                        Toast.makeText(SingleItemActivity.this, "Insufficient points or quantity", Toast.LENGTH_SHORT).show();
                        throw new FirebaseFirestoreException("Insufficient points or quantity", FirebaseFirestoreException.Code.ABORTED);
                    }

                    return null;
                }).addOnSuccessListener(aVoid -> {
                    // Transaction success
                    System.out.println("Points transferred and item ordered successfully!");
                    Toast.makeText(SingleItemActivity.this, "Points transferred and item ordered successfully!", Toast.LENGTH_SHORT).show();
                    addToHistory(users.getUid(),"shopping",itemname,endPrice,(int) count,storeID,users.getUsername());
                    //addToHistory(senderID,"food",food.getName(),pricee,quantity,food.getRestaurantID(),users.getUsername());
                }).addOnFailureListener(e -> {
                    // Transaction failure
                    System.err.println("Failed to complete transaction: " + e.getMessage());
                    Toast.makeText(SingleItemActivity.this, "Failed to complete transaction:", Toast.LENGTH_SHORT).show();
                });

            }
        });


        thread.start();






    }

    private void addToHistory(String userID, String type, String name, Double pricee,int quantity,String resId,String username) {
        History history=new History();
        history.setType(type);
        history.setId(userID);
        history.setName(name);
        history.setValue(pricee);
        Timestamp timestamp =Timestamp.now();
        history.setDate(timestamp);
        history.setState(true);
        history.setQuantity(String.valueOf(quantity));
        history.setParentName(store.getStoreName());
        history.setCostumerName(username);
        //new Timestamp(new Date())
        collectionReferenceHistory.add(history).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(SingleItemActivity.this, "H success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingleItemActivity.this, "H failure", Toast.LENGTH_SHORT).show();

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
        history2.setCostumerName(username);

        collectionReferenceHistory.add(history2).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(SingleItemActivity.this, "H2 success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingleItemActivity.this, "H2 failure", Toast.LENGTH_SHORT).show();

            }
        });



    }

    /*
    public void orderItem(String itemId, int quantityToOrder) {
    DocumentReference itemRef = db.collection("items").document(itemId);

    db.runTransaction((Transaction.Function<Void>) transaction -> {
        DocumentSnapshot itemSnapshot = transaction.get(itemRef);

        Long currentQuantity = itemSnapshot.getLong("quantity");

        if (currentQuantity != null && currentQuantity >= quantityToOrder) {
            transaction.update(itemRef, "quantity", currentQuantity - quantityToOrder);
        } else {
            throw new FirebaseFirestoreException("Insufficient quantity", FirebaseFirestoreException.Code.ABORTED);
        }

        return null;
    }).addOnSuccessListener(aVoid -> {
        // Transaction success
        System.out.println("Item ordered successfully!");
    }).addOnFailureListener(e -> {
        // Transaction failure
        System.err.println("Failed to order item: " + e.getMessage());
    });
}
     */

    /*
    public void orderItem() {
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(itemRef);
            Long availableItems = snapshot.getLong("availableItems");

            if (availableItems != null && availableItems > 0) {
                // Decrease availableItems by 1
                transaction.update(itemRef, "availableItems", availableItems - 1);
            } else {
                throw new FirebaseFirestoreException("No items available", FirebaseFirestoreException.Code.ABORTED);
            }

            return null;
        }).addOnSuccessListener(aVoid -> {
            // Transaction successful
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Transaction failed
            Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
    */

    /*
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
                        addToHistory(senderID,"food",food.getName(),pricee,quantity,food.getRestaurantID());

                    } else {
                        Toast.makeText(FoodSingleItem.this, "not enough", Toast.LENGTH_SHORT).show();
                        throw new FirebaseFirestoreException("Not enough points", FirebaseFirestoreException.Code.ABORTED);
                    }
     */
}