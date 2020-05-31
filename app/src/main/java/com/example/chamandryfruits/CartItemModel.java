package com.example.chamandryfruits;

public class CartItemModel {

    public static final int CART_ITEM = 0;
    public static final int TOTAL_AMOUNT = 1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    ////cart item
    private String productId;
    private String productImage;
    private String productTitle;
    private Long productQuantity;
    private String productPrice;
    private Long freeCoupons;
    private Long couponsApplied;
    private String cuttedPrice;
    private Long offersApplied;


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public CartItemModel(int type, String productId, String productImage, String productTitle, Long productQuantity, String productPrice, Long freeCoupons, Long couponsApplied, String cuttedPrice, Long offersApplied) {
        this.type = type;
        this.productId = productId;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.freeCoupons = freeCoupons;
        this.couponsApplied = couponsApplied;
        this.cuttedPrice = cuttedPrice;
        this.offersApplied = offersApplied;
    }

    public static int getCartItem() {
        return CART_ITEM;
    }

    public static int getTotalAmount() {
        return TOTAL_AMOUNT;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Long getFreeCoupons() {
        return freeCoupons;
    }

    public void setFreeCoupons(Long freeCoupons) {
        this.freeCoupons = freeCoupons;
    }

    public Long getCouponsApplied() {
        return couponsApplied;
    }

    public void setCouponsApplied(Long couponsApplied) {
        this.couponsApplied = couponsApplied;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    public Long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(Long offersApplied) {
        this.offersApplied = offersApplied;
    }

////cart item

    /////cart Total amount
    public CartItemModel(int type) {
        this.type = type;
    }
    /////cart Total amount





}
