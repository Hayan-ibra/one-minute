package com.example.oneminute.shopping.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.oneminute.R;
import com.example.oneminute.restaurants.OrderBottomFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OrderItemBottomSheetFragment extends BottomSheetDialogFragment{


        private ItemOrderCallback callback;
        Double submit_price=0.0;
        String parentId;

        Double price;

        public OrderItemBottomSheetFragment(Double price,String parentId, ItemOrderCallback callback) {
            this.price=price;
            this.callback=callback;
            this.parentId=parentId;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.order_item_bottom_sheet, container, false);
            // Inflate the layout for this fragment
            CardView submit=view.findViewById(R.id.order_item_bottom_card_button);
            // android:id="@+id/bottom_order_button_done"
            ImageView iv_plus=view.findViewById(R.id.order_item_bottom_plus);
            ImageView iv_minus=view.findViewById(R.id.order_item_bottom_minus);
            TextView tv_count=view.findViewById(R.id.order_item_bottom_number);
            TextView tv_result=view.findViewById(R.id.order_item_bottom_final_price);


            final int[] count = {1};
            final Double[] price_end = {price};
            submit_price=price;

            tv_result.setText(String.format("%.2f",price_end[0])+" $");

            iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(count[0] >1){
                        count[0] = count[0] -1;
                        price_end[0] =count[0]*price;


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

                    String result=String.format("%.2f",price_end[0]);
                    tv_result.setText(result+" $");
                    submit_price=price_end[0];
                }
            });


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onSuccess(submit_price,parentId,count[0]);

                    dismiss();

                }
            });




            return view;
        }


        public interface ItemOrderCallback {
            void onSuccess(Double pricee,String parentId,long count);

        }


    }


