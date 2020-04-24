package com.example.chamandryfruits;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    /////Banner slider code
    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;
    List<SliderModel> sliderModels = new ArrayList<SliderModel>();
    private int currentPage = 2;
    Timer timer;
    final private long delayTime = 3000;
    final private long periodTime = 3000;
    ////Banner slider code

    ////strip add layout
    private ImageView stripAd;
    private ConstraintLayout stripAdContainer;
    ////strip add layout

    ////Horizontal Product Layout
    private TextView horizontalLayoutTitle;
    private Button horizontalViewAllButton;
    private RecyclerView horizontalRecyclerView;
    ////Horizontal Product Layout

    ////Grid Product layout
    private TextView gridLayoutTitle;
    private Button gridLayoutButton;
    private GridView gridlayoutGridView;
    ////Grid Product layout



    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        recyclerView = view.findViewById(R.id.category_recyclerview);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        List<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
        categoryModels.add(new CategoryModel("link", "Home"));
        categoryModels.add(new CategoryModel("link", "Electronics"));
        categoryModels.add(new CategoryModel("link", "Furniture"));
        categoryModels.add(new CategoryModel("link", "Appliances"));
        categoryModels.add(new CategoryModel("link", "Fashion"));
        categoryModels.add(new CategoryModel("link", "Mobiles"));
        categoryModels.add(new CategoryModel("link", "Appliances"));
        categoryModels.add(new CategoryModel("link", "Fashion"));
        categoryModels.add(new CategoryModel("link", "Mobiles"));
        categoryModels.add(new CategoryModel("link", "Appliances"));
        categoryModels.add(new CategoryModel("link", "Fashion"));
        categoryModels.add(new CategoryModel("link", "Mobiles"));

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModels);
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        ////Banner slider Code
        viewPager = view.findViewById(R.id.banner_slider_viewPager);

        sliderModels.add(new SliderModel(R.mipmap.home_icon,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.my_orders,"#077AE4"));

        sliderModels.add(new SliderModel(R.mipmap.banner,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.group_375,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.add_user,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.bell,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.custom_error_icon,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.forgot_password_image,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.app_icon,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.logo,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.cart_black,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.home_icon,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.my_orders,"#077AE4"));

        sliderModels.add(new SliderModel(R.mipmap.banner,"#077AE4"));
        sliderModels.add(new SliderModel(R.mipmap.group_375,"#077AE4"));


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
                if(ViewPager.SCROLL_STATE_IDLE == state){
                    PageLooper();
                }
            }
        });
        StartBannerSlideShow();
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PageLooper();
                StopBannerSlideShow();
                if(event.getAction() == MotionEvent.ACTION_UP){
                    StartBannerSlideShow();
                }
                return false;
            }
        });
        viewPager.setAdapter(sliderAdapter);


        ////Banner slider Code

        /////strip ad code
        stripAdContainer = view.findViewById(R.id.strip_ad_container);
        stripAd = view.findViewById(R.id.strip_ad_image);
        stripAd.setImageResource(R.drawable.strip_ad);
        stripAdContainer.setBackgroundColor(Color.parseColor("#000000"));



        /////strip ad code

        ///Horizontal view Layout
        horizontalLayoutTitle = view.findViewById(R.id.horizontal_scroll_layout_title);
        horizontalViewAllButton = view.findViewById(R.id.horizontal_scroll_view_all);
        horizontalRecyclerView = view.findViewById(R.id.horizontal_scroll_layout_recycler_view);

        List<HorizontalProductScrollModel> horizontalProductScrollModels = new ArrayList<>();
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.phone_image,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.add_user,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.cart_black,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.logo,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.bell,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.add_user_2,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.forgot_password_image,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));
        horizontalProductScrollModels.add(new HorizontalProductScrollModel(R.mipmap.ic_launcher,"Redmi 5A", "SnapDragon 425 Processor","5999/-"));


        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModels);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        horizontalRecyclerView.setLayoutManager(layoutManager1);
        horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);
        horizontalProductScrollAdapter.notifyDataSetChanged();

        ///Horizontal view Layout

        ////Grid Product layout
        gridLayoutTitle = view.findViewById(R.id.grid_product_layout_title);
        gridLayoutButton = view.findViewById(R.id.grid_product_layout_viewall_button);
        gridlayoutGridView = view.findViewById(R.id.grid_product_layout_girdview);

        GridProductLayoutAdapter gridProductLayoutAdapter = new GridProductLayoutAdapter(horizontalProductScrollModels);
        gridlayoutGridView.setAdapter(gridProductLayoutAdapter);
        gridProductLayoutAdapter.notifyDataSetChanged();

        ////Grid Product layout




        return view;
    }

    ////Banner slider Code
    private void PageLooper(){
        if(currentPage == (sliderModels.size()-2)){
            currentPage = 2;
            viewPager.setCurrentItem(currentPage,false);
        }
        if(currentPage == 1){
            currentPage = sliderModels.size()-3;
            viewPager.setCurrentItem(currentPage,false);
        }
    }
    private void StartBannerSlideShow(){
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentPage >= sliderModels.size()){
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
        },delayTime,periodTime);
    }
    private void StopBannerSlideShow(){
        timer.cancel();
    }


    ////Banner slider Code


}
