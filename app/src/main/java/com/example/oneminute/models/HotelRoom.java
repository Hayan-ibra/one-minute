package com.example.oneminute.models;

import java.io.Serializable;
import java.util.List;

public class HotelRoom implements Serializable {
    private String roomNum;
    private String parentId;
    private String roomId;
    private String imageUrl;
    private Double pricePerDay;
    private String type;
    private long state;

    private static  HotelRoom instance;

    private List<String> services;

    public HotelRoom() {
    }

    public static HotelRoom getInstance(){
        if (instance==null){
            instance=new HotelRoom();
        }
        return instance;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public List<String> getServices() {
        return services;
    }
}
