package com.example.chamandryfruits;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.chamandryfruits.HomeFragment.swipeRefreshLayout;
import static com.example.chamandryfruits.ProductDetailsActivity.initialRating;
import static com.example.chamandryfruits.ProductDetailsActivity.productId;
import static com.example.chamandryfruits.ProductDetailsActivity.runningCartQuery;
import static com.example.chamandryfruits.ProductDetailsActivity.runningRatingQuery;
import static com.example.chamandryfruits.ProductDetailsActivity.runningWishListQuery;

public class DBQueries {


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModels = new ArrayList<>();


    public static List<List<HomePageModel>> lists = new ArrayList<>();                      ///lists to store the home page model lists for all the categories
    public static List<String> loadedCategoriesNames = new ArrayList<>();                   ///category names to access the categories it is a reference for the main list

    public static List<String> wishList = new ArrayList<>();
    public static List<WishListModel> wishListModels = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModels = new ArrayList<>();

    public static List<AddressesModel> addressesModels = new ArrayList<>();
    public static int selectedAddress = -1;


    public static boolean createdReceipt = false;

    public static void LoadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categoryModels.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                categoryModels.add(new CategoryModel(Objects.requireNonNull(documentSnapshot.get("icon")).toString(), Objects.requireNonNull(documentSnapshot.get("categoryName")).toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModels);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            Log.d("into category error", error);
                        }
                    }
                });


    }

    public static void LoadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName) {
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName)
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                long view_type = (long) documentSnapshot.get("view_type");
                                if (view_type == 0) {
                                    List<SliderModel> sliderModels = new ArrayList<>();
                                    long noOfBanners = (long) documentSnapshot.get("no_of_banners");
                                    for (long i = 1; i < noOfBanners + 1; i++) {
                                        sliderModels.add(new SliderModel(Objects.requireNonNull(documentSnapshot.get("banner_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("banner_" + i + "_background")).toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModels));
                                } else if (view_type == 1) {
                                    lists.get(index).add(new HomePageModel(1, Objects.requireNonNull(documentSnapshot.get("strip_ad_banner")).toString(),
                                            Objects.requireNonNull(documentSnapshot.get("background")).toString()));
                                } else if (view_type == 2) {

                                    List<WishListModel> viewAllProductList = new ArrayList<>();

                                    List<HorizontalProductScrollModel> horizontalProductScrollModels = new ArrayList<>();
                                    long noOfProducts = (long) documentSnapshot.get("no_of_products");
                                    for (long i = 1; i < noOfProducts + 1; i++) {
                                        horizontalProductScrollModels.add(new HorizontalProductScrollModel(Objects.requireNonNull(documentSnapshot.get("product_ID_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_image_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_title_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_subtitle_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_price_" + i)).toString()));

                                        viewAllProductList.add(new WishListModel(Objects.requireNonNull(documentSnapshot.get("product_ID_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_image_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_full_title_" + i)).toString(),
                                                (long) Objects.requireNonNull(documentSnapshot.get("free_coupons_" + i)),
                                                Objects.requireNonNull(documentSnapshot.get("average_ratting_" + i)).toString(),
                                                (long) Objects.requireNonNull(documentSnapshot.get("total_ratings_" + i)),
                                                Objects.requireNonNull(documentSnapshot.get("product_price_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("cutted_price_" + i)).toString(),
                                                (boolean) Objects.requireNonNull(documentSnapshot.get("COD_" + i))));

                                    }
                                    lists.get(index).add(new HomePageModel(2, Objects.requireNonNull(documentSnapshot.get("layout_title")).toString(), Objects.requireNonNull(documentSnapshot.get("layout_background")).toString(), horizontalProductScrollModels, viewAllProductList));

                                } else if (view_type == 3) {
                                    List<HorizontalProductScrollModel> gridProductScrollModels = new ArrayList<>();
                                    long noOfProducts = (long) documentSnapshot.get("no_of_products");
                                    for (long i = 1; i < noOfProducts + 1; i++) {
                                        gridProductScrollModels.add(new HorizontalProductScrollModel(Objects.requireNonNull(documentSnapshot.get("product_ID_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_image_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_title_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_subtitle_" + i)).toString(),
                                                Objects.requireNonNull(documentSnapshot.get("product_price_" + i)).toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(3, Objects.requireNonNull(documentSnapshot.get("layout_title")).toString(), Objects.requireNonNull(documentSnapshot.get("layout_background")).toString(), gridProductScrollModels));
                                } else {
                                    return;
                                }
                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            Log.d("into category error", error);
                        }
                    }
                });


    }

    public static void LoadWishList(final Context context, final Dialog dialog, final boolean loadProductData) {
        wishList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long i = 0; i < (long) task.getResult().get("list_size"); i++) {
                        wishList.add(Objects.requireNonNull(task.getResult().get("product_ID_" + i)).toString());
                        if (DBQueries.wishList.contains(ProductDetailsActivity.productId)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;
                            if (ProductDetailsActivity.addToWishListButton != null) {
                                ProductDetailsActivity.addToWishListButton.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                            }
                        } else {
                            if (ProductDetailsActivity.addToWishListButton != null) {
                                ProductDetailsActivity.addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9f9f9f")));
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }
                        if (loadProductData) {
                            wishListModels.clear();
                            final String productId = Objects.requireNonNull(task.getResult().get("product_ID_" + i)).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        wishListModels.add(new WishListModel(productId,
                                                Objects.requireNonNull(task.getResult().get("product_image_1")).toString(),
                                                Objects.requireNonNull(task.getResult().get("product_title")).toString(),
                                                (long) Objects.requireNonNull(task.getResult().get("free_coupons")),
                                                Objects.requireNonNull(task.getResult().get("average_rating")).toString(),
                                                (long) Objects.requireNonNull(task.getResult().get("total_ratings")),
                                                Objects.requireNonNull(task.getResult().get("product_price")).toString(),
                                                Objects.requireNonNull(task.getResult().get("cutted_price")).toString(),
                                                (boolean) Objects.requireNonNull(task.getResult().get("COD"))));
                                        MyWishListFragment.wishListAdapter.notifyDataSetChanged();
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }

    public static void RemoveFormWishList(final int index, final Context context) {
        final String removedProductId = wishList.get(index);
        wishList.remove(index);
        Map<String, Object> updateWishList = new HashMap<>();
        for (int i = 0; i < wishList.size(); i++) {
            updateWishList.put("product_ID_" + i, wishList.get(i));
        }
        updateWishList.put("list_size", (long) wishList.size());
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .set(updateWishList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (wishListModels.size() != 0) {
                        wishListModels.remove(index);
                        if (MyWishListFragment.wishListAdapter != null) {
                            MyWishListFragment.wishListAdapter.notifyDataSetChanged();
                        }
                    }
                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    if (ProductDetailsActivity.addToWishListButton != null) {
                        ProductDetailsActivity.addToWishListButton.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                    }
                    wishList.add(index, removedProductId);
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                /*
                if(ProductDetailsActivity.addToWishListButton != null) {
                    ProductDetailsActivity.addToWishListButton.setEnabled(true);
                }
                 */
                runningWishListQuery = false;

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void LoadRatingList(final Context context) {
        myRating.clear();
        myRatedIds.clear();
        if (!runningRatingQuery) {
            runningRatingQuery = true;
            firebaseFirestore.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (long i = 0; i < (long) Objects.requireNonNull(task.getResult()).get("list_size"); i++) {
                            myRatedIds.add(Objects.requireNonNull(task.getResult().get("product_ID_" + i)).toString());
                            myRating.add((long) task.getResult().get("rating_" + i));

                            if (Objects.requireNonNull(task.getResult().get("product_ID_" + i)).toString().equals(productId)) {
                                initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + i))) - 1;
                                if (ProductDetailsActivity.rateNowContainer != null) {
                                    ProductDetailsActivity.SetRating(initialRating);
                                }
                            }
                        }
                    } else {
                        String error = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    runningRatingQuery = false;
                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void LoadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount) {
        if (cartList.size() > 0 && createdReceipt) {
            createdReceipt = false;
        }
        cartList.clear();
        firebaseFirestore.collection("USERS").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (long i = 0; i < (long) task.getResult().get("list_size"); i++) {
                        cartList.add(Objects.requireNonNull(task.getResult().get("product_ID_" + i)).toString());
                        if (DBQueries.cartList.contains(ProductDetailsActivity.productId)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = true;
                        } else {
                            ProductDetailsActivity.ALREADY_ADDED_TO_CART = false;
                        }
                        if (loadProductData) {
                            //cartItemModels.clear();
                            final String productId = Objects.requireNonNull(task.getResult().get("product_ID_" + i)).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int index = 0;
                                        if (cartList.size() >= 2) {
                                            index = cartList.size() - 2;
                                        }
                                        cartItemModels.add(index, new CartItemModel(CartItemModel.CART_ITEM, productId,
                                                Objects.requireNonNull(task.getResult().get("product_image_1")).toString(),
                                                Objects.requireNonNull(task.getResult().get("product_title")).toString(),
                                                (long) 1,
                                                Objects.requireNonNull(task.getResult().get("product_price")).toString(),
                                                (long) Objects.requireNonNull(task.getResult().get("free_coupons")),
                                                (long) 1,
                                                Objects.requireNonNull(task.getResult().get("cutted_price")).toString(),
                                                (long) 1));

                                        if (cartList.size() == 1 && !createdReceipt) {
                                            createdReceipt = true;
                                            cartItemModels.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                        }
                                        if (cartList.size() == 0) {
                                            cartItemModels.clear();
                                        }
                                        if (MyCartFragment.cartAdapter != null) {
                                            MyCartFragment.cartAdapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    if (cartList.size() != 0) {
                        badgeCount.setVisibility(View.VISIBLE);
                    } else {
                        badgeCount.setVisibility(View.INVISIBLE);
                    }

                    if (DBQueries.cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(DBQueries.cartList.size()));
                    } else {
                        badgeCount.setText("99");
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }

    public static void RemoveFormCart(final int index, final Context context) {
        final String removedProductId = cartList.get(index);
        cartList.remove(index);
        Map<String, Object> updateCartList = new HashMap<>();
        for (int i = 0; i < cartList.size(); i++) {
            updateCartList.put("product_ID_" + i, cartList.get(i));
        }
        updateCartList.put("list_size", (long) cartList.size());
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (cartItemModels.size() != 0) {
                        cartItemModels.remove(index);
                        if (MyCartFragment.cartAdapter != null) {
                            MyCartFragment.cartAdapter.notifyDataSetChanged();
                        }
                    }

                    if (cartList.size() == 0) {
                        cartItemModels.clear();
                        createdReceipt = false;
                    }
                    Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    cartList.add(index, removedProductId);
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                runningCartQuery = false;
            }
        });
    }

    public static void LoadAddresses(final Context context, final Dialog loadingDialog) {
        addressesModels.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Intent deliveryIntent;
                    if ((long) task.getResult().get("list_size") == 0) {

                        deliveryIntent = new Intent(context, AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT", "deliveryIntent");
                    } else {
                        for (long i = 1; i < (long) task.getResult().get("list_size") + 1; i++) {
                            addressesModels.add(new AddressesModel(task.getResult().get("fullname_" + i).toString()
                                    , task.getResult().get("address_" + i).toString()
                                    , task.getResult().get("pincode_" + i).toString()
                                    , (boolean) task.getResult().get("selected_" + i)));
                            if ((boolean) task.getResult().get("selected_" + i)) {
                                selectedAddress = Integer.parseInt(String.valueOf(i - 1));
                            }
                        }

                        deliveryIntent = new Intent(context, DeliveryActivity.class);

                    }
                    Objects.requireNonNull(context).startActivity(deliveryIntent);
                } else {
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }
        });

    }

    public static void clearData() {
        categoryModels.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishListModels.clear();
        wishList.clear();
        cartList.clear();
        cartItemModels.clear();
    }


}
