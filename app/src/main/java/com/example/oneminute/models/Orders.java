package com.example.oneminute.models;

import com.google.firebase.Timestamp;

public class Orders {
    String userId;
    String resId;
    String username;
    String foodName;

    String specialNotes;
    long state;

    Double price;
    String resReply;
    Timestamp time;
    String timeForDelete;

    long numberOfItems;

    String imageUrl;
    String phoneNum;

    public Orders() {

    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(long numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
    }


    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public String getResReply() {
        return resReply;
    }

    public void setResReply(String resReply) {
        this.resReply = resReply;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getTimeForDelete() {
        return timeForDelete;
    }

    public void setTimeForDelete(String timeForDelete) {
        this.timeForDelete = timeForDelete;
    }
}
