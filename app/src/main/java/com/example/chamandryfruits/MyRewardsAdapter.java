package com.example.chamandryfruits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.ViewHolder> {

    List<RewardsModel> rewardsModels = new ArrayList<>();
    private boolean useMiniLayout = false;

    public MyRewardsAdapter(List<RewardsModel> rewardsModels, boolean useMiniLayout) {
        this.rewardsModels = rewardsModels;
        this.useMiniLayout = useMiniLayout;
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
        String title = rewardsModels.get(position).getTitle();
        String expiryDate = rewardsModels.get(position).getExpiryDate();
        String couponBody = rewardsModels.get(position).getCouponBody();
        holder.setData(title, expiryDate, couponBody);
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

        private void setData(final String rTitle, final String eDate, final String cBody) {
            title.setText(rTitle);
            expiryDate.setText(eDate);
            couponBody.setText(cBody);

            if(useMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetailsActivity.couponTitle.setText(rTitle);
                        ProductDetailsActivity.couponBody.setText(cBody);
                        ProductDetailsActivity.couponExpiryDate.setText(eDate);
                        ProductDetailsActivity.ShowDialogRecyclerView();
                    }
                });
            }

        }
    }
}
