package com.example.oneminute.restaurants.restaurants_recycler_adapters;

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
import com.example.oneminute.models.RestaurantProfile;
import com.example.oneminute.restaurants.RestaurantSingleProfile;

import java.util.ArrayList;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.VH> {

    ArrayList<RestaurantProfile> restaurants ;
    Context context;



    TypesAdapter.Listenerr listenerr;

    public RestaurantsAdapter(ArrayList<RestaurantProfile> restaurants, Context context, TypesAdapter.Listenerr listenerr) {
        this.restaurants = restaurants;
        this.context = context;
        this.listenerr = listenerr;
    }

    public class VH extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView tv_name,tv_location;


        public VH(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.restaurant_item_image);
            tv_name=itemView.findViewById(R.id.restaurant_item_text_name);
            tv_location=itemView.findViewById(R.id.restaurant_item_text_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();

                    Intent intent =new Intent(context, RestaurantSingleProfile.class);
                    RestaurantProfile restaurant =restaurants.get(position);

                    //listenerr.onclicked(position1);

                    intent.putExtra("2",restaurant.getParent_user_id());


                    intent.putExtra("res",restaurant);
                   /* intent.putExtra("1",restaurant.getName());

                    intent.putExtra("3",restaurant.getCity());
                    intent.putExtra("4",restaurant.getUrl());
                    intent.putExtra("5",restaurant.getLocation());
                    intent.putExtra("6",restaurant.getPhoneNum());
                    intent.putExtra("7",restaurant.getRate());
                    intent.putExtra("8",restaurant.getDescription());
                    intent.putExtra("9",restaurant.getWorkTime_from());
                    intent.putExtra("10",restaurant.getWorkTime_to());

                    */

                    context.startActivity(intent);
                }
            });


        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.restaurant_item,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RestaurantProfile res=restaurants.get(position);
        Glide.with(context).load(res.getUrl()).fitCenter().into(holder.iv);
        holder.tv_name.setText(res.getName());
        holder.tv_location.setText(res.getCity());


    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }


    public interface Listenerr{
        public  void  onclicked(int count);
    }




}
