package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {


    private ViewPager productImagesViewPager;
    private TabLayout viewPagerIndicator;
    private FloatingActionButton addToWishListButton;
    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTabLayout;

    ////////rating layout

    private LinearLayout rateNowContainer;

    ////////rating layout

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewpager);
        viewPagerIndicator = findViewById(R.id.viewpager_indicator);
        addToWishListButton = findViewById(R.id.add_to_wishlist_button);
        productDetailsViewpager = findViewById(R.id.product_details_viewpager);
        productDetailsTabLayout = findViewById(R.id.product_details_tab_layout);

        List<Integer> productImages = new ArrayList<>();
        productImages.add(R.mipmap.phone_image);
        productImages.add(R.mipmap.book);
        productImages.add(R.mipmap.handfree);
        productImages.add(R.mipmap.sofa);

        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
        productImagesViewPager.setAdapter(productImagesAdapter);

        viewPagerIndicator.setupWithViewPager(productImagesViewPager,true);


        addToWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ALREADY_ADDED_TO_WISHLIST){
                    ALREADY_ADDED_TO_WISHLIST = false;
                    addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9f9f9f")));
                }
                else {
                    ALREADY_ADDED_TO_WISHLIST = true;
                    addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                }
            }
        });

        ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter(getSupportFragmentManager(),0,productDetailsTabLayout.getTabCount());
        productDetailsViewpager.setAdapter(productDetailsAdapter);
        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        ////////rating layout

        rateNowContainer = findViewById(R.id.ratenow_container);

        for(int i=0; i<rateNowContainer.getChildCount(); i++){
            final int starPosition = i;
            rateNowContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    SetRating(starPosition);
                }
            });
        }

        ////////rating layout




    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SetRating(int starPosition){
        for(int i=0; i<rateNowContainer.getChildCount(); i++){
            ImageView starButton = (ImageView) rateNowContainer.getChildAt(i);
            starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if(i <= starPosition){
                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        else if(id == R.id.main_search_icon){
            return true;
        }
        else if(id == R.id.main_cart_icon){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

}
