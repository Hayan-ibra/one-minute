package com.example.oneminute.hotels.bootomsheets;

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
import com.example.oneminute.hotels.SingleHotelActivity;
import com.example.oneminute.restaurants.FoodSingleItem;
import com.example.oneminute.shopping.bottomsheet.OrderItemBottomSheetFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class HotelRateBottomSheet extends BottomSheetDialogFragment {

    private ItemOrderCallback callback;

    Double currentRate;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public interface ItemOrderCallback {
        void onSuccess(Double submittedRate);

    }

    public HotelRateBottomSheet(ItemOrderCallback callback) {
        this.callback=callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.foods_bottom_sheet, container, false);
        // Inflate the layout for this fragment

        RatingBar ratingBar = view.findViewById(R.id.fodd_sheet_rating_bar);
        Button button = view.findViewById(R.id.button_rate_submit);
        ratingBar.setNumStars(5);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {         //the value that you changed  its float for more choices
                currentRate = Double.valueOf(v);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof SingleHotelActivity) {

                    if (currentRate != null) {
                        callback.onSuccess(currentRate);
                    } else {

                    }
                }
                dismiss();
            }
        });
        return view;
    }


}




