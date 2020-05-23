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
public class MyRewardsFragment extends Fragment {

    private RecyclerView rewardsRecyclerView;

    public MyRewardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);
        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsRecyclerView.setLayoutManager(layoutManager);

        List<RewardsModel> rewardsModels = new ArrayList<>();
        rewardsModels.add(new RewardsModel("Discount","till 24th june 2020","50% discount on each item"));
        rewardsModels.add(new RewardsModel("Free","till 25th june 2020","Get 20% off on any product above Rs.500/- and below Rs.2500/-"));
        rewardsModels.add(new RewardsModel("Discount","till 26th june 2020","50% discount on each item"));
        rewardsModels.add(new RewardsModel("Free","till 27th june 2020","Get 20% off on any product above Rs.5500/- and below Rs.9500/-"));
        rewardsModels.add(new RewardsModel("Cashback","till 28th june 2020","50% discount on each item"));
        rewardsModels.add(new RewardsModel("Discount","till 29th june 2020","Get 20% off on any product above Rs.1500/- and below Rs.3500/-"));
        rewardsModels.add(new RewardsModel("Cashback","till 30th june 2020","50% discount on each item"));
        rewardsModels.add(new RewardsModel("Discount","till 31th june 2020","Get 20% off on any product above Rs.5200/- and below Rs.2500/-"));
        rewardsModels.add(new RewardsModel("Free","till 1st june 2020","Buy one get one free"));
        rewardsModels.add(new RewardsModel("Discount","till 4th june 2020","Get 20% off on any product above Rs.5300/- and below Rs.25030/-"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardsModels,false);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();


        return view;
    }
}
