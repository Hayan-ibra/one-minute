package com.example.oneminute.restaurants;

import android.app.Dialog;
import android.media.Rating;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oneminute.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FoodBottomSheet extends BottomSheetDialogFragment {

    BottomSheetBehavior behavior;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
/*
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        BottomSheetDialog dialog=(BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View v =View.inflate(getContext(), R.layout.foods_bottom_sheet,null);
        dialog.setContentView(v);
        behavior=BottomSheetBehavior.from((View) v.getParent());
        return dialog;
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.foods_bottom_sheet, container, false);
        // Inflate the layout for this fragment
        final float[] current_rate = {0.0f};
        RatingBar ratingBar=view.findViewById(R.id.fodd_sheet_rating_bar);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {         //the value that you changed  its float for more choices


                current_rate[0] =v;


            }
        });


        Button button=view.findViewById(R.id.button_rate_submit);
        
        
        
        ratingBar.setNumStars(5);
        
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FoodSingleItem.rate=current_rate[0];
                FoodSingleItem.returningState=1;







                if (getActivity() instanceof FoodSingleItem) {

                    ((FoodSingleItem) getActivity()).uploadRate();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {



                        }
                    },3000);

                }

                dismiss();


            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //to use it :
    //   FoodBottomSheet dialog=new FoodBottomSheet();
    //                dialog.show(getSupportFragmentManager(),"aaa");
    //
}
