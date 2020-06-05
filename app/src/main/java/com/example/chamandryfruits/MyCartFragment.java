package com.example.chamandryfruits;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {

    public static RecyclerView cartItemRecyclerView;
    private Button Continue;
    private Dialog loadingDialog;
    public static CartAdapter cartAdapter;
    private TextView totalAmount;
    public static int count = 0;
    private ConstraintLayout totalAmountLayout;

    public MyCartFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        ///loading Dialog
        loadingDialog = new Dialog(Objects.requireNonNull(getContext()));
        loadingDialog.setContentView(R.layout.loadind_progress_dialog);
        loadingDialog.setCancelable(false);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        Objects.requireNonNull(loadingDialog.getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ////loading Dialog

        cartItemRecyclerView = view.findViewById(R.id.cart_items_recyclerview);
        Continue = view.findViewById(R.id.cart_continue_btn);
        totalAmount = view.findViewById(R.id.total_cart_amount);
        totalAmountLayout = view.findViewById(R.id.total_amount_constraint_layout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemRecyclerView.setLayoutManager(layoutManager);


        cartAdapter = new CartAdapter(DBQueries.cartItemModels, totalAmount, true);
        cartItemRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        Continue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                DeliveryActivity.cartItemModels = new ArrayList<>();

                DeliveryActivity.fromCart = true;

                for (int i = 0; i < DBQueries.cartItemModels.size(); i++) {
                    CartItemModel cartItemModel = DBQueries.cartItemModels.get(i);
                    if (cartItemModel.isInStock()) {
                        DeliveryActivity.cartItemModels.add(cartItemModel);
                    }
                }
                DeliveryActivity.cartItemModels.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                loadingDialog.show();
                if (DBQueries.addressesModels.size() == 0) {
                    DBQueries.LoadAddresses(getContext(), loadingDialog);
                } else {
                    loadingDialog.dismiss();
                    Intent deliveryIntent = new Intent(getContext(), DeliveryActivity.class);

                    startActivity(deliveryIntent);
                }
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.notifyDataSetChanged();
        if (DBQueries.cartItemModels.size() == 0) {
            DBQueries.cartList.clear();
            DBQueries.LoadCartList(getContext(), loadingDialog, true, new TextView(getContext()), totalAmount);
        } else {
            //if (DBQueries.cartItemModels.get(DBQueries.cartItemModels.size() - 1).getType() == CartItemModel.TOTAL_AMOUNT) {
            //DBQueries.LoadCartList(getContext(), loadingDialog, true, new TextView(getContext()), totalAmount);

            if (DBQueries.cartItemModels.size() > DBQueries.cartList.size()) {
                DBQueries.cartItemModels.remove(DBQueries.cartItemModels.size() - 1);
            }
            DBQueries.LoadCartList(getContext(), loadingDialog, true, new TextView(getContext()), totalAmount);
            //DBQueries.cartItemModels.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
            LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
            parent.setVisibility(View.VISIBLE);
            count--;
            loadingDialog.dismiss();

        }
    }
}
