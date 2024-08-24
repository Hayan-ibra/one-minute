package com.example.oneminute.restaurants;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.Transaction.Function;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import android.util.Log;
import androidx.annotation.NonNull;
public class RestaurantsPointsTransaction {
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

        public void transferPoints(String userId, String restaurantId, int pointsToTransfer) {
            final DocumentReference userRef = db.collection("users").document(userId);
            final DocumentReference restaurantRef = db.collection("restaurants").document(restaurantId);


            db.runTransaction((Function<Void>) transaction -> {
                DocumentSnapshot userSnapshot = transaction.get(userRef);
                DocumentSnapshot restaurantSnapshot = transaction.get(restaurantRef);

                long userPoints = userSnapshot.getLong("points");
                long restaurantPoints = restaurantSnapshot.getLong("points");

                if (userPoints >= pointsToTransfer) {
                    // Subtract points from user
                    transaction.update(userRef, "points", userPoints - pointsToTransfer);

                    // Add points to restaurant
                    transaction.update(restaurantRef, "points", restaurantPoints + pointsToTransfer);
                } else {
                    throw new FirebaseFirestoreException("Not enough points", FirebaseFirestoreException.Code.ABORTED);
                }

                return null;
            }).addOnSuccessListener(aVoid -> {
                // Transaction success
                Log.d("FirestoreTransaction", "Transaction success!");
            }).addOnFailureListener(e -> {
                // Transaction failure
                Log.w("FirestoreTransaction", "Transaction failure.", e);
            });
        }
    }


