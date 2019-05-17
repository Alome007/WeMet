package com.urbler.wemet;

/**
 * Created by Alome on 5/4/2019.
 * WeMet
 */

public class registerPojo {
    public registerPojo(){
        //for firebase..
    }
    String name;
    String country;
    String state;
    String url;
    public registerPojo(String name, String country, String state, String url) {
        this.name = name;
        this.country = country;
        this.state = state;
        this.url=url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String geturl() {
        return url;
    }
}
