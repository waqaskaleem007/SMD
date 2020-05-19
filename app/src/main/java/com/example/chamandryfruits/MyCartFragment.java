package com.example.chamandryfruits;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {

    private RecyclerView cartItemRecyclerView;
    private Button Continue;

    public MyCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        cartItemRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        Continue = view.findViewById(R.id.cart_continue_btn);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0, R.mipmap.phone2, "Pixel 2", 3, "Rs.49999/-",3,2,"Rs.59999/-",0));
        cartItemModelList.add(new CartItemModel(0, R.mipmap.phone2, "Pixel 3", 2, "Rs.69999/-",0,0,"Rs.159999/-",2));
        cartItemModelList.add(new CartItemModel(0, R.mipmap.phone2, "Pixel 4", 1, "Rs.79999/-",2,0,"Rs.259999/-",3));
        cartItemModelList.add(new CartItemModel(0, R.mipmap.phone2, "Pixel 5", 4, "Rs.89999/-",1,1,"Rs.459999/-",1));

        cartItemModelList.add(new CartItemModel(1,"Price(3) items","Rs.49999/-","Free", "Rs.9999/-", "Rs.499999/-"));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemRecyclerView.setLayoutManager(layoutManager);

        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        cartItemRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        Continue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(getContext(),AddAddressActivity.class);
                Objects.requireNonNull(getContext()).startActivity(deliveryIntent);
            }
        });

        return view;
    }
}
