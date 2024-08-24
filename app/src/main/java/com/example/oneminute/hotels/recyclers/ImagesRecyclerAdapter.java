package com.example.oneminute.hotels.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.oneminute.R;
import com.example.oneminute.models.RoomImages;

import java.util.ArrayList;

public class ImagesRecyclerAdapter extends RecyclerView.Adapter<ImagesRecyclerAdapter.VH> {
    private Context context;
    private ArrayList<RoomImages> images= new ArrayList<>();

    public ImagesRecyclerAdapter(Context context) {
        this.context = context;
    }



    public void setImages(ArrayList<RoomImages> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    class  VH extends RecyclerView.ViewHolder{
        ImageView imageView;
        //TextView textView;

        public VH(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.ivivivivi);
           // textView=itemView.findViewById(R.id.image_single_item_text);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.images_single_item,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RoomImages roomImages=images.get(position);

        Glide.with(context).load(roomImages.getImageURL()).into(holder.imageView);
        //            Glide.with(context).load(hotelRoom.getImageUrl()).into(iv);
       // holder.textView.setText(roomImages.getImageURL());



    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
