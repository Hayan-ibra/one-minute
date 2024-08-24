package com.example.oneminute.models;

public class Catigories  {
     private String catName;
     private int iconId;

    public Catigories(String catName, int iconId) {
        this.catName = catName;
        this.iconId = iconId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
