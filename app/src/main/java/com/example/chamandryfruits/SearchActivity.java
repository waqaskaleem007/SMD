package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private androidx.appcompat.widget.SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.search_recycler_view);

        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final List<WishListModel> list = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        final Adapter adapter = new Adapter(list, false);
        adapter.setFromSearch(true);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                list.clear();
                ids.clear();

                final String[] tags = query.toLowerCase().split(" ");
                for (final String tag : tags) {
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("PRODUCTS").whereArrayContains("tags", tag)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                                    WishListModel model = new WishListModel(documentSnapshot.getId(),
                                            Objects.requireNonNull(documentSnapshot.get("product_image_1")).toString(),
                                            Objects.requireNonNull(documentSnapshot.get("product_title")).toString(),
                                            (long) Objects.requireNonNull(documentSnapshot.get("free_coupons")),
                                            Objects.requireNonNull(documentSnapshot.get("average_rating")).toString(),
                                            (long) Objects.requireNonNull(documentSnapshot.get("total_ratings")),
                                            Objects.requireNonNull(documentSnapshot.get("product_price")).toString(),
                                            Objects.requireNonNull(documentSnapshot.get("cutted_price")).toString(),
                                            (boolean) Objects.requireNonNull(documentSnapshot.get("COD")),
                                            true);
                                    model.setTags((ArrayList<String>) documentSnapshot.get("tags"));
                                    if (!ids.contains(model.getProductId())) {
                                        list.add(model);
                                        ids.add(model.getProductId());
                                    }
                                }
                                if (tag.equals(tags[tags.length - 1])) {
                                    if (list.size() == 0) {
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    } else {
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter.getFilter().filter(query);
                                    }
                                }
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SearchActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    class Adapter extends WishListAdapter implements Filterable {


        private List<WishListModel> originalList;

        public Adapter(List<WishListModel> wishListModels, boolean wishList) {
            super(wishListModels, wishList);
            originalList = wishListModels;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<WishListModel> filteredList = new ArrayList<>();
                    final String[] tags = constraint.toString().toLowerCase().split(" ");

                    for (WishListModel model : originalList) {
                        ArrayList<String> presentTags = new ArrayList<>();
                        for (String tag : tags) {
                            if (model.getTags().contains(tag)) {
                                presentTags.add(tag);
                            }
                        }
                        model.setTags(presentTags);
                    }
                    for (int i = tags.length; i > 0; i--) {
                        for (WishListModel model : originalList) {
                            if (model.getTags().size() == i) {
                                filteredList.add(model);
                            }

                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();

                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if(results.count > 0){
                        setWishListModels((List<WishListModel>) results.values);
                    }
                    notifyDataSetChanged();
                }
            };
        }
    }

}
