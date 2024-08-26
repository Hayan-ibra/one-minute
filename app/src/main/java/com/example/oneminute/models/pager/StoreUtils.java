package com.example.oneminute.models.pager;

import java.io.Serializable;
import java.util.List;

public class StoreUtils implements Serializable {
    private String title;
    private String searchStore;
    private List<PagerProperties> catigories;

    private int icon;
    private int image;

    public int getIcon() {
        return icon;
    }

    public StoreUtils(String title, String searchStore, List<PagerProperties> catigories, int icon, int image) {
        this.title = title;
        this.searchStore = searchStore;
        this.catigories = catigories;
        this.icon = icon;
        this.image = image;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSearchStore() {
        return searchStore;
    }

    public void setSearchStore(String searchStore) {
        this.searchStore = searchStore;
    }

    public List<PagerProperties> getCatigories() {
        return catigories;
    }

    public void setCatigories(List<PagerProperties> catigories) {
        this.catigories = catigories;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
