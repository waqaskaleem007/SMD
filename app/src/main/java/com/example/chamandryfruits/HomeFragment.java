package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private RecyclerView testing;
    private List<CategoryModel> categoryModels;
    private FirebaseFirestore firebaseFirestore;

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
        List<SliderModel> sliderModels = new ArrayList<SliderModel>();

        sliderModels.add(new SliderModel(R.mipmap.banner, "#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.banner_1, "#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.banner_6, "#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.banner_5, "#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.banner_4, "#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.banner_3, "#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.banner_2, "#077AE4"));
        ////Banner slider Code


        ///Horizontal view Layout

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

        ///Horizontal view Layout

        /////Testing recycler view
        testing = view.findViewById(R.id.homePage_recyclerView);
        LinearLayoutManager testingLinearLayout = new LinearLayoutManager(getContext());
        testingLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLinearLayout);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(sliderModels, 0));
        homePageModelList.add(new HomePageModel(1, R.drawable.strip_ad, "#000000"));
        homePageModelList.add(new HomePageModel(2, "Deals of the day", horizontalProductScrollModels));
        homePageModelList.add(new HomePageModel(3, "Trending on store", horizontalProductScrollModels));

        homePageModelList.add(new HomePageModel(sliderModels, 0));
        homePageModelList.add(new HomePageModel(1, R.drawable.strip_ad, "#000000"));
        homePageModelList.add(new HomePageModel(2, "Deals of the day", horizontalProductScrollModels));
        //homePageModelList.add(new HomePageModel(3, "Trending on store", horizontalProductScrollModels));

        homePageModelList.add(new HomePageModel(sliderModels, 0));
        homePageModelList.add(new HomePageModel(1, R.drawable.strip_ad, "#000000"));
        homePageModelList.add(new HomePageModel(2, "meals of the day", horizontalProductScrollModels));
//        homePageModelList.add(new HomePageModel(3, "Super Trending on store", horizontalProductScrollModels));

        HomePageAdapter homePageAdapter = new HomePageAdapter(homePageModelList);
        testing.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();

        /////Testing recycler view


        return view;
    }

}
