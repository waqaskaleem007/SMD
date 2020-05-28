package com.example.chamandryfruits;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
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
import java.util.List;
import java.util.Objects;

import static com.example.chamandryfruits.HomeFragment.swipeRefreshLayout;

public class DBQueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModels = new ArrayList<>();


    public static List<List<HomePageModel>> lists = new ArrayList<>();                      ///lists to store the home page model lists for all the categories
    public static List<String> loadedCategoriesNames = new ArrayList<>();                   ///category names to access the categories it is a reference for the main list

    public static List<String> wishList = new ArrayList<>();

    public static void LoadCategories(final RecyclerView categoryRecyclerView, final Context context) {

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

                                        viewAllProductList.add(new WishListModel(Objects.requireNonNull(documentSnapshot.get("product_image_" + i)).toString(),
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

    public static void LoadWishList(final Context context, final Dialog dialog) {
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    for (long i = 0; i < (long) task.getResult().get("list_size"); i++) {
                        wishList.add(Objects.requireNonNull(task.getResult().get("product_ID_" + i)).toString());

                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
