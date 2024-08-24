package com.example.oneminute.hotels.recyclers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.databinding.RoomSingleItemBinding;
import com.example.oneminute.hotels.SingleRoomActivity;
import com.example.oneminute.models.HotelRoom;
import com.example.oneminute.models.Hotels;

import java.util.ArrayList;

public class RoomsRecyclerAdapter extends RecyclerView.Adapter<RoomsRecyclerAdapter.VH> {
    private Context context;
    private Hotels hotels;
    private ArrayList<HotelRoom> rooms=new ArrayList<>();


    public RoomsRecyclerAdapter(Context context, Hotels hotels) {
        this.context = context;
        this.hotels = hotels;
    }

    public void setRooms(ArrayList<HotelRoom> rooms) {
        this.rooms = rooms;
        notifyDataSetChanged();
    }


    public class VH extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;


        public VH(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.single_room_text_title);
            imageView=itemView.findViewById(R.id.single_room_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HotelRoom room=rooms.get(getAdapterPosition());
                    Intent intent=new Intent(context, SingleRoomActivity.class);
                    intent.putExtra("room",room);
                    intent.putExtra("hotel",hotels);
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.room_single_item,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        HotelRoom room=rooms.get(position);
        holder.textView.setText("Room : "+room.getRoomNum());
        Glide.with(context).load(room.getImageUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }


}
