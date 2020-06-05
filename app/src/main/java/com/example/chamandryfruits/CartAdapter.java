package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CartAdapter extends RecyclerView.Adapter {

    List<CartItemModel> cartItemModelList = new ArrayList<CartItemModel>();
    private int lastPosition = -1;
    private TextView cartTotalAmount;

    private boolean showDeleteButton;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteButton) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteButton = showDeleteButton;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()) {
            case CartItemModel.CART_ITEM:
                String productId = cartItemModelList.get(position).getProductId();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupons = cartItemModelList.get(position).getFreeCoupons();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                Long pQuantity = cartItemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartItemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartItemModelList.get(position).isQtyError();
                List<String> qtyIds = cartItemModelList.get(position).getQtyIds();
                long stockQt = cartItemModelList.get(position).getStockQuantity();

                boolean inStock = cartItemModelList.get(position).isInStock();
                if (!inStock) {
                    productPrice = String.valueOf(0);
                }
                ((CartItemViewHolder) holder).setItemDetails(productId, resource, title, freeCoupons, productPrice, offersApplied, cuttedPrice, position, inStock, String.valueOf(pQuantity), maxQuantity, qtyError, qtyIds, stockQt);

                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount = 0;
                int saveAmount = 0;


                for (int i = 0; i < cartItemModelList.size(); i++) {

                    if (cartItemModelList.get(i).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(i).isInStock()) {
                        totalItems++;
                        totalItemPrice += Integer.parseInt(cartItemModelList.get(i).getProductPrice());

                    }
                }
                if (totalItemPrice > 500) {
                    deliveryPrice = "Free";
                    totalAmount = totalItemPrice;
                } else {
                    deliveryPrice = "100";
                    totalAmount = totalItemPrice + 100;
                }

                ((TotalAmountViewHolder) holder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, saveAmount);
                break;
            default:
                return;
        }
        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
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
        private LinearLayout deleteButton;
        private LinearLayout couponRedemptionLayout;


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
            deleteButton = itemView.findViewById(R.id.remove_item_btn);
            couponRedemptionLayout = itemView.findViewById(R.id.coupon_reedemption_layout);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("SetTextI18n")
        private void setItemDetails(final String productId, String resource, String title, Long fCoupons, String pPrice, Long oApplied, String cPrice, final int position, final boolean inStock, final String pQuantity, final Long maxQuantity, boolean qtyError, final List<String> qtyIds, final long stockQty) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.img_placeholder)).into(productImage);
            productTitle.setText(title);

            if (inStock) {
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

                productPrice.setText("Rs." + pPrice + "/-");
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setText("Rs." + cPrice + "/-");
                couponRedemptionLayout.setVisibility(View.VISIBLE);

                productQuantity.setText("Qty: " + pQuantity + " ");
                //if(showDeleteButton) {
                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(android.R.color.black));
                        productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(android.R.color.black)));
                    }
                //}
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
                        quantity.setHint("Max " + String.valueOf(maxQuantity));
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(quantity.getText())) {
                                    if (Long.parseLong(quantity.getText().toString()) <= maxQuantity) {
                                        if (itemView.getContext() instanceof Home) {
                                            DBQueries.cartItemModels.get(position).setProductQuantity(Long.valueOf(quantity.getText().toString()));
                                        } else {
                                            if (DeliveryActivity.fromCart) {
                                                DBQueries.cartItemModels.get(position).setProductQuantity(Long.valueOf(quantity.getText().toString()));
                                            } else {
                                                DeliveryActivity.cartItemModels.get(position).setProductQuantity(Long.valueOf(quantity.getText().toString()));
                                            }
                                        }

                                        productQuantity.setText("Qty: " + quantity.getText() + " ");
                                        if (!showDeleteButton) {
                                            DeliveryActivity.cartItemModels.get(position).setQtyError(false);
                                            final int initialQuantity = Integer.parseInt(pQuantity);
                                            final int finalQuantity = Integer.parseInt(quantity.getText().toString());
                                            final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                                            if (finalQuantity > initialQuantity) {

                                                for (int j = 0; j < finalQuantity - initialQuantity; j++) {
                                                    final String qtyDocumentName = UUID.randomUUID().toString().substring(0, 20);
                                                    Map<String, Object> timeStamp = new HashMap<>();
                                                    timeStamp.put("time", FieldValue.serverTimestamp());
                                                    final int finalJ = j;
                                                    firebaseFirestore.collection("PRODUCTS").document(productId).collection("QUANTITY")
                                                            .document(qtyDocumentName).set(timeStamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            qtyIds.add(qtyDocumentName);
                                                            if (finalJ + 1 == finalQuantity - initialQuantity) {
                                                                firebaseFirestore.collection("PRODUCTS").document(productId).collection("QUANTITY")
                                                                        .orderBy("time", Query.Direction.ASCENDING)
                                                                        .limit(stockQty)
                                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            List<String> serverQty = new ArrayList<>();

                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                serverQty.add(queryDocumentSnapshot.getId());
                                                                            }
                                                                            long availableQty = 0;


                                                                            for (String qtyId : qtyIds) {
                                                                                if (!serverQty.contains(qtyId)) {
                                                                                    DeliveryActivity.cartItemModels.get(position).setQtyError(true);
                                                                                    DeliveryActivity.cartItemModels.get(position).setMaxQuantity(availableQty);
                                                                                    Toast.makeText(itemView.getContext(), "All products May not be available in required quantity", Toast.LENGTH_SHORT).show();
                                                                                    DeliveryActivity.allProductsAvailable = false;

                                                                                } else {
                                                                                    availableQty++;
                                                                                }
                                                                            }
                                                                            DeliveryActivity.cartAdapter.notifyDataSetChanged();

                                                                        } else {
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                                //////document delete code if user delete the product
                                            } else if (initialQuantity > finalQuantity) {
                                                for (int i = 0; i < initialQuantity - finalQuantity; i++) {
                                                    /// agr user beech ma product ko chor de aur buy na kare
                                                    final String qtyId = qtyIds.get(qtyIds.size() - 1 - i);
                                                    firebaseFirestore.collection("PRODUCTS").document(productId).collection("QUANTITY")
                                                            .document(qtyId).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    qtyIds.remove(qtyId);
                                                                    DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                }
                                                            });
                                                }

                                            }

                                        }

                                    } else {
                                        Toast.makeText(itemView.getContext(), "Max quantity: " + maxQuantity.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                quantityDialog.dismiss();
                            }
                        });
                        quantityDialog.show();
                    }
                });

                if (oApplied > 0) {
                    offerApplied.setVisibility(View.VISIBLE);
                    offerApplied.setText(oApplied + " Offers applied");
                } else {
                    offerApplied.setVisibility(View.INVISIBLE);
                }
                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);

            } else {
                productPrice.setText("Out of Stock");
                cuttedPrice.setText(" ");
                productPrice.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                freeCoupons.setVisibility(View.INVISIBLE);
                couponRedemptionLayout.setVisibility(View.GONE);
                productQuantity.setVisibility(View.INVISIBLE);
                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                parent.setVisibility(View.GONE);
                couponsApplied.setVisibility(View.GONE);
                offerApplied.setVisibility(View.GONE);
                freeCouponsIcon.setVisibility(View.INVISIBLE);
            }
            if (showDeleteButton) {
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailsActivity.runningCartQuery) {
                        ProductDetailsActivity.runningCartQuery = true;
                        DBQueries.RemoveFormCart(position, itemView.getContext(), cartTotalAmount);
                    }
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

        private void setTotalAmount(int tItems, int tPrice, String dPrice, int tAmount, int sAmount) {
            totalItems.setText("Price (" + tItems + " items)");
            totalPrice.setText("Rs." + tPrice + "/-");
            if (dPrice.equalsIgnoreCase("Free")) {
                deliveryPrice.setText(dPrice);
            } else {
                deliveryPrice.setText("Rs." + dPrice + "/-");
            }
            totalAmount.setText("Rs." + tAmount + "/-");
            cartTotalAmount.setText("Rs." + tAmount + "/-");
            savedAmount.setText("You saved Rs." + sAmount + " on this product");
            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (tPrice == 0) {
                if(DeliveryActivity.fromCart) {
                    DBQueries.cartItemModels.remove(DBQueries.cartItemModels.size() - 1);
                    DeliveryActivity.cartItemModels.remove(DeliveryActivity.cartItemModels.size() - 1);
                }
                if(showDeleteButton){
                    DBQueries.cartItemModels.remove(DBQueries.cartItemModels.size() - 1);
                }
                parent.setVisibility(View.GONE);
            } else {
                parent.setVisibility(View.VISIBLE);
            }

        }
    }
}
