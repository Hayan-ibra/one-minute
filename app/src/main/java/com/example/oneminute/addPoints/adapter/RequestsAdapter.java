package com.example.oneminute.addPoints.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneminute.R;
import com.example.oneminute.models.RequestPoints;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.VH> {
    private Context context;
    private ArrayList<RequestPoints> points=new ArrayList<>();

    public RequestsAdapter(Context context, ArrayList<RequestPoints> points) {
        this.context = context;
        this.points = points;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.request_item,parent,false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RequestPoints requestPoints=points.get(position);
        holder.tv_error.setVisibility(View.GONE);
        String time= (String) DateUtils.getRelativeTimeSpanString(requestPoints.getTimestamp().getSeconds()*1000);
        holder.tv_timestamp.setText("From : "+time);
        holder.tv_code.setText("Code : "+requestPoints.getCode());
        holder.tv_amount.setText("Amount : "+requestPoints.getAmount());
        if (requestPoints.getState() == 1){
            holder.tv_state.setText("on hold");
            holder.imageView.setColorFilter(Color.BLUE);

        } else if (requestPoints.getState() == 2) {
            holder.tv_state.setText("Accepted");
            holder.imageView.setColorFilter(Color.GREEN);
            // 1 on hold  2 accepted  3 rejected
        }else if (requestPoints.getState() == 3) {
            holder.tv_state.setText("Rejected");
            holder.imageView.setColorFilter(Color.RED);
            if (requestPoints.getErrorCode()!=null){
                holder.tv_error.setVisibility(View.VISIBLE);
                holder.tv_error.setText(requestPoints.getErrorCode());
            }
        }
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    class VH extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tv_state,tv_amount,tv_code,tv_error,tv_timestamp;
        public VH(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.request_image_color);
            tv_code=itemView.findViewById(R.id.request_text_item_code);
            tv_error=itemView.findViewById(R.id.request_text_item_error);
            tv_state=itemView.findViewById(R.id.request_text_state);
            tv_amount=itemView.findViewById(R.id.request_text_amount);
            tv_timestamp=itemView.findViewById(R.id.request_text_item_time);
        }
    }

}
