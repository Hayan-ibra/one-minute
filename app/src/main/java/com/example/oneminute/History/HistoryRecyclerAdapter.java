package com.example.oneminute.History;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneminute.R;
import com.example.oneminute.models.History;

import java.util.ArrayList;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.VH> {

    private  ArrayList<History> histories;

    private  Context context;

    public HistoryRecyclerAdapter(ArrayList<History> histories, Context context) {
        this.histories = histories;
        this.context = context;
    }

    public class VH extends RecyclerView.ViewHolder{
        ImageView ivColor;
        TextView tv_points;
        TextView tv_name;
        TextView tv_date;
        TextView tv_quantity;
        TextView tv_parent_name;
        TextView tv_item_name;


        public VH(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.history_points_text_date);
            tv_points=itemView.findViewById(R.id.history_points_text_points);
            tv_name=itemView.findViewById(R.id.history_points_text_name);
            ivColor=itemView.findViewById(R.id.history_points_image_color);
            tv_quantity=itemView.findViewById(R.id.history_points_text_quantity);
            tv_parent_name=itemView.findViewById(R.id.history_points_text_parent_name);
            tv_item_name=itemView.findViewById(R.id.history_points_text_item_type);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.history_points_item,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        History history=histories.get(position);

        holder.tv_name.setText("name "+history.getName());
        holder.tv_points.setText(String.format("%.0f",history.getValue()).toString());
        holder.tv_item_name.setText("type : "+history.getType());



        //        String timeAgo= (String) DateUtils.getRelativeTimeSpanString(journal.getTimeAdded().getSeconds()*1000);

        String date= (String) DateUtils.getRelativeTimeSpanString(history.getDate().getSeconds()*1000);
        holder.tv_date.setText(date);

        holder.tv_parent_name.setText(history.getParentName());
        holder.tv_quantity.setText("quantity :"+history.getQuantity());

        if(history.isState()){
            holder.ivColor.setColorFilter(Color.RED);
            holder.tv_points.setTextColor(Color.RED);

        }else {
            holder.ivColor.setColorFilter(Color.GREEN);
            holder.tv_points.setTextColor(Color.GREEN);


        }


    }

    @Override
    public int getItemCount() {
        return histories.size();
    }







}
