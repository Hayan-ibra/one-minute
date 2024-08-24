package com.example.oneminute.restaurants.order;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.models.Orders;

import java.util.ArrayList;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.VH> {


        ArrayList<Orders> orders=new ArrayList<>();
        Context context;
        String userName;



public OrderRecyclerAdapter(String userName,Context context) {

        this.context = context;
        this.userName=userName;


        }

public void setOrders(ArrayList<Orders> orders) {
        this.orders = orders;
        notifyDataSetChanged();
        }

public class VH extends RecyclerView.ViewHolder{
    TextView tv_name,tv_food_name,tv_time,tv_number,tv_state,tv_user;
    ImageView iv_food;


    CardView cardView;

    public VH(@NonNull View itemView) {
        super(itemView);
        tv_name=itemView.findViewById(R.id.order_item_text_resName);
        tv_food_name=itemView.findViewById(R.id.order_item_text_food_name);
        tv_number=itemView.findViewById(R.id.order_item_text_number_of_items);
        tv_user=itemView.findViewById(R.id.order_item_text_UserName);
        tv_time=itemView.findViewById(R.id.order_item_text_tiem);
        iv_food=itemView.findViewById(R.id.order_item_image_food);

        cardView=itemView.findViewById(R.id.card_order);
        tv_state=itemView.findViewById(R.id.order_item_text_state);





    }
}






    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Orders order =orders.get(position);
        holder.tv_name.setText("From : "+order.getResReply());
        holder.tv_user.setText("User : "+userName);

        String time= (String) DateUtils.getRelativeTimeSpanString(order.getTime().getSeconds()*1000);
        holder.tv_food_name.setText("Order : "+order.getFoodName());
        holder.tv_time.setText("Time : "+time);
        holder.tv_number.setText("Quantity : "+order.getNumberOfItems());
       // holder.tv_phone.setText("phone "+order.getPhoneNum());
        holder.tv_state.setText("On Hold");
        Glide.with(context).load(order.getImageUrl()).into(holder.iv_food);

        holder.tv_name.setTag(order.getUserId());
        //holder.tv_phone.setTag(order.getTimeForDelete());
        if (order.getState()==1){

            holder.tv_state.setText("Accepted");
            holder.tv_state.setTextColor(Color.GREEN);
            holder.tv_state.setVisibility(View.VISIBLE);

        } else if (order.getState()==2) {

            holder.tv_state.setText("Denied");
            holder.tv_state.setTextColor(Color.RED);
            holder.tv_state.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }



}
