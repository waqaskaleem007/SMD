package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.chamandryfruits.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private RecyclerView myAddressesRecyclerView;
    private static  AddressesAdapter addressesAdapter;
    private Button deliverHereButton;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Addressed");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myAddressesRecyclerView = findViewById(R.id.addresses_recycler_view);
        deliverHereButton = findViewById(R.id.deliver_here_button);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(layoutManager);

        List<AddressesModel> addressesModels = new ArrayList<>();
        addressesModels.add(new AddressesModel("Ali Azmat", "23 naddem shaheed road", "54000",true));
        addressesModels.add(new AddressesModel("irfan Azmat", "23 naddem shaheed road", "54000",false));
        addressesModels.add(new AddressesModel("khan Azmat", "48 Garden town", "54000",false));
        addressesModels.add(new AddressesModel("tugh Azmat", "28 johar town", "208888",false));
        addressesModels.add(new AddressesModel("raza Azmat", "23 naddem izmeer road", "54000",false));
        addressesModels.add(new AddressesModel("hood Azmat", "23 naddem shaheed road", "54000",false));
        addressesModels.add(new AddressesModel("Ali Azmat", "23 naddem shaheed road", "54000",false));
        addressesModels.add(new AddressesModel("Ali Azmat", "23 naddem shaheed road", "54000",false));

        int mode = getIntent().getIntExtra("MODE",-1);
        if(mode == SELECT_ADDRESS){
            deliverHereButton.setVisibility(View.VISIBLE);
        }
        else{
            deliverHereButton.setVisibility(View.GONE);
        }
        addressesAdapter = new AddressesAdapter(addressesModels,mode);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator) Objects.requireNonNull(myAddressesRecyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();

    }

    public static void RefreshItem(int deSelect, int select){
        addressesAdapter.notifyItemChanged(deSelect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
