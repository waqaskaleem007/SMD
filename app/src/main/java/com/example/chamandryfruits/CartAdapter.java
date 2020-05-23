package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.BiMap;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    List<CartItemModel> cartItemModelList = new ArrayList<CartItemModel>();

    public CartAdapter(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()) {
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CartItemModel.CART_ITEM:
                View cartView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new CartItemViewHolder(cartView);
            case CartItemModel.TOTAL_AMOUNT:
                View totalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new TotalAmountViewHolder(totalView);
            default:
                return null;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                int resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                int freeCoupons = cartItemModelList.get(position).getFreeCoupons();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                int offersApplied = cartItemModelList.get(position).getOffersApplied();
                ((CartItemViewHolder) holder).setItemDetails(resource, title, freeCoupons, productPrice, offersApplied, cuttedPrice);

                break;
            case CartItemModel.TOTAL_AMOUNT:
                String totalItems = cartItemModelList.get(position).getTotalItems();
                String totalItemPrice = cartItemModelList.get(position).getTotalItemPrice();
                String deliveryPrice = cartItemModelList.get(position).getDeliveryPrice();
                String saveAmount = cartItemModelList.get(position).getSaveAmount();
                String totalAmount = cartItemModelList.get(position).getTotalAmmount();
                ((TotalAmountViewHolder) holder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, saveAmount);
                break;
            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView freeCouponsIcon;
        private TextView productTitle;
        private TextView freeCoupons;
        private TextView productPrice;
        private TextView offerApplied;
        private TextView couponsApplied;
        private TextView cuttedPrice;
        private TextView productQuantity;


        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupon);
            freeCouponsIcon = itemView.findViewById(R.id.free_coupon_icon);
            productPrice = itemView.findViewById(R.id.product_price);
            offerApplied = itemView.findViewById(R.id.offers_applied);
            couponsApplied = itemView.findViewById(R.id.coupons_applied);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }

        @SuppressLint("SetTextI18n")
        private void setItemDetails(int resource, String title, int fCoupons, String pPrice, int oApplied, String cPrice) {
            productImage.setImageResource(resource);
            productTitle.setText(title);
            productPrice.setText(pPrice);
            if (fCoupons > 0) {
                freeCouponsIcon.setVisibility(View.VISIBLE);
                freeCoupons.setVisibility(View.VISIBLE);
                if (fCoupons == 1) {
                    freeCoupons.setText("Free " + fCoupons + " coupon");
                } else {
                    freeCoupons.setText("Free " + fCoupons + " coupons");
                }
            } else {
                freeCouponsIcon.setVisibility(View.INVISIBLE);
                freeCoupons.setVisibility(View.INVISIBLE);
            }

            if (oApplied > 0) {
                offerApplied.setVisibility(View.VISIBLE);
                offerApplied.setText(oApplied + " Offers applied");
            } else {
                offerApplied.setVisibility(View.INVISIBLE);
            }

            cuttedPrice.setText(cPrice);
            productQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog quantityDialog = new Dialog(itemView.getContext());
                    quantityDialog.setContentView(R.layout.quantity_dialog);
                    quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    quantityDialog.setCancelable(false);


                    final EditText quantity = quantityDialog.findViewById(R.id.quantity_number);
                    Button cancelButton = quantityDialog.findViewById(R.id.cancel_button);
                    Button okButton = quantityDialog.findViewById(R.id.ok_button);


                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            quantityDialog.dismiss();
                        }
                    });

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productQuantity.setText("Qty: " + quantity.getText() + " ");
                            quantityDialog.dismiss();
                        }
                    });
                    quantityDialog.show();
                }
            });
        }


    }

    class TotalAmountViewHolder extends RecyclerView.ViewHolder {

        private TextView totalItems;
        private TextView totalPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;

        public TotalAmountViewHolder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saved_amount);

        }

        private void setTotalAmount(String tItems, String tPrice, String dPrice, String tAmount, String sAmount) {
            totalItems.setText(tItems);
            totalPrice.setText(tPrice);
            deliveryPrice.setText(dPrice);
            totalAmount.setText(tAmount);
            savedAmount.setText(sAmount);
        }
    }
}
