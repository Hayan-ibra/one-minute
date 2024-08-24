package com.example.oneminute.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.models.Store;
import com.example.oneminute.models.pager.PagerProperties;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class SingleStoreActivity extends AppCompatActivity {

    ImageView iv;
    TextView tv_name,tv_location,tv_number,tv_work,tv_type;

    Button btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_store);
        iv=findViewById(R.id.SingleStoreActivity_imageview);
        tv_name=findViewById(R.id.SingleStoreActivity_text_name);
        tv_location=findViewById(R.id.SingleStoreActivity_text_location);
        tv_number=findViewById(R.id.SingleStoreActivity_text_phone);
        tv_work=findViewById(R.id.SingleStoreActivity_text_work_time);
        tv_type=findViewById(R.id.SingleStoreActivity_text_catigories);
        btn=findViewById(R.id.SingleStoreActivity_button);


        Intent intent=getIntent();
        Store store= (Store) intent.getSerializableExtra("shop");

        Glide.with(SingleStoreActivity.this).load(store.getImageUrl()).into(iv);
        tv_name.setText(store.getStoreName());
        tv_location.setText("Location : "+store.getLocation());
        tv_number.setText("Phone number : "+store.getPhoneNum());
        tv_work.setText("Work time : "+store.getWorkTimeFrom()+" to "+store.getWorkTimeTo());
        List<String> storeType=store.getStoreType();
        String type="";
        for (int i=0;i<storeType.size();i++){
            if (i==storeType.size()-1){
                type=type+storeType.get(i);

            }else {
                type=type+storeType.get(i)+" , ";
            }
        }
        tv_type.setText("Store type : "+type);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 =new Intent(SingleStoreActivity.this, SingleStoreItemList.class);
                intent1.putExtra("1",store.getStoreId());
                intent1.putExtra("2",store.getStoreName());
                intent1.putExtra("3",0);
                startActivity(intent1);
            }
        });




    }
}