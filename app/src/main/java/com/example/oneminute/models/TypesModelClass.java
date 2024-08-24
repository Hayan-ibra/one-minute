package com.example.oneminute.models;

public class TypesModelClass {
    int icon;
    String name;

    String realName;



    public TypesModelClass(int icon, String name, String realName) {
        this.icon = icon;
        this.name = name;
        this.realName = realName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
