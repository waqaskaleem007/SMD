package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Objects;

import static com.example.chamandryfruits.DBQueries.categoryModels;
import static com.example.chamandryfruits.DBQueries.homePageModelList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter homePageAdapter;
    private ImageView noInternetConnection;

    public HomeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {
            noInternetConnection.setVisibility(View.GONE);

            recyclerView = view.findViewById(R.id.category_recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);


            final CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModels);
            recyclerView.setAdapter(categoryAdapter);

            if(categoryModels.size() == 0){
                DBQueries.LoadCategories(categoryAdapter, getContext());
            }else {
                categoryAdapter.notifyDataSetChanged();                         //to reset the data in the adapter if the adapter is not null for the 2nd time
            }


            homePageRecyclerView = view.findViewById(R.id.homePage_recyclerView);
            LinearLayoutManager testingLinearLayout = new LinearLayoutManager(getContext());
            testingLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
            homePageRecyclerView.setLayoutManager(testingLinearLayout);

            homePageAdapter = new HomePageAdapter(homePageModelList);
            homePageRecyclerView.setAdapter(homePageAdapter);

            if(homePageModelList.size() == 0){
                DBQueries.LoadFragmentData(homePageAdapter, getContext());
            }else {
                homePageAdapter.notifyDataSetChanged();                         //to reset the data in the adapter if the adapter is not null for the 2nd time
            }


        }else{
            Glide.with(this).load(R.drawable.no_internet_connection).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }


        return view;
    }

}
