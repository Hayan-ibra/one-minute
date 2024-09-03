package com.example.oneminute.restaurants.restaurants_recycler_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oneminute.R;
import com.example.oneminute.models.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.VH> {



    Context context;
    List<Food> foods;
    Listenerr listenerr;


    public FoodAdapter(Context context, List<Food> foods,FoodAdapter.Listenerr listenerr) {
        this.context = context;
        this.foods = foods;
        this.listenerr=listenerr;

    }

    public class  VH extends RecyclerView.ViewHolder{

        TextView tv_name;
        ImageView image;


        public VH(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.textView_item_recycler_food);
            image=itemView.findViewById(R.id.imageView_item_recycler_food);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position1=getAdapterPosition();


                    Food food1 =foods.get(position1);

                    listenerr.onclicked(position1,food1);



                }
            });

        }


    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.food_item,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Food food=foods.get(position);
        holder.tv_name.setText(food.getName().trim());

        //using glide
        Glide.with(context).load(food.getFoodUrl()).fitCenter().into(holder.image);


    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public interface Listenerr{
        public  void  onclicked(int count,Food food);
    }

}
