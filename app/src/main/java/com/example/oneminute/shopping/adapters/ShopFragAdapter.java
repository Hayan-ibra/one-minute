package com.example.oneminute.shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        holder.tv_title.setText(util1.getTitle());
        holder.iv_startIcon.setImageResource(util1.getIcon());



    }

    @Override
    public int getItemCount() {
        return utils.size();
    }

    public class Vh extends RecyclerView.ViewHolder{
        ImageView iv_go,iv_line,iv_startIcon;
        TextView tv_title;





        public Vh(@NonNull View itemView) {
            super(itemView);
            iv_go=itemView.findViewById(R.id.store_item_img_go);
            iv_line=itemView.findViewById(R.id.store_item_img_underline);
            tv_title=itemView.findViewById(R.id.store_item_text_title);
            iv_startIcon=itemView.findViewById(R.id.store_item_img_star_icon);
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
