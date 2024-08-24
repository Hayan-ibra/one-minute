package com.example.oneminute.hotels.recyclers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.hotels.SingleHotelActivity;
import com.example.oneminute.models.Hotels;

import java.util.ArrayList;

public class HotelsRecyclerAdapter extends RecyclerView.Adapter<HotelsRecyclerAdapter.VH> {

    private Context context;
    private ArrayList<Hotels> hotels=new ArrayList<>();

    public HotelsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setHotels(ArrayList<Hotels> hotels) {
        this.hotels = hotels;
        notifyDataSetChanged();
    }



    class VH extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv_name,tv_location,tv_rate,tv_phone;
        Button button;

        public VH(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.hotel_item_imge);
            tv_rate=itemView.findViewById(R.id.hotel_item_text_rate);
            tv_name=itemView.findViewById(R.id.hotel_item_text_title);
            tv_phone=itemView.findViewById(R.id.hotel_item_text_phone);
            tv_location=itemView.findViewById(R.id.hotel_item_text_location);
            button=itemView.findViewById(R.id.hotel_item_button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Hotels hotelToSend = hotels.get(getAdapterPosition());
                    Intent intent=new Intent(context, SingleHotelActivity.class);
                    intent.putExtra("hotel",hotelToSend);
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.hotels_fragment_hotel_items,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Hotels hotel =hotels.get(position);
        Glide.with(context).load(hotel.getImageUrl()).into(holder.iv);
        holder.tv_name.setText(hotel.getHotelName());
        holder.tv_location.setText(" "+hotel.getLocation());
        holder.tv_phone.setText(" "+hotel.getPhoneNumber());
        holder.tv_rate.setText(String.format("%.1f",Math.ceil(hotel.getRate() * 100) / 100.0).toString());

    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }
}
