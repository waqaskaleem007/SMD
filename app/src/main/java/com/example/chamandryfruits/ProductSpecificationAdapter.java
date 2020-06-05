package com.example.chamandryfruits;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecificationAdapter extends RecyclerView.Adapter<ProductSpecificationAdapter.ViewHolder> {

    private List<ProductSpecificationModel> productSpecificationModels = new ArrayList<ProductSpecificationModel>();

    public ProductSpecificationAdapter(List<ProductSpecificationModel> productSpecificationModels) {
        this.productSpecificationModels = productSpecificationModels;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpecificationModels.get(position).getType()) {
            case 0:
                return ProductSpecificationModel.SPECIFICATION_TITLE;
            case 1:
                return ProductSpecificationModel.SPECIFICATION_BODY;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public ProductSpecificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                TextView title = new TextView(parent.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(setDp(16, parent.getContext()),
                        setDp(16, parent.getContext()),
                        setDp(16, parent.getContext()),
                        setDp(8, parent.getContext()));

                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);

            case ProductSpecificationModel.SPECIFICATION_BODY:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specification_item_layout, parent, false);
                return new ViewHolder(view);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpecificationAdapter.ViewHolder holder, int position) {
        switch (productSpecificationModels.get(position).getType()) {
            case ProductSpecificationModel.SPECIFICATION_TITLE:
                holder.SetTitle(productSpecificationModels.get(position).getTitle());
                break;
            case ProductSpecificationModel.SPECIFICATION_BODY:
                String name = productSpecificationModels.get(position).getFeatureName();
                String value = productSpecificationModels.get(position).getFeatureValue();
                holder.SetFeatures(name, value);
                break;
            default:
                return;
        }



    }

    @Override
    public int getItemCount() {
        if(productSpecificationModels == null){
            return 0;
        }else {
            return productSpecificationModels.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView featureName;
        TextView featureValue;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        private void SetTitle(String titleText) {
            title = (TextView) itemView;
            title.setText(titleText);
        }

        private void SetFeatures(String name, String value) {
            featureName = itemView.findViewById(R.id.feature_name);
            featureValue = itemView.findViewById(R.id.feature_value);
            featureName.setText(name);
            featureValue.setText(value);
        }
    }

    private int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
