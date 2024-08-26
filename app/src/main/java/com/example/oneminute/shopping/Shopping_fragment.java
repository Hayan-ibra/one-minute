package com.example.oneminute.shopping;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oneminute.R;
import com.example.oneminute.models.pager.PagerProperties;
import com.example.oneminute.models.pager.StoreUtils;
import com.example.oneminute.shopping.adapters.ShopFragAdapter;

import java.util.ArrayList;
import java.util.List;



public class Shopping_fragment extends Fragment {
    //First it starts here

    ArrayList<StoreUtils> utils=new ArrayList<>();




    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public Shopping_fragment() {
        // Required empty public constructor
    }

    public static Shopping_fragment newInstance(String param1, String param2) {
        Shopping_fragment fragment = new Shopping_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_shopping, container, false);




        fill();

        RecyclerView rec=v.findViewById(R.id.shopping_fragment_recycler);
        ShopFragAdapter adapter=new ShopFragAdapter(getActivity(),utils);
        rec.setAdapter(adapter);
        rec.setLayoutManager(new LinearLayoutManager(getActivity()));
        rec.setHasFixedSize(true);

        return v;
    }

















    private void fill() {
        List<PagerProperties> list01=new ArrayList<>();
        list01.add(new PagerProperties("tops",R.drawable.shirt));
        list01.add(new PagerProperties("shorts",R.drawable.shirt));
        list01.add(new PagerProperties("bottoms",R.drawable.shirt));
        list01.add(new PagerProperties("sportwear",R.drawable.shirt));
        utils.add(new StoreUtils("Clothing", "clothing",list01,R.drawable.shirt,R.drawable.clothing));

        List<PagerProperties> list02 =new ArrayList<>();
        list02.add(new PagerProperties("jewelry",R.drawable.jewerly));
        list02.add(new PagerProperties("watches",R.drawable.jewerly));
        list02.add(new PagerProperties("bags",R.drawable.jewerly));
        utils.add(new StoreUtils("Accessories","accessories",list02,R.drawable.jewerly,R.drawable.accessories));


        List<PagerProperties> list04=new ArrayList<>();
        list04.add(new PagerProperties("outerwear",R.drawable.seasonalimg));
        list04.add(new PagerProperties("swimwear",R.drawable.seasonalimg));
        list04.add(new PagerProperties("sleepwear",R.drawable.seasonalimg));
        utils.add(new StoreUtils("Seasonal","seasonal",list04,R.drawable.seasonal,R.drawable.seasonalimg));

        List<PagerProperties> list05=new ArrayList<>();
        list05.add(new PagerProperties("workwear",R.drawable.formal));
        list05.add(new PagerProperties("formalwear",R.drawable.formal));
        list05.add(new PagerProperties("underwear",R.drawable.formal));
        utils.add(new StoreUtils("Lifes tyle","lifestyle",list05,R.drawable.formal,R.drawable.lifestyle));

        List<PagerProperties> list06=new ArrayList<>();
        list06.add(new PagerProperties("smartwatch",R.drawable.smartwatch));
        list06.add(new PagerProperties("fitness",R.drawable.smartwatch));
        list06.add(new PagerProperties("trackers",R.drawable.smartwatch));
        utils.add(new StoreUtils("Technology","technology",list06,R.drawable.smartwatch,R.drawable.technologyy));

        List<PagerProperties> list03=new ArrayList<>();
        list03.add(new PagerProperties("shoes",R.drawable.shoes));
        list03.add(new PagerProperties("boots",R.drawable.shoes));
        list03.add(new PagerProperties("sandals",R.drawable.shoes));
        utils.add(new StoreUtils("Foot wear","footwear",list03,R.drawable.shoes,R.drawable.footwear));

    }


}