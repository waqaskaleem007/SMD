package com.example.chamandryfruits;

import java.util.ArrayList;
import java.util.List;

public class HomePageModel {

    final static public int bannerSlider = 0;
    final static public int stripAdBanner = 1;
    final static public int horizontalProduct = 2;
    final static public int gridProduct = 3;

    private int type;

    /////Banner slider code
    List<SliderModel> sliderModels = new ArrayList<SliderModel>();

    public List<SliderModel> getSliderModels() {
        return sliderModels;
    }

    public void setSliderModels(List<SliderModel> sliderModels) {
        this.sliderModels = sliderModels;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public HomePageModel(int type, List<SliderModel> sliderModels) {
        this.sliderModels = sliderModels;
        this.type = type;
    }
    /////Banner slider code

    ////strip add layout
    private String resource;
    private String backgroundColor;

    public HomePageModel(int type, String resource, String backgroundColor) {
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    ////strip add layout

    ////Horizontal Product Layout && grid product layout
    String title;
    List<HorizontalProductScrollModel> horizontalProductScrollModels = new ArrayList<>();

    public HomePageModel(int type, String title, List<HorizontalProductScrollModel> horizontalProductScrollModels) {
        this.type = type;
        this.title = title;
        this.horizontalProductScrollModels = horizontalProductScrollModels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProductScrollModel> getHorizontalProductScrollModels() {
        return horizontalProductScrollModels;
    }

    public void setHorizontalProductScrollModels(List<HorizontalProductScrollModel> horizontalProductScrollModels) {
        this.horizontalProductScrollModels = horizontalProductScrollModels;
    }
    ////Horizontal Product Layout && grid product layout


}
