package com.example.oneminute.models;

import java.io.Serializable;

public class Hotels implements Serializable {
    private static Hotels instance;
    private String hotelName;
    private String hotelId;
    private String ImageUrl;
    private String location;
    private Double rate;
    private String phoneNumber;
    private String description;

    private String city;
    private long numberOfPreviews;


    public Hotels() {
    }
    public static Hotels getInstance(){
        if (instance==null){
            instance=new Hotels();
        }
        return instance;
    }

    public long getNumberOfPreviews() {
        return numberOfPreviews;
    }

    public void setNumberOfPreviews(long numberOfPreviews) {
        this.numberOfPreviews = numberOfPreviews;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
