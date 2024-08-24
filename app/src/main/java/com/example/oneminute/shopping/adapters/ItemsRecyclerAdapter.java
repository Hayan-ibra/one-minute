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

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.models.Store;
import com.example.oneminute.models.StoreItem;
import com.example.oneminute.shopping.ShopItemLists;
import com.example.oneminute.shopping.SingleItemActivity;

import java.util.ArrayList;

public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.VH> {
    private Context context;
    private ArrayList<StoreItem> items=new ArrayList<>();

    private onClicked listener;

    public ItemsRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<StoreItem> items) {
        this.items.clear();
        this.items = items;


        notifyDataSetChanged();
    }

    public void AdapterListener(onClicked listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.items_recycler_item,null,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        StoreItem item=items.get(position);
        holder.tv.setText(item.getItemName());
        holder.tv.setTag(position);
        Glide.with(context).load(item.getImageUrl()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class VH extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.textView_item_recycler_store_item);
            iv=itemView.findViewById(R.id.imageView_item_recycler_store_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) tv.getTag();
                    StoreItem item=items.get(pos);
                    Intent intent=new Intent(context, SingleItemActivity.class);
                    intent.putExtra("item",item);
                    context.startActivity(intent);
                }
            });
        }
    }

    public  interface  onClicked{
         void click(StoreItem item);
    }
}
