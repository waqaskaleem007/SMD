package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.chamandryfruits.Home.showCart;

public class ProductDetailsActivity extends AppCompatActivity {


    private ViewPager productImagesViewPager;
    private TabLayout viewPagerIndicator;
    private FloatingActionButton addToWishListButton;
    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTabLayout;
    private Button couponRedeemButton;

    ////coupon Dialog
    public static TextView couponTitle;
    public static TextView couponBody;
    public static TextView couponExpiryDate;
    private static RecyclerView couponsRecyclerView;
    private static LinearLayout selectedCoupon;
    ////coupon Dialog


    ////////rating layout

    private LinearLayout rateNowContainer;

    ////////rating layout

    private Button buyNowButton;

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

        buyNowButton = findViewById(R.id.buy_now_button);
        couponRedeemButton = findViewById(R.id.coupon_reedemption_button);

        List<Integer> productImages = new ArrayList<>();
        productImages.add(R.mipmap.phone_image);
        productImages.add(R.mipmap.book);
        productImages.add(R.mipmap.handfree);
        productImages.add(R.mipmap.sofa);

        ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
        productImagesViewPager.setAdapter(productImagesAdapter);

        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);


        addToWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ALREADY_ADDED_TO_WISHLIST) {
                    ALREADY_ADDED_TO_WISHLIST = false;
                    addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9f9f9f")));
                } else {
                    ALREADY_ADDED_TO_WISHLIST = true;
                    addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                }
            }
        });

        final ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter(getSupportFragmentManager(), 0, productDetailsTabLayout.getTabCount());
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

        for (int i = 0; i < rateNowContainer.getChildCount(); i++) {
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

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                startActivity(deliveryIntent);
            }
        });

        ///coupon Dialog
        final Dialog checkCouponPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCouponPriceDialog.setContentView(R.layout.coupon_redeem_dialog);
        checkCouponPriceDialog.setCancelable(true);
        checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView toggleRecyclerCouponView = checkCouponPriceDialog.findViewById(R.id.toggle_recycler_view);
        couponsRecyclerView = checkCouponPriceDialog.findViewById(R.id.coupons_recyclerView);
        selectedCoupon = checkCouponPriceDialog.findViewById(R.id.selected_coupon);

        couponTitle = checkCouponPriceDialog.findViewById(R.id.rewards_coupon_title);
        couponBody = checkCouponPriceDialog.findViewById(R.id.rewards_coupon_body);
        couponExpiryDate = checkCouponPriceDialog.findViewById(R.id.rewards_coupon_validity);

        TextView originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
        TextView discountedPrice = checkCouponPriceDialog.findViewById(R.id.disconunted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        couponsRecyclerView.setLayoutManager(layoutManager);

        List<RewardsModel> rewardsModels = new ArrayList<>();
        rewardsModels.add(new RewardsModel("Discount", "till 24th june 2020", "50% discount on each item"));
        rewardsModels.add(new RewardsModel("Free", "till 25th june 2020", "Get 20% off on any product above Rs.500/- and below Rs.2500/-"));
        rewardsModels.add(new RewardsModel("Discount", "till 26th june 2020", "50% discount on each item"));
        rewardsModels.add(new RewardsModel("Free", "till 27th june 2020", "Get 20% off on any product above Rs.5500/- and below Rs.9500/-"));
        rewardsModels.add(new RewardsModel("Cashback", "till 28th june 2020", "50% discount on each item"));
        rewardsModels.add(new RewardsModel("Discount", "till 29th june 2020", "Get 20% off on any product above Rs.1500/- and below Rs.3500/-"));
        rewardsModels.add(new RewardsModel("Cashback", "till 30th june 2020", "50% discount on each item"));
        rewardsModels.add(new RewardsModel("Discount", "till 31th june 2020", "Get 20% off on any product above Rs.5200/- and below Rs.2500/-"));
        rewardsModels.add(new RewardsModel("Free", "till 1st june 2020", "Buy one get one free"));
        rewardsModels.add(new RewardsModel("Discount", "till 4th june 2020", "Get 20% off on any product above Rs.5300/- and below Rs.25030/-"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardsModels, true);
        couponsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerCouponView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogRecyclerView();
            }
        });

        ///coupon Dialog

        couponRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkCouponPriceDialog.show();
            }
        });

    }

    public static void ShowDialogRecyclerView() {
        if (couponsRecyclerView.getVisibility() == View.GONE) {
            couponsRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupon.setVisibility(View.GONE);
        } else {
            couponsRecyclerView.setVisibility(View.GONE);
            selectedCoupon.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SetRating(int starPosition) {
        for (int i = 0; i < rateNowContainer.getChildCount(); i++) {
            ImageView starButton = (ImageView) rateNowContainer.getChildAt(i);
            starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (i <= starPosition) {
                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            Intent cartIntent = new Intent(ProductDetailsActivity.this, Home.class);
            showCart = true;
            startActivity(cartIntent);
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
