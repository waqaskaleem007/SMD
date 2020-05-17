package com.example.chamandryfruits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {


    private RecyclerView myOrdersRecyclerView;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        myOrdersRecyclerView = view.findViewById(R.id.my_orders_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myOrdersRecyclerView.setLayoutManager(layoutManager);

        List<MyOrdersItemModel> myOrdersItemModelList = new ArrayList<>();
        myOrdersItemModelList.add(new MyOrdersItemModel(R.mipmap.phone2,"Pixel 2(Black)","Delivered on Monday",3));
        myOrdersItemModelList.add(new MyOrdersItemModel(R.mipmap.phone_image,"Pixel 3(Black)","canceled",1));
        myOrdersItemModelList.add(new MyOrdersItemModel(R.mipmap.phone2,"Pixel 4(Black)","Delivered on Monday",2));
        myOrdersItemModelList.add(new MyOrdersItemModel(R.mipmap.phone_image,"Pixel 5(Black)","Delivered on Monday",0));
        myOrdersItemModelList.add(new MyOrdersItemModel(R.mipmap.phone2,"Pixel 6(Black)","canceled",4));

        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(myOrdersItemModelList);
        myOrdersRecyclerView.setAdapter(myOrderAdapter);
        myOrderAdapter.notifyDataSetChanged();

        return view;
    }
}
