package com.example.chamandryfruits;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.chamandryfruits.DBQueries.lists;
import static com.example.chamandryfruits.DBQueries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {

    HomePageAdapter homePageAdapter;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();                      /////fake list to improve ui until the data is retrieved from the data base
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        RecyclerView categoryRecyclerView = findViewById(R.id.category_recyclerView);


        LinearLayoutManager testingLinearLayout = new LinearLayoutManager(this);
        testingLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLinearLayout);

        homePageAdapter = new HomePageAdapter(homePageModelFakeList);


        int listPosition = 0;
        for (int i = 0; i < loadedCategoriesNames.size(); i++) {
            if (loadedCategoriesNames.get(i).equalsIgnoreCase(title)) {
                listPosition = i;
            }
        }
        if (listPosition == 0) {
            assert title != null;
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            DBQueries.LoadFragmentData(categoryRecyclerView, this, loadedCategoriesNames.size() - 1, title);
        } else {
            homePageAdapter = new HomePageAdapter(lists.get(listPosition));
            homePageAdapter.notifyDataSetChanged();
        }

        categoryRecyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();

        ////home page fake list
        List<SliderModel> sliderModelsFakeList = new ArrayList<>();
        sliderModelsFakeList.add(new SliderModel("null", "#ffffff"));
        sliderModelsFakeList.add(new SliderModel("null", "#ffffff"));
        sliderModelsFakeList.add(new SliderModel("null", "#ffffff"));
        sliderModelsFakeList.add(new SliderModel("null", "#ffffff"));
        sliderModelsFakeList.add(new SliderModel("null", "#ffffff"));


        List<HorizontalProductScrollModel> horizontalProductScrollModelsFakeList = new ArrayList<>();
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelsFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));

        homePageModelFakeList.add(new HomePageModel(0, sliderModelsFakeList));
        homePageModelFakeList.add(new HomePageModel(1, "", "#ffffff"));
        homePageModelFakeList.add(new HomePageModel(2, "", "#ffffff", horizontalProductScrollModelsFakeList, new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3, "", "#ffffff", horizontalProductScrollModelsFakeList));

        ////home page fake list


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
