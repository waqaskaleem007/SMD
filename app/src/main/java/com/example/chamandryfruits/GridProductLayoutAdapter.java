package com.example.chamandryfruits;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<HorizontalProductScrollModel>();

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return horizontalProductScrollModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            ImageView productImage = view.findViewById(R.id.h_s_product_image);
            TextView productTitle = view.findViewById(R.id.h_s_product_name);
            TextView productDesc = view.findViewById(R.id.h_s_product_desc);
            TextView productPrice = view.findViewById(R.id.h_s_product_price);

            productImage.setImageResource(horizontalProductScrollModelList.get(position).getProductImage());
            productTitle.setText(horizontalProductScrollModelList.get(position).getProductTitle());
            productDesc.setText(horizontalProductScrollModelList.get(position).getProductDesc());
            productPrice.setText(horizontalProductScrollModelList.get(position).getProductPrice());

        }else{
            view = convertView;
        }
        return view;

    }
}
