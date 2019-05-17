package com.urbler.wemet;

/**
 * Created by Alome on 5/7/2019.
 * WeMet
 */

public class timeLineApp {
    String p1;
    String p2;
    String topic;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;
    String likes;
    public timeLineApp(){
        //for fireBase...
    }
    public timeLineApp(String p1, String p2, String topic, String likes,String date) {
        this.p1 = p1;
        this.p2 = p2;
        this.topic = topic;
        this.likes = likes;
        this.date=date;
    }
    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
