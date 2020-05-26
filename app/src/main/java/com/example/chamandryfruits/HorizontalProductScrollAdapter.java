package com.example.chamandryfruits;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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
        String resource =  horizontalProductScrollModels.get(position).getProductImage();
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
        if(horizontalProductScrollModels.size() > 8){
            return 8;
        }
        else {
            return horizontalProductScrollModels.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitle;
        TextView productDesc;
        TextView productPrice;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.h_s_product_image);
            productTitle = itemView.findViewById(R.id.h_s_product_name);
            productDesc = itemView.findViewById(R.id.h_s_product_desc);
            productPrice = itemView.findViewById(R.id.h_s_product_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }

        public void setProductImage(String resource) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.phone_image)).into(productImage);
        }

        public void setProductTitle(String productTitle) {
            this.productTitle.setText(productTitle);
        }

        public void setProductDesc(String productDesc) {
            this.productDesc.setText(productDesc);
        }

        public void setProductPrice(String productPrice) {
            this.productPrice.setText("Rs."+productPrice+"/-");
        }
    }
}
