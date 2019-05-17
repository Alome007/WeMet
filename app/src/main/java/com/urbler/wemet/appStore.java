package com.urbler.wemet;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alome on 5/15/2019.
 * WeMet
 */

public class appStore extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
