package com.example.chamandryfruits;

public class AddressesModel {
    private String fullName;
    private String mobileNo;
    private String address;
    private String pinCode;
    private boolean selected;
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public AddressesModel(String fullName, String address, String pinCode, boolean selected, String mobileNo) {
        this.fullName = fullName;
        this.address = address;
        this.pinCode = pinCode;
        this.selected = selected;
        this.mobileNo = mobileNo;
    }
}
