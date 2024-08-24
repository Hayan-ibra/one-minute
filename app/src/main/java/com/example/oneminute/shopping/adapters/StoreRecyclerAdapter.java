package com.example.oneminute.shopping.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.models.Store;
import com.example.oneminute.shopping.SingleStoreActivity;

import java.util.ArrayList;

public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.VH> {
    private Context context;
    private ArrayList<Store> stores=new ArrayList<>();

    public StoreRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.store_recycler_item,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Store store=stores.get(position);
        holder.tv.setText(store.getStoreName());
        Glide.with(context).load(store.getImageUrl()).into(holder.iv);


    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView tv;



        public VH(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.store_recycler_item_image);
            tv=itemView.findViewById(R.id.store_recycler_item_text_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Store st=stores.get(getAdapterPosition());
                    Intent intent=new Intent(context, SingleStoreActivity.class);
                    intent.putExtra("shop",st);
                    context.startActivity(intent);

                }
            });
        }
    }



}
