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
public class ProductSpecificationFragment extends Fragment {

    private RecyclerView productSpecificationRecyclerView;

    public ProductSpecificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_specification, container, false);

        productSpecificationRecyclerView = view.findViewById(R.id.product_specification_recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        productSpecificationRecyclerView.setLayoutManager(layoutManager);

        List<ProductSpecificationModel> productSpecificationModels = new ArrayList<>();
        productSpecificationModels.add(new ProductSpecificationModel(0, "General"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));

        productSpecificationModels.add(new ProductSpecificationModel(0, "Display"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));

        productSpecificationModels.add(new ProductSpecificationModel(0, "General"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));

        productSpecificationModels.add(new ProductSpecificationModel(0, "Display"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));
        productSpecificationModels.add(new ProductSpecificationModel(1, "RAM", "4GB"));



        ProductSpecificationAdapter productSpecificationAdapter = new ProductSpecificationAdapter(productSpecificationModels);
        productSpecificationRecyclerView.setAdapter(productSpecificationAdapter);
        productSpecificationAdapter.notifyDataSetChanged();

        return view;
    }
}
