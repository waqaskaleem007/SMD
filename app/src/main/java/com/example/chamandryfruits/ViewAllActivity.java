package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GridView gridView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Deals of the day");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        gridView = findViewById(R.id.grid_view);

        int layoutCode = getIntent().getIntExtra("layout_code", -1);

        if (layoutCode == 0) {
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            List<WishListModel> wishListModels = new ArrayList<>();
            wishListModels.add(new WishListModel(R.mipmap.phone2, "Pixel 2(Black)", 3, "1.5", 700, "Rs.49999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.phone_image, "Pixel 3(Black)", 2, "2.5", 800, "Rs.59999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.jacket, "Pixel 4(Black)", 1, "3.5", 300, "Rs.69999/-", "Rs.59999/-", "with card"));
            wishListModels.add(new WishListModel(R.mipmap.book, "Pixel 5(White)", 0, "4.5", 200, "Rs.79999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.sofa, "Pixel 6(Black)", 4, "2.5", 1700, "Rs.89999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.handfree, "Pixel 7(Black)", 0, "4.5", 7200, "Rs.99999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.weights, "Pixel 2(Black)", 3, "1.5", 700, "Rs.49999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.download, "Pixel 3(Black)", 2, "2.5", 800, "Rs.59999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.jacket, "Pixel 4(Black)", 1, "3.5", 300, "Rs.69999/-", "Rs.59999/-", "with card"));
            wishListModels.add(new WishListModel(R.mipmap.book, "Pixel 5(White)", 0, "4.5", 200, "Rs.79999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.phone2, "Pixel 6(Black)", 4, "2.5", 1700, "Rs.89999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.phone_image, "Pixel 7(Black)", 0, "4.5", 7200, "Rs.99999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.phone2, "Pixel 2(Black)", 3, "1.5", 700, "Rs.49999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.phone_image, "Pixel 3(Black)", 2, "2.5", 800, "Rs.59999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.phone2, "Pixel 4(Black)", 1, "3.5", 300, "Rs.69999/-", "Rs.59999/-", "with card"));
            wishListModels.add(new WishListModel(R.mipmap.phone_image, "Pixel 5(White)", 0, "4.5", 200, "Rs.79999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.phone2, "Pixel 6(Black)", 4, "2.5", 1700, "Rs.89999/-", "Rs.59999/-", "Cash on delivery"));
            wishListModels.add(new WishListModel(R.mipmap.phone_image, "Pixel 7(Black)", 0, "4.5", 7200, "Rs.99999/-", "Rs.59999/-", "Cash on delivery"));


            WishListAdapter wishListAdapter = new WishListAdapter(wishListModels, false);
            recyclerView.setAdapter(wishListAdapter);
            wishListAdapter.notifyDataSetChanged();
        } else if (layoutCode == 1) {
            gridView.setVisibility(View.VISIBLE);

            List<HorizontalProductScrollModel> horizontalProductScrollModels = new ArrayList<>();
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.phone_image, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.book, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.sofa, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.jacket, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.handfree, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.download, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.weights, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.phone_image, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.book, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.sofa, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.jacket, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.handfree, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.download, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.weights, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.phone_image, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.book, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.sofa, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.jacket, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.handfree, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.download, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));
            horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.weights, "Redmi 5A", "SnapDragon 425 Processor", "5999/-"));


            GridProductLayoutAdapter adapter = new GridProductLayoutAdapter(horizontalProductScrollModels);
            gridView.setAdapter(adapter);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
