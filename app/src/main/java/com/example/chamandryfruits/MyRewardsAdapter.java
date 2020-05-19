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

    public MyRewardsAdapter(List<RewardsModel> rewardsModels) {
        this.rewardsModels = rewardsModels;
    }

    @NonNull
    @Override
    public MyRewardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rewardsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_item_layout,parent,false);
        return new ViewHolder(rewardsView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRewardsAdapter.ViewHolder holder, int position) {
        String title = rewardsModels.get(position).getTitle();
        String expiryDate = rewardsModels.get(position).getExpiryDate();
        String couponBody = rewardsModels.get(position).getCouponBody();
        holder.setData(title,expiryDate,couponBody);
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
        private void setData(String rTitle, String eDate, String cBody){
            title.setText(rTitle);
            expiryDate.setText(eDate);
            couponBody.setText(cBody);
        }
    }
}
