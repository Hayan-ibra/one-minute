package com.example.oneminute.models;

import java.io.Serializable;

public class RestaurantProfile  implements Serializable {





    private static RestaurantProfile instance;


    public static RestaurantProfile getInstance() {
        if (instance == null) {
            instance = new RestaurantProfile();
        }
        return instance;
    }


    private  String time_from_int;
    private String time_to_int;





    private String city;
    private String location;
    private String name;
    private String workTime_from;
    private String workTime_to;
    private double rate;
    private String phoneNum;
    private String stateOfReserve;
    private String url;
    private String description;

    private String resuid;

    private String parent_user_id;

    public RestaurantProfile() {
    }




    public static void setInstance(RestaurantProfile instance) {
        RestaurantProfile.instance = instance;
    }

    public String getTime_from_int() {
        return time_from_int;
    }

    public void setTime_from_int(String time_from_int) {
        this.time_from_int = time_from_int;
    }

    public String getTime_to_int() {
        return time_to_int;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkTime_from() {
        return workTime_from;
    }

    public void setWorkTime_from(String workTime_from) {
        this.workTime_from = workTime_from;
    }

    public String getWorkTime_to() {
        return workTime_to;
    }

    public void setWorkTime_to(String workTime_to) {
        this.workTime_to = workTime_to;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getStateOfReserve() {
        return stateOfReserve;
    }

    public void setStateOfReserve(String stateOfReserve) {
        this.stateOfReserve = stateOfReserve;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResuid() {
        return resuid;
    }

    public void setResuid(String resuid) {
        this.resuid = resuid;
    }

    public String getParent_user_id() {
        return parent_user_id;
    }

    public void setParent_user_id(String user_res_uid) {
        this.parent_user_id = user_res_uid;
    }





}
