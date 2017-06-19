package com.phacsin.educonn_school;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by GD on 1/31/2017.
 */

public class StudentApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
