package com.example.oneminute.models.pager;

import java.io.Serializable;

public class PagerProperties implements Serializable {
    private String name;
    private int icon;

    public PagerProperties(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
