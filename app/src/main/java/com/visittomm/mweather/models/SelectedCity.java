package com.visittomm.mweather.models;

/**
 * Created by monmon on 1/4/16.
 */
public class SelectedCity {
    String name = null;
    public SelectedCity(String name) {
        super();
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
