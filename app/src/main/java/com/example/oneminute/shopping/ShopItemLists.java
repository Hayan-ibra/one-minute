package com.example.oneminute.shopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.oneminute.R;
import com.example.oneminute.models.Store;
import com.example.oneminute.models.StoreItem;
import com.example.oneminute.models.pager.PagerProperties;
import com.example.oneminute.models.pager.StoreUtils;
import com.example.oneminute.shopping.viewmodel.ItemsViewModel;
import com.example.oneminute.shopping.viewmodel.StoresViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopItemLists extends AppCompatActivity {
    //second you will end up here
    //in this fragment  we will get data through view-model

    ViewPager2 viewpager ;
    TabLayout tablayut;


    ItemsViewModel viewModel;
    StoresViewModel storesViewModel;


    Intent intent;
    StoreUtils data;
    List <PagerProperties> catigories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_lists);
        tablayut=findViewById(R.id.tabLayout);
        viewpager=findViewById(R.id.viewpager);

        //initialize viwmodels
        viewModel=new ViewModelProvider(ShopItemLists.this).get(ItemsViewModel.class);
        storesViewModel=new ViewModelProvider(ShopItemLists.this).get(StoresViewModel.class);

        intent=getIntent();
        data= (StoreUtils) intent.getSerializableExtra("objl");
        catigories=data.getCatigories();


        List<String> searchCategories =new ArrayList<>();
        // Arrays.asList("category1", "category2", "category3")

        for (int i=0;i<catigories.size();i++){
            searchCategories.add(catigories.get(i).getName());

        }
        Arrays.asList(searchCategories);
        getData(searchCategories,catigories);










    }

    private void initiatePager(ArrayList<StoreItem> items) {
        ArrayList<Fragment> fragments =new ArrayList<>();



        //TODO

        fragments.add(ShopItemListFragment.newInstance(data.getSearchStore(),1,items));
        Toast.makeText(this, ""+data.getSearchStore(), Toast.LENGTH_SHORT).show();


        for (int i = 0 ; i<catigories.size() ;i++){
            //TODO
            fragments.add(ShopItemListFragment.newInstance(catigories.get(i).getName(),0,items));

        }

        PagerAdapter pagerAdapter = new PagerAdapter(ShopItemLists.this,fragments);
        viewpager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tablayut, viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                //listener
                //it will be called when creating tab for the first time
                if (position==0){
                    tab.setText("Stores");
                    tab.setIcon(R.drawable.baseline_shopping_cart_blue_24);
                }else {
                    tab.setText(catigories.get(position - 1).getName());
                    tab.setIcon(catigories.get(position - 1).getIcon());
                    //tab . set icon
                }

            }

        }).attach();
    }

    private void getData(List<String> searchCategories, List <PagerProperties> cats) {
        viewModel.getItems(searchCategories).observe(ShopItemLists.this, new Observer<ArrayList<StoreItem>>() {
            @Override
            public void onChanged(ArrayList<StoreItem> items) {
                ArrayList<StoreItem> it=new ArrayList<>(items);
                initiatePager(it);


            }
        });


    }




}