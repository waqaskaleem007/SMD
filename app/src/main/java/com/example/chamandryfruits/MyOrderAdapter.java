package com.example.chamandryfruits;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    List<MyOrdersItemModel> myOrdersItemModelList = new ArrayList<>();

    public MyOrderAdapter(List<MyOrdersItemModel> myOrdersItemModelList) {
        this.myOrdersItemModelList = myOrdersItemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_oder_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, int position) {
        int resource = myOrdersItemModelList.get(position).getProductImage();
        int rating = myOrdersItemModelList.get(position).getRating();
        String title = myOrdersItemModelList.get(position).getProductTitle();
        String deliveryStatus = myOrdersItemModelList.get(position).getDeliveryStatus();
        holder.SetData(resource,title,deliveryStatus,rating);


    }

    @Override
    public int getItemCount() {
        return myOrdersItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rateNowContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.my_orders_product_image);
            productTitle = itemView.findViewById(R.id.my_orders_product_tite);
            orderIndicator = itemView.findViewById(R.id.order_indicator);
            deliveryStatus = itemView.findViewById(R.id.order_delivered_date);
            rateNowContainer = itemView.findViewById(R.id.ratenow_container);
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void SetData(int resource, String pTitle, String dStatus, int rating){
            productImage.setImageResource(resource);
            productTitle.setText(pTitle);
            if(dStatus.equalsIgnoreCase("canceled")) {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            }
            else{
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successfulGreen)));
            }
            deliveryStatus.setText(dStatus);

            SetRating(rating);
            ////////rating layout
            for(int i=0; i<rateNowContainer.getChildCount(); i++){
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
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void SetRating(int starPosition){
            for(int i=0; i<rateNowContainer.getChildCount(); i++){
                ImageView starButton = (ImageView) rateNowContainer.getChildAt(i);
                starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if(i <= starPosition){
                    starButton.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }
            }
        }

    }
}
