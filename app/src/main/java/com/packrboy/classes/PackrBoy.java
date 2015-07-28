package com.packrboy.classes;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by addy on 28/06/15.
 */
public class PackrBoy extends Application {

    public static PackrBoy sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }

    public static PackrBoy getInstance(){
        return sInstance;
    }
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }


}
