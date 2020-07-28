package com.example.chamandryfruits;

import com.google.firebase.Timestamp;

public class RewardsModel {
    private String type;
    private String lowerLimit;
    private String upperLimit;
    private String discount;
    private String couponBody;
    private Timestamp timestamp;
    private boolean alreadyUsed;
    private String couponId;

    public RewardsModel(String couponId, String type, String lowerLimit, String upperLimit, String discount, String couponBody, Timestamp timestamp, boolean alreadyUsed) {
        this.type = type;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.discount = discount;
        this.couponBody = couponBody;
        this.timestamp = timestamp;
        this.alreadyUsed = alreadyUsed;
        this.couponId = couponId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public boolean isAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCouponBody() {
        return couponBody;
    }

    public void setCouponBody(String couponBody) {
        this.couponBody = couponBody;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
