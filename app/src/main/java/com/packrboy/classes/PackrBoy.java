package com.packrboy.classes;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.inloop.easygcm.GcmListener;

/**
 * Created by addy on 28/06/15.
 */
public class PackrBoy extends Application implements GcmListener {

    public static PackrBoy sInstance;
    private static final String TAG = "Packrmate";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }

    public static PackrBoy getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


    @Override
    public void onMessage(String s, Bundle bundle) {
        Log.v(TAG, "### message from: " + s);
        Log.v(TAG, "### data: " + s);
        for (String key : bundle.keySet()) {
            Log.v(TAG, "> " + key + ": " + bundle.get(key));
        }

    }

    @Override
    public void sendRegistrationIdToBackend(String s) {
        Log.v(TAG, "### registration id: " + s);

    }
}
