package com.example.oneminute.shopping;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oneminute.R;
import com.example.oneminute.models.Store;
import com.example.oneminute.models.StoreItem;
import com.example.oneminute.shopping.adapters.GridSpacingItemDecoration;
import com.example.oneminute.shopping.adapters.ItemsRecyclerAdapter;
import com.example.oneminute.shopping.adapters.StoreRecyclerAdapter;
import com.example.oneminute.shopping.viewmodel.ItemsViewModel;
import com.example.oneminute.shopping.viewmodel.StoresViewModel;

import java.util.ArrayList;


public class ShopItemListFragment extends Fragment {

    //third
    //here iit will display the filtered data
    //the data will go from get instance and be saved
    //then it will be passed through check method and be displayed
    //if sate is 1 it will be stores fragment
    //if state is 0 it will be items
    //if state is 2 so it will items from store



    StoreRecyclerAdapter storeRecyclerAdapter;
    static ItemsRecyclerAdapter itemsRecyclerAdapter;

    ItemsViewModel viewModel;
    StoresViewModel storesViewModel;





    ArrayList<StoreItem> finalRes =new ArrayList<>();

  //  private  OnFragmentClick listener;
    private static final String CATEGORY = "category";
    private static final String TYPE="aa";
    private static final String ARRAY="array";


    private String category;
    private int state;


    ArrayList<StoreItem> items=new ArrayList<>();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        finalRes.clear();


        itemsRecyclerAdapter=new ItemsRecyclerAdapter(getActivity());
        storeRecyclerAdapter=new StoreRecyclerAdapter(getActivity());
        //finalRes.clear();
        itemsRecyclerAdapter.notifyDataSetChanged();




    }

    @Override
    public void onDetach() {
        super.onDetach();



    }


    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(), ""+category, Toast.LENGTH_SHORT).show();
        //
        itemsRecyclerAdapter.notifyDataSetChanged();
       // check(SingleStoreItemList.itemArrayList,category);



    }

    public ShopItemListFragment() {
        // Required empty public constructor
    }

    public static ShopItemListFragment newInstance(String category,int type,ArrayList<StoreItem> storeItems) {
        ShopItemListFragment fragment = new ShopItemListFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY, category);
        args.putInt(TYPE,type);
        args.putSerializable(ARRAY,storeItems);



        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY);
            state=getArguments().getInt(TYPE);
            items= (ArrayList<StoreItem>) getArguments().getSerializable(ARRAY);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_shop_item_list, container, false);
        RecyclerView recyclerView=v.findViewById(R.id.ShopItemListFragment_recycler_male);
        viewModel=new ViewModelProvider(getActivity()).get(ItemsViewModel.class);
        storesViewModel=new ViewModelProvider(getActivity()).get(StoresViewModel.class);

       /* vm.getItems().observe(getActivity(), new Observer<ArrayList<StoreItem>>() {
            @Override
            public void onChanged(ArrayList<StoreItem> items) {
                itemsRecyclerAdapter.setItems(items);

            }
        });*/


        if (state==1){
            storesViewModel.searchStores(category);
            //storeRecyclerAdapter=new StoreRecyclerAdapter(getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(storeRecyclerAdapter);
            recyclerView.setHasFixedSize(true);

        } else if (state==0) {

            check(items,category);
            itemsRecyclerAdapter.setItems(finalRes);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            int spacingInPixels =20 ;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
            recyclerView.setAdapter(itemsRecyclerAdapter);
            recyclerView.setHasFixedSize(true);
        } else if (state==2) {
            finalRes.clear();

            check(items,category);
            itemsRecyclerAdapter.setItems(finalRes);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            int spacingInPixels =20 ;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
            recyclerView.setAdapter(itemsRecyclerAdapter);
            recyclerView.setHasFixedSize(true);
        }


        storesViewModel.getStores(category).observe(getActivity(), new Observer<ArrayList<Store>>() {
            @Override
            public void onChanged(ArrayList<Store> stores) {
                storeRecyclerAdapter.setStores(stores);
            }
        });




        return v;
    }




    private  void check(ArrayList<StoreItem>  it,String cat){
        //finalRes.clear();
        for(int i=0;i<it.size();i++){
            if (it.get(i).getCatigory().contains(cat)){
                finalRes.add(it.get(i));
            }


        }



        //itemsRecyclerAdapter.setItems(finalRes);



    }



/*

    public interface OnFragmentClick {
        void onFragmentInteraction(String cat);
        void onFragmentInteractionStore(String cat);
    }*/
}