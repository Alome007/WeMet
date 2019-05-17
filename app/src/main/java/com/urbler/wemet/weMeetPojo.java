package com.urbler.wemet;

/**
 * Created by Alome on 3/29/2019.
 * 2019 Urbler
 */

public class weMeetPojo {
    private String friendsName;
    private String friendsPicUrl;
    private String date;
    private String location;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public weMeetPojo(String friendsName, String friendsPicUrl, String date, String location, String type, String id) {
        this.friendsName = friendsName;
        this.friendsPicUrl = friendsPicUrl;
        this.date = date;
        this.location = location;
        this.type = type;
        this.id=id;
    }

    public weMeetPojo() {
        //for firebase
    }


    public String getFriendsName() {
        return friendsName;
    }

    public void setFriendsName(final String friendsName) {
        this.friendsName = friendsName;
    }

    public String getFriendsPicUrl() {
        return friendsPicUrl;
    }

    public void setFriendsPicUrl(final String friendsPicUrl) {
        this.friendsPicUrl = friendsPicUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }
}

