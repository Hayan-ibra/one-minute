package com.example.oneminute.models;

public class HotelRate {
    private String HotelId;
    private String RaterId;
    private Double subRate;

    public HotelRate() {
    }

    public String getHotelId() {
        return HotelId;
    }

    public void setHotelId(String hotelId) {
        HotelId = hotelId;
    }

    public String getRaterId() {
        return RaterId;
    }

    public void setRaterId(String raterId) {
        RaterId = raterId;
    }

    public Double getSubRate() {
        return subRate;
    }

    public void setSubRate(Double subRate) {
        this.subRate = subRate;
    }
}
