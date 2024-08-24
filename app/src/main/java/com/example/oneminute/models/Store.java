package com.example.oneminute.models;

import java.io.Serializable;
import java.util.List;

public class Store implements Serializable {

    private  static Store instance;
    private String storeId;
    private String storeName;
    private String imageUrl;
    private String workTimeFrom;
    private String workTimeTo;

    private String location;
    private List<String> storeType;
    private String phoneNum;
    private String description;


    public static Store getInstance(){
        if (instance==null){
            instance=new Store();
        }
        return instance;
    }


    public Store() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getWorkTimeFrom() {
        return workTimeFrom;
    }

    public void setWorkTimeFrom(String workTimeFrom) {
        this.workTimeFrom = workTimeFrom;
    }

    public String getWorkTimeTo() {
        return workTimeTo;
    }

    public void setWorkTimeTo(String workTimeTo) {
        this.workTimeTo = workTimeTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getStoreType() {
        return storeType;
    }

    public void setStoreType(List<String> storeType) {
        this.storeType = storeType;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
