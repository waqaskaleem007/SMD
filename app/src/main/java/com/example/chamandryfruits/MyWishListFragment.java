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
public class MyWishListFragment extends Fragment {

    private RecyclerView wishListRecyclerView;

    public MyWishListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_wish_list, container, false);
        wishListRecyclerView = view.findViewById(R.id.my_wishList_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        wishListRecyclerView.setLayoutManager(layoutManager);

        List<WishListModel> wishListModels = new ArrayList<>();
        wishListModels.add(new WishListModel(R.mipmap.phone2, "Pixel 2(Black)",3,"1.5",700,"Rs.49999/-","Rs.59999/-","Cash on delivery"));
        wishListModels.add(new WishListModel(R.mipmap.phone_image, "Pixel 3(Black)",2,"2.5",800,"Rs.59999/-","Rs.59999/-","Cash on delivery"));
        wishListModels.add(new WishListModel(R.mipmap.phone2, "Pixel 4(Black)",1,"3.5",300,"Rs.69999/-","Rs.59999/-","with card"));
        wishListModels.add(new WishListModel(R.mipmap.phone_image, "Pixel 5(White)",0,"4.5",200,"Rs.79999/-","Rs.59999/-","Cash on delivery"));
        wishListModels.add(new WishListModel(R.mipmap.phone2, "Pixel 6(Black)",4,"2.5",1700,"Rs.89999/-","Rs.59999/-","Cash on delivery"));
        wishListModels.add(new WishListModel(R.mipmap.phone_image, "Pixel 7(Black)",0,"4.5",7200,"Rs.99999/-","Rs.59999/-","Cash on delivery"));

        WishListAdapter wishListAdapter = new WishListAdapter(wishListModels,true);
        wishListRecyclerView.setAdapter(wishListAdapter);
        wishListAdapter.notifyDataSetChanged();


        return view;
    }
}
