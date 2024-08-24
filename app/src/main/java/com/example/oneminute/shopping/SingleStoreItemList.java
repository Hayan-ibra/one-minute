package com.example.oneminute.shopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.oneminute.R;
import com.example.oneminute.models.StoreItem;
import com.example.oneminute.models.pager.PagerProperties;
import com.example.oneminute.shopping.viewmodel.SingleStoreItemViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SingleStoreItemList extends AppCompatActivity {
    private FirebaseFirestore db =FirebaseFirestore.getInstance();
    private CollectionReference collectionReferenceItems=db.collection("StoreItems");
    private CollectionReference collectionReferenceStores=db.collection("StoreOwnerN");
    SingleStoreItemViewModel viewModel;
    ViewPager2 viewpager ;
    TabLayout tablayout;



      ArrayList<StoreItem> itemArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_store_item_list);

        tablayout=findViewById(R.id.SingleStoreItemList_tabLayout);
        viewpager=findViewById(R.id.SingleStoreItemList_viewpager);
        Intent intent =getIntent();
        String uid=intent.getStringExtra("1");
        String name=intent.getStringExtra("2");
        int state=intent.getIntExtra("3",0);
        viewModel=new ViewModelProvider(SingleStoreItemList.this).get(SingleStoreItemViewModel.class);
        getData(uid);



        viewModel.getItems().observe(this, new Observer<ArrayList<StoreItem>>() {
            @Override
            public void onChanged(ArrayList<StoreItem> items) {
                itemArrayList.clear();
                itemArrayList=items;
                List<String> cats = sortData(items);
                createTabs(cats);

            }
        });



    }

    private void createTabs(List<String> cats) {
        ArrayList<Fragment> fragments =new ArrayList<>();
        //List <PagerProperties> catigories=data.getCatigories();





        for (int i = 0 ; i<cats.size() ;i++){

            fragments.add(ShopItemListFragment.newInstance(cats.get(i),2,itemArrayList));

        }

        PagerAdapter pagerAdapter = new PagerAdapter(SingleStoreItemList.this,fragments);
        viewpager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tablayout, viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(cats.get(position));

            }

        }).attach();




    }

    private List<String >sortData(ArrayList<StoreItem> items) {
        Set<String> uniqueCategories = new HashSet<>();

        // Extract categories from items
        for (StoreItem item : items) {
            uniqueCategories.addAll(item.getCatigory());
        }

        // Convert set back to arraylist
        return  new ArrayList<>(uniqueCategories);

    }

    private void getData(String uid) {
        ArrayList<StoreItem> storeItems=new ArrayList<>();

        collectionReferenceItems.whereEqualTo("parentId",uid).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    for (QueryDocumentSnapshot snapshot: value){
                        StoreItem item=snapshot.toObject(StoreItem.class);
                        storeItems.add(item);
                    }
                    viewModel.setItems(storeItems);

                }else {
                    Toast.makeText(SingleStoreItemList.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}