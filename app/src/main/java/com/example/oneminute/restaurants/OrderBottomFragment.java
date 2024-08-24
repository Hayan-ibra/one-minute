package com.example.oneminute.restaurants;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.oneminute.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OrderBottomFragment  extends BottomSheetDialogFragment {


    private MyCallback callback;
    String notes=null;


    Double submit_price=0.0;
    int times;
    Double price;

    public OrderBottomFragment(Double price,MyCallback callback) {
        this.price=price;
        this.callback=callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_order, container, false);
        // Inflate the layout for this fragment
        CardView submit=view.findViewById(R.id.bottom_order_button_done);
       // android:id="@+id/bottom_order_button_done"
        ImageView iv_plus=view.findViewById(R.id.bottom_order_pluss_button_iv);
        ImageView iv_minus=view.findViewById(R.id.bottom_order_minus_button_iv);
        TextView tv_count=view.findViewById(R.id.text_bottom_order_number);
        TextView tv_result=view.findViewById(R.id.bottom_order_text_result);
        EditText edt_notes=view.findViewById(R.id.bottom_order_edittext_notes);

        final int[] count = {1};
        final Double[] price_end = {price};
        submit_price=price;
        times=count[0];
        tv_result.setText(String.format("%.2f",price_end[0])+" $");

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count[0] >1){
                    count[0] = count[0] -1;
                    price_end[0] =count[0]*price;
                    times=count[0];

                    tv_count.setText(count[0]+"");

                    String result=String.format("%.2f",price_end[0]);
                    tv_result.setText(result+" $");
                    submit_price=price_end[0];
                }else {
                    Toast.makeText(getActivity(), "can't go under 1", Toast.LENGTH_SHORT).show();
                }
            }
        });


        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count[0] = count[0] +1;
                tv_count.setText(count[0]+"");
                price_end[0] =count[0]*price;
                times=count[0];
                String result=String.format("%.2f",price_end[0]);
                tv_result.setText(result+" $");
                submit_price=price_end[0];
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes=edt_notes.getText().toString();
                callback.onSuccess(submit_price,times,notes);

                dismiss();

            }
        });




        return view;
    }


    public interface MyCallback {
        void onSuccess(Double pricee,int times,String notes);

    }


}
