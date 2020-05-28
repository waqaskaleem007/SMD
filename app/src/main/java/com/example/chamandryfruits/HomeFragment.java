package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.chamandryfruits.DBQueries.categoryModels;
import static com.example.chamandryfruits.DBQueries.lists;
import static com.example.chamandryfruits.DBQueries.loadedCategoriesNames;
import static com.example.chamandryfruits.Home.drawer;
import static com.example.chamandryfruits.Home.toolbar;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelsFakeList = new ArrayList<>();                     /////fake list to improve ui until the data is retrieved from the data base
    private RecyclerView homePageRecyclerView;
    private HomePageAdapter homePageAdapter;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();                      /////fake list to improve ui until the data is retrieved from the data base
    private ImageView noInternetConnection;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private Button retryButton;

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
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));
        connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        homePageRecyclerView = view.findViewById(R.id.homePage_recyclerView);

        assert connectivityManager != null;
        networkInfo = connectivityManager.getActiveNetworkInfo();

        recyclerView = view.findViewById(R.id.category_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        retryButton = view.findViewById(R.id.retry_button);

        LinearLayoutManager testingLinearLayout = new LinearLayoutManager(getContext());
        testingLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        homePageRecyclerView.setLayoutManager(testingLinearLayout);


        ///////categories fake list
        categoryModelsFakeList.add(new CategoryModel("null", ""));
        categoryModelsFakeList.add(new CategoryModel("", ""));
        categoryModelsFakeList.add(new CategoryModel("", ""));
        categoryModelsFakeList.add(new CategoryModel("", ""));
        categoryModelsFakeList.add(new CategoryModel("", ""));
        categoryModelsFakeList.add(new CategoryModel("", ""));
        categoryModelsFakeList.add(new CategoryModel("", ""));
        categoryModelsFakeList.add(new CategoryModel("", ""));

        ///////categories fake list

        ////home page fake list
        List<SliderModel> sliderModelsFakeList = new ArrayList<>();
        sliderModelsFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelsFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelsFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelsFakeList.add(new SliderModel("null", "#dfdfdf"));
        sliderModelsFakeList.add(new SliderModel("null", "#dfdfdf"));


        List<HorizontalProductScrollModel> horizontalProductScrollModelsFakeList = new ArrayList<>();
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));

        homePageModelFakeList.add(new HomePageModel(0, sliderModelsFakeList));
        homePageModelFakeList.add(new HomePageModel(1, "", "#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2, "", "#dfdfdf", horizontalProductScrollModelsFakeList, new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3, "", "#dfdfdf", horizontalProductScrollModelsFakeList));

        ////home page fake list
        categoryAdapter = new CategoryAdapter(categoryModelsFakeList);
        homePageAdapter = new HomePageAdapter(homePageModelFakeList);

        if (networkInfo != null && networkInfo.isConnected()) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toolbar.setNavigationIcon(R.mipmap.navbar_mobile);
            noInternetConnection.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);

            if (categoryModels.size() == 0) {
                DBQueries.LoadCategories(recyclerView, getContext());
            } else {
                categoryAdapter = new CategoryAdapter(categoryModels);
                categoryAdapter.notifyDataSetChanged();                         //to reset the data in the adapter if the adapter is not null for the 2nd time
            }

            recyclerView.setAdapter(categoryAdapter);

            if (lists.size() == 0) {
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());
                DBQueries.LoadFragmentData(homePageRecyclerView, getContext(), 0, "Home");
            } else {
                homePageAdapter = new HomePageAdapter(lists.get(0));
                homePageAdapter.notifyDataSetChanged();                         //to reset the data in the adapter if the adapter is not null for the 2nd time
            }
            homePageRecyclerView.setAdapter(homePageAdapter);

        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toolbar.setNavigationIcon(null);
            drawer.closeDrawer(GravityCompat.START);
            retryButton.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.no_internet_connection).into(noInternetConnection);        //no internet connection gif
            noInternetConnection.setVisibility(View.VISIBLE);
        }

        //////swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });
        //////swipe refresh layout
        retryButton.setOnClickListener(new View.OnClickListener() {                 //to reload the page after the internet establishes
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();
        categoryModels.clear();
        lists.clear();
        loadedCategoriesNames.clear();

        if (networkInfo != null && networkInfo.isConnected()) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toolbar.setNavigationIcon(R.mipmap.navbar_mobile);
            noInternetConnection.setVisibility(View.GONE);
            retryButton.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            homePageRecyclerView.setVisibility(View.VISIBLE);
            categoryAdapter = new CategoryAdapter(categoryModelsFakeList);
            homePageAdapter = new HomePageAdapter(homePageModelFakeList);
            recyclerView.setAdapter(categoryAdapter);
            DBQueries.LoadCategories(recyclerView, getContext());
            homePageRecyclerView.setAdapter(homePageAdapter);


            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            DBQueries.LoadFragmentData(homePageRecyclerView, getContext(), 0, "Home");

        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
            toolbar.setNavigationIcon(null);
            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(getContext(),"No Internet Connection!",Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            homePageRecyclerView.setVisibility(View.GONE);
            Glide.with(Objects.requireNonNull(getContext())).load(R.drawable.no_internet_connection).into(noInternetConnection);        //no internet connection gif
            noInternetConnection.setVisibility(View.VISIBLE);
            retryButton.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }

    }




}
