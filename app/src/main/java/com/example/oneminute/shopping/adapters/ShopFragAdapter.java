package com.example.oneminute.shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneminute.R;
import com.example.oneminute.models.pager.StoreUtils;
import com.example.oneminute.shopping.ShopItemLists;

import java.util.ArrayList;

public class ShopFragAdapter extends RecyclerView.Adapter<ShopFragAdapter.Vh> {
    Context context;
    ArrayList<StoreUtils> utils;



    public ShopFragAdapter(Context context, ArrayList<StoreUtils> utils) {
        this.context = context;
        this.utils = utils;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.storeutils_item,null,false);
        return new Vh(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        StoreUtils util1=utils.get(position);


       /* ViewGroup.LayoutParams layoutParams = holder.iv.getLayoutParams();


       layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;


        layoutParams.height = (int) (layoutParams.width * 380.0 / 900.0);

        holder.iv.setLayoutParams(layoutParams);*/
        holder.iv.setImageResource(util1.getImage());





    }

    @Override
    public int getItemCount() {
        return utils.size();
    }

    public class Vh extends RecyclerView.ViewHolder{
        ImageView iv;







        public Vh(@NonNull View itemView) {
            super(itemView);

            iv=itemView.findViewById(R.id.stores_types_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ShopItemLists.class);
                    intent.putExtra("objl",utils.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }







}
