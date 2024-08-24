package com.example.oneminute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oneminute.models.Users;
import com.example.oneminute.restaurants.viewmodel.PointsViewModel;
import com.example.oneminute.signing.ForgotPassword;
import com.example.oneminute.signing.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    MaterialButton sign_in_btn,sign_up_btn,forgot_btn;
    EditText name_edt,password_edt;

    //firebase
    private FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();
    private  FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firebase connection
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference =db.collection("Users");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign_in_btn=findViewById(R.id.button_signin_signin);
        sign_up_btn=findViewById(R.id.button_signup_signin);
        forgot_btn=findViewById(R.id.button_forgot_signin);
        name_edt=findViewById(R.id.edittext_email_login);
        password_edt=findViewById(R.id.edittext_password_login);




        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        forgot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);

            }
        });


        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(name_edt.getText().toString()) && !TextUtils.isEmpty(password_edt.getText().toString())){

                    String email =name_edt.getText().toString();
                    String password =password_edt.getText().toString();
                    signInWithEmailAndPass(email,password);



                }




            }
        });






    }

    private void signInWithEmailAndPass(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                currentUser= firebaseAuth.getCurrentUser();


                final String currentUserId =currentUser.getUid();


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


                                //display list of journals after log in
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();

                            }


                        }
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failde", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
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

                            PointsViewModel  viewModel = new ViewModelProvider(MainActivity.this).get(PointsViewModel.class);

                            viewModel.setPoints(users.getPoints());


                            //display list of journals after log in
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();



                        }


                    }
                }
            });







        }
    }
}