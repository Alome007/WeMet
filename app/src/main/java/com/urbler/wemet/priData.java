package com.urbler.wemet;

/**
 * Created by Alome on 5/6/2019.
 * WeMet
 */

public class priData {
    String tilte;
    String des;
    String myId;
    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public priData(){
    //for firebase...
}
    public priData(String tilte, String des, String myId) {
        this.tilte = tilte;
        this.des = des;
        this.myId=myId;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
