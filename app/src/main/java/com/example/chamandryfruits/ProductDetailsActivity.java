package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.chamandryfruits.Home.showCart;
import static com.example.chamandryfruits.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {


    private ViewPager productImagesViewPager;
    private TabLayout viewPagerIndicator;
    private TextView productTitle;
    private TextView avgRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;
    private TextView rewardTitle;
    private TextView rewardBody;

    private FloatingActionButton addToWishListButton;
    private static boolean ALREADY_ADDED_TO_WISHLIST = false;
    private Button couponRedeemButton;

    ////product description variables
    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailsTabLayout;
    private String productDescription;
    private String productOtherDetails;
    private TextView productOnlyDescriptionBody;
    private List<ProductSpecificationModel> productSpecificationModels = new ArrayList<>();
    private View productTabInclude;
    private View productDetailsOnlyInclude;

    ////product description variables


    ////////rating layout
    private LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNumberContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    ////////rating layout

    private Button buyNowButton;

    private FirebaseFirestore firebaseFirestore;
    ////coupon Dialog
    private LinearLayout couponRedemptionLayout;
    public static TextView couponTitle;
    public static TextView couponBody;
    public static TextView couponExpiryDate;
    private static RecyclerView couponsRecyclerView;
    private static LinearLayout selectedCoupon;
    ////coupon Dialog

    private Dialog loadingDialog;

    private Dialog signInDialog;
    private LinearLayout addToCart;

    private FirebaseUser currentUser;
    private String productId;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        firebaseFirestore = FirebaseFirestore.getInstance();
        productTitle = findViewById(R.id.product_title);
        avgRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_rating_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        codIndicator = findViewById(R.id.cod_idicator_imageview);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);

        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_containers);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productTabInclude = findViewById(R.id.product_description_include_layout);
        productDetailsOnlyInclude = findViewById(R.id.product_details_include_layout);

        productOnlyDescriptionBody = findViewById(R.id.product_details_body);

        totalRatings = findViewById(R.id.total_ratings);
        ratingsNumberContainer = findViewById(R.id.ratings_numbers_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        ratingsProgressBarContainer = findViewById(R.id.rating_progressbar_container);
        averageRating = findViewById(R.id.avg_rating);

        addToCart = findViewById(R.id.Add_to_cart_button);
        couponRedemptionLayout = findViewById(R.id.coupon_reedemption_layout);

        ///loading Dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loadind_progress_dialog);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        productId = Objects.requireNonNull(getIntent().getStringExtra("PRODUCT_ID"));
        final List<String> productImages = new ArrayList<>();
        firebaseFirestore.collection("PRODUCTS").document(productId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    for (long i = 1; i < (long) documentSnapshot.get("no_of_product_images") + 1; i++) {
                        productImages.add(Objects.requireNonNull(documentSnapshot.get("product_image_" + i)).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(Objects.requireNonNull(documentSnapshot.get("product_title")).toString());
                    avgRatingMiniView.setText(Objects.requireNonNull(documentSnapshot.get("average_rating")).toString());
                    totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")" + "Ratings");
                    productPrice.setText(Objects.requireNonNull("Rs." + documentSnapshot.get("product_price")).toString() + "/-");
                    cuttedPrice.setText(Objects.requireNonNull("Rs." + documentSnapshot.get("cutted_price")).toString() + "/-");
                    if ((boolean) documentSnapshot.get("COD")) {
                        tvCodIndicator.setVisibility(View.VISIBLE);
                        codIndicator.setVisibility(View.VISIBLE);
                    } else {
                        tvCodIndicator.setVisibility(View.GONE);
                        codIndicator.setVisibility(View.GONE);
                    }
                    rewardTitle.setText((long) documentSnapshot.get("free_coupons") + " " + documentSnapshot.get("free_coupon_title").toString());
                    rewardBody.setText(Objects.requireNonNull(documentSnapshot.get("free_coupon_body")).toString());
                    if ((boolean) documentSnapshot.get("use_tab_layout")) {
                        //productDetailsTabsContainer.setVisibility(View.VISIBLE);
                        //productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                        productTabInclude.setVisibility(View.VISIBLE);
                        productDetailsOnlyInclude.setVisibility(View.GONE);

                        productDescription = Objects.requireNonNull(documentSnapshot.get("product_description")).toString();

                        productOtherDetails = Objects.requireNonNull(documentSnapshot.get("product_other_details")).toString();
                        for (long i = 1; i < (long) documentSnapshot.get("total_spec_titles") + 1; i++) {
                            productSpecificationModels.add(new ProductSpecificationModel(0, Objects.requireNonNull(documentSnapshot.get("spec_title_" + i)).toString()));
                            for (long j = 1; j < (long) documentSnapshot.get("spec_title_" + i + "_total_fields") + 1; j++) {
                                productSpecificationModels.add(new ProductSpecificationModel(1, Objects.requireNonNull(documentSnapshot.get("spec_title_" + i + "_field_" + j + "_name")).toString(), Objects.requireNonNull(documentSnapshot.get("spec_title_" + i + "_field_" + j + "_value")).toString()));
                            }
                        }
                    } else {
                        //productDetailsTabsContainer.setVisibility(View.INVISIBLE);
                        //productDetailsOnlyContainer.setVisibility(View.INVISIBLE);
                        productTabInclude.setVisibility(View.GONE);
                        productDetailsOnlyInclude.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(Objects.requireNonNull(documentSnapshot.get("product_description")).toString());
                    }

                    totalRatings.setText((long) documentSnapshot.get("total_ratings") + "ratings");
                    for (int i = 0; i < 5; i++) {
                        TextView rating = (TextView) ratingsNumberContainer.getChildAt(i);
                        rating.setText(String.valueOf((long) Objects.requireNonNull(documentSnapshot.get(5 - i + "_star"))));
                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(i);
                        int maxProgress = Integer.parseInt((String.valueOf((long) documentSnapshot.get("total_ratings"))));
                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) Objects.requireNonNull(documentSnapshot.get(5 - i + "_star")))));
                    }
                    totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                    averageRating.setText(Objects.requireNonNull(documentSnapshot.get("average_rating")).toString());
                    final ProductDetailsAdapter productDetailsAdapter = new ProductDetailsAdapter(getSupportFragmentManager(), 0, productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModels);
                    productDetailsViewpager.setAdapter(productDetailsAdapter);

                    if (DBQueries.wishList.size() == 0) {
                        DBQueries.LoadWishList(ProductDetailsActivity.this, loadingDialog);
                    }else {
                        loadingDialog.dismiss();
                    }

                    if (DBQueries.wishList.contains(productId)) {
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                    } else {
                        ALREADY_ADDED_TO_WISHLIST = false;
                    }

                } else {
                    loadingDialog.dismiss();
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);



        addToWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    if (ALREADY_ADDED_TO_WISHLIST) {

                        ALREADY_ADDED_TO_WISHLIST = false;
                        addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9f9f9f")));
                    } else {
                        Map<String, Object> addProduct = new HashMap<>();
                        addProduct.put("product_ID_" + String.valueOf(DBQueries.wishList.size()), productId);
                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                .set(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> updateListSize = new HashMap<>();
                                    updateListSize.put("list_size", (long)(DBQueries.wishList.size() + 1));
                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                            .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                ALREADY_ADDED_TO_WISHLIST = true;
                                                addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                                DBQueries.wishList.add(productId);
                                                Toast.makeText(ProductDetailsActivity.this,"Added To WishList Successfully!",Toast.LENGTH_SHORT).show();
                                            } else {
                                                String error = Objects.requireNonNull(task.getException()).getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

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
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        SetRating(starPosition);
                    }
                }
            });
        }

        ////////rating layout

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    /////to be add to cart
                }
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

        /////sign in dialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        Objects.requireNonNull(signInDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button signInBtn = signInDialog.findViewById(R.id.dialog_sign_in_button);
        Button signUpBtn = signInDialog.findViewById(R.id.dialog_sign_up_button);

        final Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseButton = true;
                SignUpFragment.disableCloseButton = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseButton = true;
                SignUpFragment.disableCloseButton = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        ///sign in Dialog

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            couponRedemptionLayout.setVisibility(View.GONE);
        } else {
            couponRedemptionLayout.setVisibility(View.VISIBLE);
        }


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
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, Home.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
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
