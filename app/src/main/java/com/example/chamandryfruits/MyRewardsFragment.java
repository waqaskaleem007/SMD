package com.example.chamandryfruits;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyRewardsFragment extends Fragment {

    private RecyclerView rewardsRecyclerView;
    private Dialog loadingDialog;
    public static MyRewardsAdapter myRewardsAdapter;

    public MyRewardsFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);
        ///loading Dialog
        loadingDialog = new Dialog(Objects.requireNonNull(getContext()));
        loadingDialog.setContentView(R.layout.loadind_progress_dialog);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////loading Dialog
        rewardsRecyclerView = view.findViewById(R.id.my_rewards_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rewardsRecyclerView.setLayoutManager(layoutManager);

        myRewardsAdapter = new MyRewardsAdapter(DBQueries.rewardsModels,false);
        rewardsRecyclerView.setAdapter(myRewardsAdapter);

        if(DBQueries.rewardsModels.size() == 0){
            DBQueries.LoadRewards(getContext(), loadingDialog, true);
        }else {
            loadingDialog.dismiss();
        }
        myRewardsAdapter.notifyDataSetChanged();



        return view;
    }
}
