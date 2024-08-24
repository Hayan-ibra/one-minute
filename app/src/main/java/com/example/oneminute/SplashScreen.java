package com.example.oneminute;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.oneminute.models.Users;
import com.example.oneminute.restaurants.viewmodel.PointsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashScreen extends AppCompatActivity {
    private FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firebase connection
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference =db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getData();
    }

    private void getData() {
        if(firebaseAuth.getCurrentUser() != null){



            currentUser= firebaseAuth.getCurrentUser();

            final String currentUserId =currentUser.getUid();
            Users users =Users.getInstance();
            users.setUid(currentUserId);





            collectionReference.whereEqualTo("uid",currentUserId).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(!value.isEmpty()){
                        //getting all snapshots
                        for (QueryDocumentSnapshot snapshot : value){

                            Users users =Users.getInstance();
                            users.setUsername(snapshot.getString("username"));
                            users.setUid(snapshot.getString("uid"));
                            users.setUrl(snapshot.getString("url"));
                            users.setPoints(snapshot.getDouble("points"));
                            users.setGender(snapshot.getString("gender"));
                            users.setPhoneNum(snapshot.getString("phoneNum"));
                            users.setAccountType(snapshot.getString("accountType"));
                            users.setEmail(snapshot.getString("email"));

                            PointsViewModel  viewModel = new ViewModelProvider(SplashScreen.this).get(PointsViewModel.class);

                            viewModel.setPoints(users.getPoints());


                            //display list of journals after log in
                            startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                            finish();



                        }


                    }
                }
            });







        }else {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            finish();
        }
    }

}