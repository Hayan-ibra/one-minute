package com.example.oneminute.restaurants.restaurants_recycler_adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneminute.R;
import com.example.oneminute.models.TypesModelClass;

import java.util.ArrayList;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.VH> {
    ArrayList<TypesModelClass> models;

    int count=0;
    Context context;


    Listenerr listenerr;

    public TypesAdapter(ArrayList<TypesModelClass> models, Context context, Listenerr listenerr) {
        this.models = models;
        this.context = context;
        this.listenerr = listenerr;

    }

    class  VH extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txt;

        CardView cardView;


        public VH(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.image_type_item);
            txt=itemView.findViewById(R.id.text_type_item);
           cardView=itemView.findViewById(R.id.card_type_item);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos =getAdapterPosition();
                    trans(pos);

                    listenerr.onclicked(pos,models.get(pos).getRealName(),models.get(pos).getName());
                }
            });

        }

        void trans(int i){
            count=i;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.types_simple_item,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        TypesModelClass icon =models.get(position);

        holder.txt.setText(icon.getName());
        holder.img.setImageResource(icon.getIcon());

        /*
         models.add(new TypesModelClass(R.drawable.meal,"Meals","meals"));
        models.add(new TypesModelClass(R.drawable.food,"Fast food" , "fast_food" ));
        models.add(new TypesModelClass(R.drawable.hamburger,"Burger", "burger" ));
        models.add(new TypesModelClass(R.drawable.ice_cream,"Dessert", "dessert" ));
        models.add(new TypesModelClass(R.drawable.fish_end,"Fish", "sea_food" ));
        models.add(new TypesModelClass(R.drawable.food_steak,"Western", "western_foods" ));
        models.add(new TypesModelClass(R.drawable.salad,"Salads", "salad" ));
        models.add(new TypesModelClass(R.drawable.sandwitch,"Sandwich",  "sandwich"));
        models.add(new TypesModelClass(R.drawable.soup,"Soup",  "soup"));
        models.add(new TypesModelClass(R.drawable.pizza,"Pizza","pizza"  ));
        models.add(new TypesModelClass(R.drawable.pasta,"Pasta", "pasta"  ));
        models.add(new TypesModelClass(R.drawable.beer,"Beverage",  "beverage" ));
        models.add(new TypesModelClass(R.drawable.glass_cocktail,"Cocktails", "cocktails"  ));
        models.add(new TypesModelClass(R.drawable.baseline_restaurant_menu_24,"Others",  "others" ));
         */



        holder.cardView.setCardBackgroundColor(Color.WHITE);
        int colorInt = Color.parseColor("#7A58AA");
        holder.img.setColorFilter(colorInt);




        if (count==position){
           // holder.txt.setText("500");


            holder.img.setColorFilter(Color.WHITE);
            int colorInt2 = Color.parseColor("#7A58AA");
            holder.cardView.setCardBackgroundColor(colorInt2);


/*
            if(icon.getRealName()==String.valueOf("meals")){
               holder.img.setImageResource(R.drawable.meal_blue);
               holder.img.setColorFilter(Color.WHITE);

                int colorInt = Color.parseColor("#0d5dc8");
                holder.cardView.setCardBackgroundColor(colorInt);
            } else if (icon.getRealName()==String.valueOf("fast_food")) {
                holder.img.setImageResource(R.drawable.food_blue);

            }
            else if (icon.getRealName()==String.valueOf("burger")) {
                holder.img.setImageResource(R.drawable.hamburger_blue);

            }
            else if (icon.getRealName()==String.valueOf("dessert")) {
                holder.img.setImageResource(R.drawable.ice_cream_blue);

            }
            else if (icon.getRealName()==String.valueOf("sea_food")) {
                holder.img.setImageResource(R.drawable.fish_blue);

            }
            else if (icon.getRealName()==String.valueOf("western_foods")) {
                holder.img.setImageResource(R.drawable.food_steak_blue);

            }
            else if (icon.getRealName()==String.valueOf("salad")) {
                holder.img.setImageResource(R.drawable.salad_blue);

            }
            else if (icon.getRealName()==String.valueOf( "sandwich")) {
                holder.img.setImageResource(R.drawable.sandwitch_blue);


            }
            else if (icon.getRealName()==String.valueOf("soup")) {
                holder.img.setImageResource(R.drawable.soup_blue);


            }
            else if (icon.getRealName()==String.valueOf("pizza" )) {
                holder.img.setImageResource(R.drawable.pizza_blue);


            }
            else if (icon.getRealName()==String.valueOf("pasta" )) {
                holder.img.setImageResource(R.drawable.pasta_blue);


            }
            else if (icon.getRealName()==String.valueOf("beverage"  )) {
                holder.img.setImageResource(R.drawable.beer_blue);



            }
            else if (icon.getRealName()==String.valueOf( "cocktails"  )) {
                holder.img.setImageResource(R.drawable.glass_cocktail_blue);



            }
            else if (icon.getRealName()==String.valueOf(  "others"   )) {
                holder.img.setImageResource(R.drawable.baseline_restaurant_menu_24_blue);




            }*/





        }








    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public interface Listenerr{
        public  void  onclicked(int count,String tag,String displayName);
    }



}
