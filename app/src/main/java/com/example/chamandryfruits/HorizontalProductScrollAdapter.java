package com.example.chamandryfruits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    List<HorizontalProductScrollModel> horizontalProductScrollModels = new ArrayList<>();

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModels) {
        this.horizontalProductScrollModels = horizontalProductScrollModels;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder holder, int position) {
        int resource =  horizontalProductScrollModels.get(position).getProductImage();
        String productTitle = horizontalProductScrollModels.get(position).getProductTitle();
        String productDesc = horizontalProductScrollModels.get(position).getProductDesc();
        String productPrice = horizontalProductScrollModels.get(position).getProductPrice();

        holder.setProductImage(resource);
        holder.setProductTitle(productTitle);
        holder.setProductDesc(productDesc);
        holder.setProductPrice(productPrice);


    }

    @Override
    public int getItemCount() {
        return horizontalProductScrollModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle;
        TextView productDesc;
        TextView productPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.h_s_product_image);
            productTitle = itemView.findViewById(R.id.h_s_product_name);
            productDesc = itemView.findViewById(R.id.h_s_product_desc);
            productPrice = itemView.findViewById(R.id.h_s_product_price);
        }

        public void setProductImage(int productImage) {
            this.productImage.setImageResource(productImage);
        }

        public void setProductTitle(String productTitle) {
            this.productTitle.setText(productTitle);
        }

        public void setProductDesc(String productDesc) {
            this.productDesc.setText(productDesc);
        }

        public void setProductPrice(String productPrice) {
            this.productPrice.setText(productPrice);
        }
    }
}
