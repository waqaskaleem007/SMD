package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {

    List<RewardsModel> rewardsModels = new ArrayList<>();
    private boolean useMiniLayout = false;
    private RecyclerView couponsRecyclerView;
    private LinearLayout selectedCoupon;
    private String productOriginalPrice;
    private TextView couponTitle;
    private TextView couponExpiryDate;
    private TextView selectedCouponBody;
    private TextView discountedPrice;
    private int cartItemPosition = -1;
    private List<CartItemModel> cartItemModels;


    public MyRewardsAdapter(List<RewardsModel> rewardsModels, boolean useMiniLayout) {
        this.rewardsModels = rewardsModels;
        this.useMiniLayout = useMiniLayout;
    }

    public MyRewardsAdapter(List<RewardsModel> rewardsModels, boolean useMiniLayout, RecyclerView couponsRecyclerView, LinearLayout selectedCoupon, String productOriginalPrice, TextView couponTitle, TextView couponExpiryDate, TextView couponBody, TextView discountedPrice) {
        this.rewardsModels = rewardsModels;
        this.useMiniLayout = useMiniLayout;
        this.couponsRecyclerView = couponsRecyclerView;
        this.selectedCoupon = selectedCoupon;
        this.productOriginalPrice = productOriginalPrice;
        this.couponTitle = couponTitle;
        this.couponExpiryDate = couponExpiryDate;
        this.selectedCouponBody = couponBody;
        this.discountedPrice = discountedPrice;

    }

    public MyRewardsAdapter(int cartItemPosition,List<RewardsModel> rewardsModels, boolean useMiniLayout, RecyclerView couponsRecyclerView, LinearLayout selectedCoupon, String productOriginalPrice, TextView couponTitle, TextView couponExpiryDate, TextView couponBody, TextView discountedPrice, List<CartItemModel> cartItemModels) {
        this.rewardsModels = rewardsModels;
        this.useMiniLayout = useMiniLayout;
        this.couponsRecyclerView = couponsRecyclerView;
        this.selectedCoupon = selectedCoupon;
        this.productOriginalPrice = productOriginalPrice;
        this.couponTitle = couponTitle;
        this.couponExpiryDate = couponExpiryDate;
        this.selectedCouponBody = couponBody;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartItemModels = cartItemModels;
    }

    @NonNull
    @Override
    public MyRewardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rewardsView;
        if (useMiniLayout) {
            rewardsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_rewards_item_layout, parent, false);
        } else {
            rewardsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout, parent, false);
        }
        return new ViewHolder(rewardsView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRewardsAdapter.ViewHolder holder, int position) {
        String couponId = rewardsModels.get(position).getCouponId();
        String type = rewardsModels.get(position).getType();
        Timestamp validity = rewardsModels.get(position).getTimestamp();
        String body = rewardsModels.get(position).getCouponBody();
        String lowerLimit = rewardsModels.get(position).getLowerLimit();
        String upperLimit = rewardsModels.get(position).getUpperLimit();
        String discORamt = rewardsModels.get(position).getDiscount();
        boolean alreadyUses = rewardsModels.get(position).isAlreadyUsed();

        holder.setData(type, validity, body, lowerLimit, upperLimit, discORamt, alreadyUses, couponId);
    }

    @Override
    public int getItemCount() {
        return rewardsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView expiryDate;
        TextView couponBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rewards_coupon_title);
            expiryDate = itemView.findViewById(R.id.rewards_coupon_validity);
            couponBody = itemView.findViewById(R.id.rewards_coupon_body);
        }

        @SuppressLint("SetTextI18n")
        private void setData(final String type, final Timestamp validity, final String body, final String lowerLimit, final String upperLimit, final String discORamt, final boolean alreadyUsed, final String couponId) {
            final String typeChange;
            if (type.equals("Discount")) {
                title.setText(type);
                typeChange = type;
            } else {
                title.setText("FLAT RS." + discORamt + " OFF");
                typeChange = "FLAT RS." + discORamt + " OFF";
            }
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM YYYY");


            if(alreadyUsed){
                expiryDate.setText("Already used");
                expiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                couponBody.setTextColor(Color.parseColor("#50ffffff"));
                title.setTextColor(Color.parseColor("#50ffffff"));
            }else {
                couponBody.setTextColor(Color.parseColor("#ffffff"));
                title.setTextColor(Color.parseColor("#ffffff"));
                expiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.couponPurple));
                expiryDate.setText("till " + simpleDateFormat.format(validity.toDate()));
            }


            //couponExpiryDate.setText("till " + validity.toDate().toString());
            couponBody.setText(body);

            if (useMiniLayout) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!alreadyUsed) {
                            couponTitle.setText(typeChange);
                            selectedCouponBody.setText(body);
                            expiryDate.setText("till " + validity.toDate().toString());
                            couponExpiryDate.setText("till " + validity.toDate().toString());

                            if (Long.parseLong(productOriginalPrice) > Long.parseLong(lowerLimit) && Long.parseLong(productOriginalPrice) < Long.parseLong(upperLimit)) {
                                if (type.equals("Discount")) {
                                    long discountAmount = Long.parseLong(productOriginalPrice) * Long.parseLong(discORamt) / 100;
                                    discountedPrice.setText("Rs." + String.valueOf(Long.parseLong(productOriginalPrice) - discountAmount) + "/-");
                                } else {
                                    discountedPrice.setText("Rs." + String.valueOf(Long.parseLong(productOriginalPrice) - Long.parseLong(discORamt) + "/-"));
                                }
                                if(cartItemPosition != -1) {
                                    cartItemModels.get(cartItemPosition).setSelectedCouponId(couponId);
                                }

                            } else {
                                if(cartItemPosition != -1) {
                                    cartItemModels.get(cartItemPosition).setSelectedCouponId(null);
                                }
                                discountedPrice.setText("Invalid");
                                Toast.makeText(itemView.getContext(), "Sorry! Product does not matches the coupon terms", Toast.LENGTH_SHORT).show();
                            }

                            if (couponsRecyclerView.getVisibility() == View.GONE) {
                                couponsRecyclerView.setVisibility(View.VISIBLE);
                                selectedCoupon.setVisibility(View.GONE);
                            } else {
                                couponsRecyclerView.setVisibility(View.GONE);
                                selectedCoupon.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }

        }
    }
}
