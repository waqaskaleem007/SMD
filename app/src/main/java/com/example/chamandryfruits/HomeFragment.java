package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView homePageRecyclerView;
    private List<CategoryModel> categoryModels;
    private FirebaseFirestore firebaseFirestore;
    private HomePageAdapter homePageAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        recyclerView = view.findViewById(R.id.category_recyclerview);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        categoryModels = new ArrayList<CategoryModel>();

        final CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModels);
        recyclerView.setAdapter(categoryAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                categoryModels.add(new CategoryModel(Objects.requireNonNull(documentSnapshot.get("icon")).toString(), Objects.requireNonNull(documentSnapshot.get("categoryName")).toString()));
                            }
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            Log.d("into category error", error);
                        }
                    }
                });


        ////Banner slider Code


        ////Banner slider Code


        ///Horizontal view Layout
/*
        List<HorizontalProductScrollModel> horizontalProductScrollModels = new ArrayList<>();
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.phone_image, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.book, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.sofa, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.jacket, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.handfree, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.download, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.weights, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.weights, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.weights, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));

 */
        ///Horizontal view Layout

        /////Testing recycler view
        homePageRecyclerView = view.findViewById(R.id.homePage_recyclerView);
        LinearLayoutManager testingLinearLayout = new LinearLayoutManager(getContext());
        testingLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLinearLayout);
        final List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageAdapter = new HomePageAdapter(homePageModelList);
        homePageRecyclerView.setAdapter(homePageAdapter);

        firebaseFirestore.collection("CATEGORIES")
                .document("Home")
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("into the successful", "this is successful");
                    Log.d("into the for loop", task.getResult().toString());
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                        Log.d("into the for loop", "this is for loop");
                        long view_type = (long) documentSnapshot.get("view_type");
                        if (view_type == 0) {
                            Log.d("into the viewtype", "this is view type");
                            List<SliderModel> sliderModels = new ArrayList<>();
                            long noOfBanners = (long) documentSnapshot.get("no_of_banners");
                            for (long i = 1; i < noOfBanners + 1; i++) {
                                sliderModels.add(new SliderModel(documentSnapshot.get("banner_" + i).toString(),
                                        documentSnapshot.get("banner_" + i + "_background").toString()));
                            }
                            homePageModelList.add(new HomePageModel(0, sliderModels));
                        } else if (view_type == 1) {
                            Log.d("into the viewtype", "this is view type");
                            homePageModelList.add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString(),
                                    documentSnapshot.get("background").toString()));
                        } else if (view_type == 2) {
                        } else if (view_type == 3) {
                        } else {
                            return;
                        }
                    }
                    homePageAdapter.notifyDataSetChanged();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    Log.d("into category error", error);
                }
            }
        });


        /////Testing recycler view


        return view;
    }

}
