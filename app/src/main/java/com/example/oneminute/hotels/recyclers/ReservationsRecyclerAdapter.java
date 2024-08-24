package com.example.oneminute.hotels.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneminute.R;
import com.example.oneminute.models.Reservation;
import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReservationsRecyclerAdapter extends RecyclerView.Adapter<ReservationsRecyclerAdapter.VH> {

    private Context context;
    private ArrayList<Reservation> reservations=new ArrayList<>();

    public ReservationsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
        notifyDataSetChanged();
    }



    class VH extends RecyclerView.ViewHolder{
        TextView tv_hotel,tv_room,tv_start,tv_end;



        public VH(@NonNull View itemView) {
            super(itemView);
            tv_hotel=itemView.findViewById(R.id.reservation_item_text_hotel);
            tv_room=itemView.findViewById(R.id.reservation_item_text_room);
            tv_end=itemView.findViewById(R.id.reservation_item_text_ending_date);
            tv_start=itemView.findViewById(R.id.reservation_item_text_starting_date);
        }
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.reservation_recycler_item,parent,false);

        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Reservation reservation=reservations.get(position);
        holder.tv_hotel.setText(reservation.getHotelName());
        holder.tv_room.setText(reservation.getRoomName());
        Timestamp startingDate=reservation.getDate().get(0);
        Timestamp endDate=reservation.getDate().get((reservation.getDate().size())-1);

        holder.tv_start.setText(startingDate.toDate().toString());
        holder.tv_end.setText(endDate.toDate().toString());

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

}
