package com.visittomm.mweather.models;

/**
 * Created by monmon on 1/3/16.
 */
public class City {

    String name = null;
    boolean selected = false;

    public City(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
