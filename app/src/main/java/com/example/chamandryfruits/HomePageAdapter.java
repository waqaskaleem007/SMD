package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {
    private List<HomePageModel> homePageModelList = new ArrayList<HomePageModel>();

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.bannerSlider:
                List<SliderModel> sliderModels = homePageModelList.get(position).getSliderModels();
                ((BannerSliderViewHolder) holder).setBannerSliderViewPager(sliderModels);
                break;
            case HomePageModel.stripAdBanner:
                int resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgroundColor();
                ((StripAdViewHolder) holder).setStripAd(resource, color);
                break;
            case HomePageModel.horizontalProduct:
                List<HorizontalProductScrollModel> horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModels();
                String title = homePageModelList.get(position).title;
                ((HorizontalProductHolder) holder).setHorizontalProduct(horizontalProductScrollModelList, title);
                break;
            case HomePageModel.gridProduct:
                List<HorizontalProductScrollModel> gridProductList = homePageModelList.get(position).getHorizontalProductScrollModels();
                String gridTitle = homePageModelList.get(position).getTitle();
                ((GridProductViewHolder) holder).setGridProductLayout(gridProductList, gridTitle);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        private ViewPager viewPager;
        private SliderAdapter sliderAdapter;
        private int currentPage = 2;
        Timer timer;
        final private long delayTime = 3000;
        final private long periodTime = 3000;


        @SuppressLint("ClickableViewAccessibility")
        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.banner_slider_viewPager);

        }

        @SuppressLint("ClickableViewAccessibility")
        private void setBannerSliderViewPager(final List<SliderModel> sliderModels) {

            sliderAdapter = new SliderAdapter(sliderModels);
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
                        PageLooper(sliderModels);
                    }
                }
            });
            StartBannerSlideShow(sliderModels);
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    PageLooper(sliderModels);
                    StopBannerSlideShow(sliderModels);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        StartBannerSlideShow(sliderModels);
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

        private void setStripAd(int resource, String color) {
            stripAd.setImageResource(resource);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductHolder extends RecyclerView.ViewHolder {
        private TextView horizontalLayoutTitle;
        private Button horizontalViewAllButton;
        private RecyclerView horizontalRecyclerView;

        public HorizontalProductHolder(@NonNull View itemView) {
            super(itemView);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalViewAllButton = itemView.findViewById(R.id.horizontal_scroll_view_all);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recycler_view);

        }

        private void setHorizontalProduct(List<HorizontalProductScrollModel> horizontalProductScrollModels, String title){
            horizontalLayoutTitle.setText(title);
            if(horizontalProductScrollModels.size() > 8){
                horizontalViewAllButton.setVisibility(View.VISIBLE);
            }
            else{
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

    public class GridProductViewHolder extends RecyclerView.ViewHolder{

        private TextView gridLayoutTitle;
        private Button gridLayoutButton;
        private GridView gridlayoutGridView;

        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridLayoutButton = itemView.findViewById(R.id.grid_product_layout_viewall_button);
            gridlayoutGridView = itemView.findViewById(R.id.grid_product_layout_girdview);

        }
        private void setGridProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModels, String title){
            gridLayoutTitle.setText(title);
            GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModels);
            gridlayoutGridView.setAdapter(gridProductLayoutAdapter);
            //gridProductLayoutAdapter.notifyDataSetChanged();
        }
    }

}
