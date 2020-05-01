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

public class CategoryActivity extends AppCompatActivity {


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

        ////Banner slider Code
        List<SliderModel> sliderModels = new ArrayList<SliderModel>();
        sliderModels.add(new SliderModel(R.mipmap.home_icon,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.my_orders,"#077AE4"));

        sliderModels.add(new SliderModel(R.mipmap.banner,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.group_375,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.add_user,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.bell,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.custom_error_icon,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.forgot_password_image,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.app_icon,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.logo,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.cart_black,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.home_icon,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.my_orders,"#077AE4"));

        sliderModels.add(new SliderModel(R.mipmap.banner,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.group_375,"#077AE4"));

        ////Banner slider Code


        ///Horizontal view Layout

        List<HorizontalProductScrollModel> horizontalProductScrollModels = new ArrayList<>();
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.phone_image,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.add_user,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.cart_black,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.logo,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.bell,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.add_user_2,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.forgot_password_image,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.ic_launcher,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));

        ///Horizontal view Layout

        /////Testing recycler view

        LinearLayoutManager testingLinearLayout = new LinearLayoutManager(this);
        testingLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(testingLinearLayout);

        List<HomePageModel> homePageModelList = new ArrayList<>();
        homePageModelList.add(new HomePageModel(sliderModels,0));
        homePageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
        homePageModelList.add(new HomePageModel(2, "Deals of the day", horizontalProductScrollModels));
        homePageModelList.add(new HomePageModel(3, "Trending on store", horizontalProductScrollModels));

        homePageModelList.add(new HomePageModel(sliderModels,0));
        homePageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
        homePageModelList.add(new HomePageModel(2, "Deals of the day", horizontalProductScrollModels));
        homePageModelList.add(new HomePageModel(3, "Trending on store", horizontalProductScrollModels));

        homePageModelList.add(new HomePageModel(sliderModels,0));
        homePageModelList.add(new HomePageModel(1,R.drawable.strip_ad,"#000000"));
        homePageModelList.add(new HomePageModel(2, "meals of the day", horizontalProductScrollModels));
        homePageModelList.add(new HomePageModel(3, "Super Trending on store", horizontalProductScrollModels));

        HomePageAdapter homePageAdapter = new HomePageAdapter(homePageModelList);
        categoryRecyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();


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
        if(id == R.id.main_search_icon){
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
