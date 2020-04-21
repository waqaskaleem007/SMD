package com.example.chamandryfruits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        recyclerView = view.findViewById(R.id.category_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
        categoryModels.add(new CategoryModel("link", "Home"));
        categoryModels.add(new CategoryModel("link", "Electronics"));
        categoryModels.add(new CategoryModel("link", "Furniture"));
        categoryModels.add(new CategoryModel("link", "Appliances"));
        categoryModels.add(new CategoryModel("link", "Fashion"));
        categoryModels.add(new CategoryModel("link", "Mobiles"));
        categoryModels.add(new CategoryModel("link", "Appliances"));
        categoryModels.add(new CategoryModel("link", "Fashion"));
        categoryModels.add(new CategoryModel("link", "Mobiles"));
        categoryModels.add(new CategoryModel("link", "Appliances"));
        categoryModels.add(new CategoryModel("link", "Fashion"));
        categoryModels.add(new CategoryModel("link", "Mobiles"));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModels);
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        return view;
    }
}
