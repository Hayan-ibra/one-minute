package com.example.oneminute.hotels.bootomsheets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oneminute.R;
import com.example.oneminute.hotels.SingleHotelActivity;
import com.example.oneminute.models.Reservation;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReserveBottomSheet extends BottomSheetDialogFragment {

    private ItemReserveCallback callback;
    private openDateFragment dialogCallback;

    TextView text_start_date;
    Button done;
    TextView textView_here;

    int dayCount=1;

    ArrayList<Timestamp> timestamps=new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public interface ItemReserveCallback {
        void onSuccess(ArrayList<Timestamp> timestamp,int count);

    }
    public interface openDateFragment {
        void openDialog(Calendar calendar);

    }

    public ReserveBottomSheet(ItemReserveCallback callback,openDateFragment dialogCallback) {
        this.callback=callback;
        this.dialogCallback=dialogCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reserve_hotel_room_bottom_sheet, container, false);
        // Inflate the layout for this fragment

        ImageView plus=view.findViewById(R.id.reserve_bottom_sheet_button_plus) ;
        ImageView minus=view.findViewById(R.id.reserve_bottom_sheet_button_minus) ;
        TextView textViewNumber=view.findViewById(R.id.reserve_bottom_number);
        text_start_date=view.findViewById(R.id.reserve_bottom_sheet_text_setstarting);
        textView_here=view.findViewById(R.id.reserve_bottom_sheet_text_here);
        done=view.findViewById(R.id.reserve_bottom_sheet_done);
        done.setEnabled(false);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dayCount=dayCount+1;
                textViewNumber.setText(String.valueOf(dayCount));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dayCount>1){
                    dayCount=dayCount-1;
                    textViewNumber.setText(String.valueOf(dayCount));
                }

            }
        });

        textView_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callback.onSuccess(timestamps,dayCount);






                dismiss();
            }
        });




        return view;
    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setOnDateSetListener(dateSetListener);
        newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
        timestamps.clear();
    }
    private final DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {



        timestamps.clear();
        //dialogCallback.openDialog(calendar);
        int newDay=1;
        int newYear=new Date().getYear()+1;
        int newMonth=1;
        for (int i = 0; i<dayCount; i++){
            Calendar calendar = Calendar.getInstance();
            if (dayOfMonth+i>31 && (month==0 || month==2 || month==4   || month==6 ||month==7 || month ==9 || month==11)) {
                if (dayOfMonth+i>31 && month==1){
                    calendar.set(newYear, newMonth, newDay);
                    Date selectedDate = calendar.getTime();
                    // Convert to Firestore Timestamp
                    Timestamp timestamp = new Timestamp(selectedDate);
                    timestamps.add(timestamp);
                    newDay++;

                }else {
                    //Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month+1, newDay);
                    Date selectedDate = calendar.getTime();
                    // Convert to Firestore Timestamp
                    Timestamp timestamp = new Timestamp(selectedDate);
                    timestamps.add(timestamp);
                    newDay++;
                }


            } else if (month+i>30 && (month==3 || month==5 || month==8   || month==10 )) {
               // Calendar calendar = Calendar.getInstance();
                calendar.set(year, month+1, newDay);
                Date selectedDate = calendar.getTime();
                // Convert to Firestore Timestamp
                Timestamp timestamp = new Timestamp(selectedDate);
                timestamps.add(timestamp);
                newDay++;
            } else if (dayOfMonth>28 &&(month== 1 ) ) {
                //Calendar calendar = Calendar.getInstance();
                calendar.set(year, month+1, newDay);
                Date selectedDate = calendar.getTime();
                // Convert to Firestore Timestamp
                Timestamp timestamp = new Timestamp(selectedDate);
                timestamps.add(timestamp);
                newDay++;
            }else {
                //Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth+i);
                Date selectedDate = calendar.getTime();
                // Convert to Firestore Timestamp
                Timestamp timestamp = new Timestamp(selectedDate);
                timestamps.add(timestamp);
            }


           // Date selectedDate = calendar.getTime();
            // Convert to Firestore Timestamp
           // Timestamp timestamp = new Timestamp(selectedDate);
           // timestamps.add(timestamp);

        }
        newDay=1;





        text_start_date.setText("Start Date : ");
        String monthStr=turnNumberToMonth(month);
        done.setEnabled(true);
        textView_here.setText(dayOfMonth+" "+monthStr);






    };

    String  turnNumberToMonth(int num){
        switch (num){
            case 0:
                return "January";
            case 1:
                return "February";

            case 2:
                return "March";

            case 3:
                return "April";

            case 4:
                return "May";

            case 5:
                return "June";

            case 6:
                return "July";

            case 7:
                return "August";

            case 8:
                return "September";

            case 9:
                return "October";

            case 10:
                return "November";

            case 11:
                return "December";

            default:
                return "Wrong Format";


        }


    }
}
