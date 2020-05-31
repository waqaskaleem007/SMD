package com.example.chamandryfruits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.example.chamandryfruits.DeliveryActivity.SELECT_ADDRESS;
import static com.example.chamandryfruits.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.chamandryfruits.MyAddressesActivity.RefreshItem;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    List<AddressesModel> addressesModels = new ArrayList<>();
    private int MODE;
    private int preSelectedPosition;

    public AddressesAdapter(List<AddressesModel> addressesModels, int mode) {
        this.addressesModels = addressesModels;
        this.MODE = mode;
        preSelectedPosition = DBQueries.selectedAddress;
    }

    @NonNull
    @Override
    public AddressesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View addressesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new ViewHolder(addressesView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.ViewHolder holder, int position) {
        String fullName = addressesModels.get(position).getFullName();
        String address = addressesModels.get(position).getAddress();
        String pinCode = addressesModels.get(position).getPinCode();
        boolean selected = addressesModels.get(position).isSelected();
        holder.SetData(fullName, address, pinCode, selected, position);
    }

    @Override
    public int getItemCount() {
        return addressesModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        TextView pinCode;
        ImageView icon;
        LinearLayout optionContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pinCode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_view);
            optionContainer = itemView.findViewById(R.id.option_container);
        }

        private void SetData(String fName, String aAddress, String pPinCode, boolean select, final int position) {
            name.setText(fName);
            address.setText(aAddress);
            pinCode.setText(pPinCode);
            if (MODE == SELECT_ADDRESS) {
                icon.setImageResource(R.mipmap.check);
                if (select) {
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preSelectedPosition != position) {
                            addressesModels.get(position).setSelected(true);
                            addressesModels.get(preSelectedPosition).setSelected(false);
                            RefreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBQueries.selectedAddress = position;
                        }
                    }
                });
            } else if (MODE == MANAGE_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                icon.setImageResource(R.mipmap.vertical_dots);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionContainer.setVisibility(View.VISIBLE);
                        RefreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = position;
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RefreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = -1;

                    }
                });
            }

        }

    }
}
