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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
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


    public static boolean runningWishListQuery = false;
    public static boolean runningRatingQuery = false;


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

    public static FloatingActionButton addToWishListButton;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
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
    public static LinearLayout rateNowContainer;
    private TextView totalRatings;
    private LinearLayout ratingsNumberContainer;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    public static int initialRating;

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
    public static String productId;

    private DocumentSnapshot documentSnapshot;


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
        initialRating = -1;
        ///loading Dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loadind_progress_dialog);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////loading Dialog

        productId = Objects.requireNonNull(getIntent().getStringExtra("PRODUCT_ID"));
        final List<String> productImages = new ArrayList<>();
        firebaseFirestore.collection("PRODUCTS").document(productId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    for (long i = 1; i < (long) documentSnapshot.get("no_of_product_images") + 1; i++) {
                        productImages.add(Objects.requireNonNull(documentSnapshot.get("product_image_" + i)).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImagesViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(Objects.requireNonNull(documentSnapshot.get("product_title")).toString());
                    avgRatingMiniView.setText(Objects.requireNonNull(documentSnapshot.get("average_rating")).toString());
                    totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")" + "Ratings");
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

                    totalRatings.setText((long) documentSnapshot.get("total_ratings") + " Ratings");
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

                    if (currentUser != null) {
                        if (DBQueries.myRating.size() == 0) {
                            DBQueries.LoadRatingList(ProductDetailsActivity.this);
                        }
                        if (DBQueries.wishList.size() == 0) {
                            DBQueries.LoadWishList(ProductDetailsActivity.this, loadingDialog, false);
                        } else {
                            loadingDialog.dismiss();
                        }


                    } else {
                        loadingDialog.dismiss();
                    }

                    if (DBQueries.myRatedIds.contains(productId)) {
                        int index = DBQueries.myRatedIds.indexOf(productId);
                        initialRating = Integer.parseInt(String.valueOf(DBQueries.myRating.get(index))) - 1;
                        SetRating(initialRating);
                    }

                    if (DBQueries.wishList.contains(productId)) {
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                    } else {
                        addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9f9f9f")));
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
                    if (!runningWishListQuery) {
                        runningWishListQuery = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBQueries.wishList.indexOf(productId);
                            DBQueries.RemoveFormWishList(index, ProductDetailsActivity.this);
                            addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9f9f9f")));
                        } else {
                            addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBQueries.wishList.size()), productId);
                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> updateListSize = new HashMap<>();
                                        updateListSize.put("list_size", (long) (DBQueries.wishList.size() + 1));
                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                                .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (DBQueries.wishListModels.size() == 0) {
                                                        DBQueries.wishListModels.add(new WishListModel(productId
                                                                , Objects.requireNonNull(documentSnapshot.get("product_image_1")).toString(),
                                                                Objects.requireNonNull(documentSnapshot.get("product_title")).toString(),
                                                                (long) Objects.requireNonNull(documentSnapshot.get("free_coupons")),
                                                                Objects.requireNonNull(documentSnapshot.get("average_rating")).toString(),
                                                                (long) Objects.requireNonNull(documentSnapshot.get("total_ratings")),
                                                                Objects.requireNonNull(documentSnapshot.get("product_price")).toString(),
                                                                Objects.requireNonNull(documentSnapshot.get("cutted_price")).toString(),
                                                                (boolean) Objects.requireNonNull(documentSnapshot.get("COD"))));

                                                    }

                                                    ALREADY_ADDED_TO_WISHLIST = true;
                                                    addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                                    DBQueries.wishList.add(productId);
                                                    Toast.makeText(ProductDetailsActivity.this, "Added To WishList Successfully!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9f9f9f")));
                                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                runningWishListQuery = false;
                                            }
                                        });
                                    } else {
                                        runningWishListQuery = false;
                                        String error = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
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
                        if(starPosition != initialRating) {
                            if (!runningRatingQuery) {
                                runningRatingQuery = true;
                                SetRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBQueries.myRatedIds.contains(productId)) {

                                    TextView oldRating = (TextView) ratingsNumberContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNumberContainer.getChildAt(5 - starPosition - 1);


                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));


                                } else {
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    //productRating.put( + "_star", (long) documentSnapshot.get(1 + "_star" + 1));
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                }
                                firebaseFirestore.collection("PRODUCTS").document(productId)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myRating = new HashMap<>();
                                            if (DBQueries.myRatedIds.contains(productId)) {
                                                myRating.put("rating_" + DBQueries.myRatedIds.indexOf(productId), (long) starPosition + 1);

                                            } else {

                                                myRating.put("product_ID_" + DBQueries.myRatedIds.size(), productId);
                                                myRating.put("rating_" + DBQueries.myRatedIds.size(), (long) (starPosition + 1));
                                                myRating.put("list_size", (long) DBQueries.myRatedIds.size() + 1);
                                            }

                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        if (DBQueries.myRatedIds.contains(productId)) {
                                                            DBQueries.myRating.set(DBQueries.myRatedIds.indexOf(productId), (long) starPosition + 1);
                                                            TextView oldRating = (TextView) ratingsNumberContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingsNumberContainer.getChildAt(5 - starPosition - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                        } else {

                                                            DBQueries.myRatedIds.add(productId);
                                                            DBQueries.myRating.add((long) starPosition + 1);
                                                            TextView rating = (TextView) ratingsNumberContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + 1 + ")" + "Ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " Ratings");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));
                                                            Toast.makeText(ProductDetailsActivity.this, "Thank You For Rating", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int i = 0; i < 5; i++) {
                                                            TextView ratingFigures = (TextView) ratingsNumberContainer.getChildAt(i);
                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(i);
                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);

                                                            progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                                                        }
                                                        initialRating = starPosition;
                                                        averageRating.setText(calculateAverageRating((long) (0), true));
                                                        avgRatingMiniView.setText(calculateAverageRating((long) (0), true));
                                                        if (DBQueries.wishList.contains(productId) && DBQueries.wishListModels.size() != 0) {
                                                            int index = DBQueries.wishList.indexOf(productId);
                                                            DBQueries.wishListModels.get(index).setRating(averageRating.getText().toString());
                                                            DBQueries.wishListModels.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));

                                                        }

                                                    } else {
                                                        SetRating(initialRating);
                                                        String error = Objects.requireNonNull(task.getException()).getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                        Log.d("into error rating", error);
                                                    }
                                                    runningRatingQuery = false;
                                                }
                                            });
                                        } else {
                                            runningRatingQuery = false;
                                            SetRating(initialRating);
                                            String error = Objects.requireNonNull(task.getException()).getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            Log.d("into error rating", error);
                                        }
                                    }
                                });


                            }
                        }
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            couponRedemptionLayout.setVisibility(View.GONE);
        } else {
            couponRedemptionLayout.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (DBQueries.myRating.size() == 0) {
                DBQueries.LoadRatingList(ProductDetailsActivity.this);
            }
            if (DBQueries.wishList.size() == 0) {
                DBQueries.LoadWishList(ProductDetailsActivity.this, loadingDialog, false);
            } else {
                loadingDialog.dismiss();
            }

        } else {
            loadingDialog.dismiss();
        }
        if (DBQueries.myRatedIds.contains(productId)) {
            int index = DBQueries.myRatedIds.indexOf(productId);
            initialRating = Integer.parseInt(String.valueOf(DBQueries.myRating.get(index))) - 1;
            SetRating(initialRating);
        }

        if (DBQueries.wishList.contains(productId)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        } else {
            addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9f9f9f")));
            ALREADY_ADDED_TO_WISHLIST = false;
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
    public static void SetRating(int starPosition) {
        for (int i = 0; i < rateNowContainer.getChildCount(); i++) {
            ImageView starButton = (ImageView) rateNowContainer.getChildAt(i);
            starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (i <= starPosition) {
                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));

            }
        }
    }


    private String calculateAverageRating(Long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int i = 1; i < 6; i++) {
            TextView ratingNo = (TextView) ratingsNumberContainer.getChildAt(5 - i);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * i);
        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
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
