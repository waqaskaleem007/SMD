package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {
    private List<HomePageModel> homePageModelList = new ArrayList<HomePageModel>();
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPosition = -1;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        this.recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.bannerSlider;
            case 1:
                return HomePageModel.stripAdBanner;
            case 2:
                return HomePageModel.horizontalProduct;
            case 3:
                return HomePageModel.gridProduct;
            default:
                return -1;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.bannerSlider:
                View bannerSliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_layout, parent, false);
                return new BannerSliderViewHolder(bannerSliderView);

            case HomePageModel.stripAdBanner:
                View stripAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdViewHolder(stripAdView);
            case HomePageModel.horizontalProduct:
                View horizontalProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductHolder(horizontalProductView);
            case HomePageModel.gridProduct:
                View gridProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewHolder(gridProductView);
            default:
                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.bannerSlider:
                List<SliderModel> sliderModels = homePageModelList.get(position).getSliderModels();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModels);
                break;
            case HomePageModel.stripAdBanner:
                String resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgroundColor();
                ((StripAdViewHolder) holder).setStripAd(resource, color);
                break;
            case HomePageModel.horizontalProduct:
                String layoutColor = homePageModelList.get(position).getBackgroundColor();
                List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModels();
                String title = homePageModelList.get(position).getTitle();
                List<WishListModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                ((HorizontalProductHolder) holder).setHorizontalProduct(horizontalProductScrollModelList, title, layoutColor, viewAllProductList);
                break;
            case HomePageModel.gridProduct:
                String gridColor = homePageModelList.get(position).getBackgroundColor();
                List<HorizontalProductScrollModel> gridProductList = homePageModelList.get(position).getHorizontalProductScrollModels();
                String gridTitle = homePageModelList.get(position).getTitle();
                ((GridProductViewHolder) holder).setGridProductLayout(gridProductList, gridTitle, gridColor);
                break;
        }
        if(lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        private ViewPager viewPager;
        private SliderAdapter sliderAdapter;
        private int currentPage;
        Timer timer;
        final private long delayTime = 3000;
        final private long periodTime = 3000;
        private List<SliderModel> arrangedList;

        @SuppressLint("ClickableViewAccessibility")
        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.banner_slider_viewPager);

        }

        @SuppressLint("ClickableViewAccessibility")
        private void setBannerSliderViewPager(final List<SliderModel> sliderModels) {
            currentPage = 2;
            if (timer != null) {
                timer.cancel();
            }
            arrangedList = new ArrayList<>();
            for (int i = 0; i < sliderModels.size(); i++) {
                arrangedList.add(i, sliderModels.get(i));
            }

            arrangedList.add(0, sliderModels.get(sliderModels.size() - 2));
            arrangedList.add(1, sliderModels.get(sliderModels.size() - 1));
            arrangedList.add(sliderModels.get(0));
            arrangedList.add(sliderModels.get(1));


            sliderAdapter = new SliderAdapter(arrangedList);
            viewPager.setClipToPadding(false);
            viewPager.setPageMargin(20);
            viewPager.setCurrentItem(currentPage);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (ViewPager.SCROLL_STATE_IDLE == state) {
                        PageLooper(arrangedList);
                    }
                }
            });
            StartBannerSlideShow(arrangedList);
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    PageLooper(arrangedList);
                    StopBannerSlideShow(arrangedList);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        StartBannerSlideShow(arrangedList);
                    }
                    return false;
                }
            });
            viewPager.setAdapter(sliderAdapter);
        }

        private void PageLooper(List<SliderModel> sliderModels) {
            if (currentPage == (sliderModels.size() - 2)) {
                currentPage = 2;
                viewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModels.size() - 3;
                viewPager.setCurrentItem(currentPage, false);
            }
        }

        private void StartBannerSlideShow(final List<SliderModel> sliderModels) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModels.size()) {
                        currentPage = 1;
                    }
                    viewPager.setCurrentItem(currentPage++);
                }
            };

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, delayTime, periodTime);
        }

        private void StopBannerSlideShow(List<SliderModel> sliderModelList) {
            timer.cancel();
        }

    }

    public class StripAdViewHolder extends RecyclerView.ViewHolder {
        private ImageView stripAd;
        private ConstraintLayout stripAdContainer;

        public StripAdViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);
            stripAd = itemView.findViewById(R.id.strip_ad_image);

        }

        private void setStripAd(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(stripAd);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout Container;
        private TextView horizontalLayoutTitle;
        private Button horizontalViewAllButton;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductHolder(@NonNull View itemView) {
            super(itemView);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalViewAllButton = itemView.findViewById(R.id.horizontal_scroll_view_all);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recycler_view);
            horizontalRecyclerView.setRecycledViewPool(recycledViewPool);
            Container = itemView.findViewById(R.id.Container);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setHorizontalProduct(List<HorizontalProductScrollModel> horizontalProductScrollModels, final String title, String color, final List<WishListModel> viewAllProductList) {
            Container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontalLayoutTitle.setText(title);
            if (horizontalProductScrollModels.size() > 8) {
                horizontalViewAllButton.setVisibility(View.VISIBLE);
                horizontalViewAllButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishListModels = viewAllProductList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code", 0);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            } else {
                horizontalViewAllButton.setVisibility(View.INVISIBLE);
            }

            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModels);
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(itemView.getContext());
            layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
            horizontalRecyclerView.setLayoutManager(layoutManager1);
            horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();
        }

    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout gridContainer;
        private TextView gridLayoutTitle;
        private Button gridLayoutButton;
        private GridLayout gridProductLayout;

        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutButton = itemView.findViewById(R.id.grid_product_layout_viewall_button);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);
            gridContainer = itemView.findViewById(R.id.grid_container);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setGridProductLayout(final List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color) {
            gridContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            gridLayoutTitle.setText(title);

            for (int i = 0; i < 4; i++) {
                ImageView productImage = gridProductLayout.getChildAt(i).findViewById(R.id.h_s_product_image);
                TextView productTitle = gridProductLayout.getChildAt(i).findViewById(R.id.h_s_product_name);
                TextView productDesc = gridProductLayout.getChildAt(i).findViewById(R.id.h_s_product_desc);
                TextView productPrice = gridProductLayout.getChildAt(i).findViewById(R.id.h_s_product_price);

                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(i).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.img_placeholder)).into(productImage);
                productTitle.setText(horizontalProductScrollModelList.get(i).getProductTitle());
                productDesc.setText(horizontalProductScrollModelList.get(i).getProductDesc());
                productPrice.setText("Rs." + horizontalProductScrollModelList.get(i).getProductPrice() + "/-");

                gridProductLayout.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                if (!title.equals("")) {
                    final int finalI = i;
                    gridProductLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                            productDetailsIntent.putExtra("PRODUCT_ID",horizontalProductScrollModelList.get(finalI).getProductId());
                            itemView.getContext().startActivity(productDetailsIntent);
                        }
                    });
                }
            }
            if (!title.equals("")) {
                gridLayoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.horizontalProductScrollModels = horizontalProductScrollModelList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code", 1);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            }
        }
    }

}
