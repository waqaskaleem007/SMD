package com.example.chamandryfruits;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    List<WishListModel> wishListModels = new ArrayList<>();
    private boolean wishList;

    public WishListAdapter(List<WishListModel> wishListModels, boolean wishList) {
        this.wishListModels = wishListModels;
        this.wishList = wishList;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.ViewHolder holder, int position) {
        int productImage = wishListModels.get(position).getProductImage();
        String productTitle = wishListModels.get(position).getProductTitle();
        int freeCoupons = wishListModels.get(position).getFreeCoupons();
        String rating = wishListModels.get(position).getRating();
        int totalRatings = wishListModels.get(position).getTotalRatings();
        String productPrice = wishListModels.get(position).getProductPrice();
        String cuttedPrice = wishListModels.get(position).getCuttedPrice();
        String paymentMethod = wishListModels.get(position).getPaymentMethod();

        holder.setData(productImage, productTitle, freeCoupons, rating, totalRatings, productPrice, cuttedPrice, paymentMethod);

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

        private void setData(int resource, String title, int freeCouponsNo, String avgRate, int totalRatingsNo, String price, String cPrice, String payMethod) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (freeCouponsNo != 0) {
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
            ratings.setText(avgRate);
            totalRatings.setText(totalRatingsNo + "(Ratings)");
            productPrice.setText(price);
            cuttedPrice.setText(cPrice);
            paymentMethod.setText(payMethod);
            if (wishList) {
                deleteBtn.setVisibility(View.VISIBLE);
            }
            else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "delete", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }
    }
}
