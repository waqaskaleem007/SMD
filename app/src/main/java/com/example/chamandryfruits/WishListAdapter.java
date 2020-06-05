package com.example.chamandryfruits;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private boolean fromSearch;
    List<WishListModel> wishListModels = new ArrayList<>();
    private boolean wishList;
    private int lastPosition = -1;

    public WishListAdapter(List<WishListModel> wishListModels, boolean wishList) {
        this.wishListModels = wishListModels;
        this.wishList = wishList;
    }

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public List<WishListModel> getWishListModels() {
        return wishListModels;
    }

    public void setWishListModels(List<WishListModel> wishListModels) {
        this.wishListModels = wishListModels;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        String productImage = wishListModels.get(position).getProductImage();
        String productTitle = wishListModels.get(position).getProductTitle();
        long freeCoupons = wishListModels.get(position).getFreeCoupons();
        String rating = wishListModels.get(position).getRating();
        long totalRatings = wishListModels.get(position).getTotalRatings();
        String productPrice = wishListModels.get(position).getProductPrice();
        String cuttedPrice = wishListModels.get(position).getCuttedPrice();
        boolean paymentMethod = wishListModels.get(position).isCod();
        boolean inStock = wishListModels.get(position).isInStock();
        String productId =  wishListModels.get(position).getProductId();
        holder.setData(productImage, productTitle, freeCoupons, rating, totalRatings, productPrice, cuttedPrice, paymentMethod, position, productId, inStock);
        if(lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupons;
        private ImageView couponIcon;
        private TextView ratings;
        private TextView totalRatings;
        private TextView productPrice;
        private TextView cuttedPrice;
        private View priceCut;
        private TextView paymentMethod;
        private ImageButton deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.wishist_product_image);
            productTitle = itemView.findViewById(R.id.wishist_product_title);
            freeCoupons = itemView.findViewById(R.id.wishist_product_free_coupons);
            couponIcon = itemView.findViewById(R.id.wishist_product_coupen_icon);
            ratings = itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings = itemView.findViewById(R.id.wishlist_total_ratings);
            productPrice = itemView.findViewById(R.id.wishlist_product_price);
            cuttedPrice = itemView.findViewById(R.id.wishlist_product_cuttedprice);
            priceCut = itemView.findViewById(R.id.price_cut_divider);
            paymentMethod = itemView.findViewById(R.id.wishlist_product_payment_method);
            deleteBtn = itemView.findViewById(R.id.wishlist_product_delete_button);
        }

        private void setData(String resource, String title, long freeCouponsNo, String avgRate, long totalRatingsNo, String price, String cPrice, boolean payMethod, final int index, final String productId, boolean inStock) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.img_placeholder)).into(productImage);
            productTitle.setText(title);

            if (freeCouponsNo != 0 && inStock) {
                couponIcon.setVisibility(View.VISIBLE);
                if (freeCouponsNo == 1) {
                    freeCoupons.setText("Free " + freeCouponsNo + " coupons");
                } else {
                    freeCoupons.setText("Free " + freeCouponsNo + " coupons");
                }
            } else {
                couponIcon.setVisibility(View.INVISIBLE);
                freeCoupons.setVisibility(View.INVISIBLE);
            }
            LinearLayout linearLayout = (LinearLayout) ratings.getParent();
            if(inStock){
                ratings.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                ratings.setText(avgRate);
                totalRatings.setText("(" + totalRatingsNo + ")" + "Ratings");
                productPrice.setText("Rs." + price + "/-");
                cuttedPrice.setText("Rs." + cPrice + "/-");
                if (payMethod) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            }else {

                linearLayout.setVisibility(View.INVISIBLE);
                ratings.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cuttedPrice.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);
            }

            if (wishList) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailsActivity.runningWishListQuery) {
                        ProductDetailsActivity.runningWishListQuery = true;
                        DBQueries.RemoveFormWishList(index, itemView.getContext());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fromSearch){
                        ProductDetailsActivity.fromSearch = true;
                    }
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("PRODUCT_ID",productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }
    }
}
