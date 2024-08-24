package com.example.oneminute.signing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oneminute.MainActivity;
import com.example.oneminute.R;
import com.example.oneminute.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUp extends AppCompatActivity {
    Button signup_button;
    ImageView profile_img;

    EditText email_edt,password_edt,username_edt,phone_edt;

    MaterialRadioButton maleRadioButton;
    MaterialRadioButton femaleRadioButton;



    //firebase authentication

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //firebase firestore

    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");

    //firebase storage for image storage
    private StorageReference storageReference;
    private Uri imageUri;



    //key for result
    int GALLERY_KEY =10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Ui Elements
        signup_button=findViewById(R.id.button_signup_signup);
        profile_img=findViewById(R.id.imageView_profile_signup);
        email_edt=findViewById(R.id.edittext_email_signup);
        phone_edt=findViewById(R.id.edittext_phonenum_signup);
        password_edt=findViewById(R.id.edittext_password_signup);
        username_edt=findViewById(R.id.edittext_username_signup);
        maleRadioButton=findViewById(R.id.radiobutton_male_signup);
        femaleRadioButton=findViewById(R.id.radiobutton_female_signup);

        //Firebase

        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        //Authentication
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser=firebaseAuth.getCurrentUser();
                if(currentUser !=null){
                    //User Already Logged In



                }else{
                    //not user yet




                }


            }
        };





        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(!TextUtils.isEmpty(email_edt.getText().toString()) && !TextUtils.isEmpty(password_edt.getText().toString())
                        && !TextUtils.isEmpty(username_edt.getText().toString()) &&!TextUtils.isEmpty(phone_edt.getText().toString())){
                    String username=username_edt.getText().toString();

                    String email=email_edt.getText().toString();
                    String password=password_edt.getText().toString();
                    String phone=phone_edt.getText().toString();
                    String gender="0";

                    if(maleRadioButton.isChecked()){
                        gender="male";
                    } else if (femaleRadioButton.isChecked()) {
                        gender="female";
                    }
                    signUpToFireBase(email,password,username,phone,gender);


                }else {
                    Toast.makeText(SignUp.this, "please fill all the fields", Toast.LENGTH_SHORT).show();
                }




            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent =new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_KEY);
            }
        });




    }



    private void signUpToFireBase(String email, String password, String username, String phone,String gender) {
        if(maleRadioButton.isChecked() || femaleRadioButton.isChecked()){





            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if(task.isSuccessful()){



                        //we take the user to the next activity
                        currentUser=firebaseAuth.getCurrentUser();
                        assert currentUser !=null;
                        final String currentUserId = currentUser.getUid();
                        double points =0;
                        String accountType="default";
                        if(imageUri != null){

                            final  StorageReference  filepaht =storageReference.child("profile_images")
                                    .child("my_image"+ Timestamp.now().getSeconds());

                            //uploading the  image
                            filepaht.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    filepaht.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String image_urll=uri.toString();

                                            Users users=new Users();
                                            users.setAccountType(accountType);
                                            users.setGender(gender);
                                            users.setPoints(points);
                                            users.setPhoneNum(phone);
                                            users.setUsername(username);
                                            users.setUid(currentUserId);
                                            users.setUrl(image_urll);
                                            users.setEmail(email);

                                            collectionReference.add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Users users11=Users.getInstance();
                                                    users11.setAccountType(accountType);
                                                    users11.setGender(gender);
                                                    users11.setPoints(points);
                                                    users11.setPhoneNum(phone);
                                                    users11.setUsername(username);
                                                    users11.setUid(currentUserId);
                                                    users11.setEmail(email);
                                                    Intent intent =new Intent(SignUp.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SignUp.this, "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this, "Could not upload the image", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {


                            String image_url=null;

                            Users users=new Users();
                            users.setAccountType(accountType);
                            users.setGender(gender);
                            users.setPoints(points);
                            users.setPhoneNum(phone);
                            users.setUsername(username);
                            users.setUid(currentUserId);
                            users.setUrl(image_url);
                            users.setEmail(email);

                            collectionReference.add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Users users11=Users.getInstance();
                                    users11.setAccountType(accountType);
                                    users11.setGender(gender);
                                    users11.setPoints(points);
                                    users11.setPhoneNum(phone);
                                    users11.setUsername(username);
                                    users11.setUid(currentUserId);
                                    users.setEmail(email);
                                    Intent intent =new Intent(SignUp.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUp.this, "Wrong", Toast.LENGTH_SHORT).show();
                                }
                            });





                        }





                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });






        }else {
            Toast.makeText(this, "please check your gender", Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==GALLERY_KEY && resultCode==RESULT_OK){
            imageUri=data.getData();
            profile_img.setImageURI(imageUri);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser =firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }
}