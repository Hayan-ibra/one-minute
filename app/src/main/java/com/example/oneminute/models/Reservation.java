package com.example.oneminute.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Reservation {
    private String roomId;
    private String userId;
    private String userName;
    private String docId;

    private String roomName;
    private String hotelName;


    private ArrayList<Timestamp> date;

    Double endPrice;

    public Reservation() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Double getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(Double endPrice) {
        this.endPrice = endPrice;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public ArrayList<Timestamp> getDate() {
        return date;
    }

    public void setDate(ArrayList<Timestamp> date) {
        this.date = date;
    }
}
