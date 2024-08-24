package com.example.oneminute.addPoints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.oneminute.R;
import com.example.oneminute.models.RequestPoints;
import com.example.oneminute.models.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddPointsActivity extends AppCompatActivity {
    EditText edt_amt,edt_code;
    Button btn_go;
    ImageView iv_btn;
    ProgressBar progressBar;
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("PointsRequest");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_points);
        edt_amt=findViewById(R.id.AddPointsActivity_edittext_amount);
        edt_code=findViewById(R.id.AddPointsActivity_edittext_code);
        btn_go=findViewById(R.id.AddPointsActivity_button_go);
        iv_btn=findViewById(R.id.AddPointsActivity_iv_Btn);
        progressBar=findViewById(R.id.AddPointsActivity_progressBar2);
        progressBar.setVisibility(View.GONE);


        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(edt_amt.getText().toString()) && !TextUtils.isEmpty(edt_code.getText().toString())){
                    String code=edt_code.getText().toString();
                    long amount=Long.valueOf(edt_amt.getText().toString());

                    progressBar.setVisibility(View.VISIBLE);
                    request(code,amount);

                }else {
                    Toast.makeText(AddPointsActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });


        iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddPointsActivity.this,ShowRequests.class));
            }
        });

    }

    private void request(String code, long amount) {

        RequestPoints requestPoints=new RequestPoints();
        requestPoints.setAmount(amount);
        requestPoints.setState(1);
        requestPoints.setCode(code);
        requestPoints.setTimestamp(Timestamp.now());
        requestPoints.setUserId(Users.getInstance().getUid());
        requestPoints.setUserName(Users.getInstance().getUsername());
        requestPoints.setErrorCode(null);
        requestPoints.setOperationId(UUID.randomUUID().toString());
        requestPoints.setRecyclerState(true);

        collectionReference.add(requestPoints).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddPointsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPointsActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }
}