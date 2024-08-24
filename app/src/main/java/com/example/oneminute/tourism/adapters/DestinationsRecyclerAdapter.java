package com.example.oneminute.tourism.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.oneminute.R;
import com.example.oneminute.models.TouristDestinations;
import com.example.oneminute.tourism.SingleDestination;
import com.example.oneminute.tourism.my_image_view.DynamicImageView;

import java.util.ArrayList;

public class DestinationsRecyclerAdapter extends RecyclerView.Adapter<DestinationsRecyclerAdapter.Vh> {
    private Context context ;
    private ArrayList<TouristDestinations> destinations=new ArrayList<>();

    public DestinationsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setDestinations(ArrayList<TouristDestinations> destinations) {
        this.destinations = destinations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.tourist_destination_item,parent,false);

        return new Vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        TouristDestinations dest =destinations.get(position);
        Glide.with(context).load(dest.getImageUrl()).into(holder.imageView);
        holder.textView.setText(dest.getName());
        holder.tv_location.setText("  "+dest.getLocation());
        holder.tv_city.setText("  "+dest.getCity());
        holder.tv_time_from.setText(dest.getTimeFrom());
        holder.tv_time_to.setText(dest.getTimeTo());




    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    class Vh extends RecyclerView.ViewHolder{

        DynamicImageView imageView;
        TextView textView,tv_location,tv_city,tv_time_from,tv_time_to;
        Button button;

        public Vh(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.tourist_destination_image);
            textView=itemView.findViewById(R.id.tourist_destination_text);
            tv_city=itemView.findViewById(R.id.tourist_destination_text_city);
            tv_location=itemView.findViewById(R.id.tourist_destination_text_location);
            tv_time_from=itemView.findViewById(R.id.tourist_destination_text_work_from);
            tv_time_to=itemView.findViewById(R.id.tourist_destination_text_work_to);
            button=itemView.findViewById(R.id.hotel_item_button1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, SingleDestination.class);
                    intent.putExtra("dest",destinations.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
