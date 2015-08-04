package com.packrboy.classes;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.packrboy.R;
import com.packrboy.activities.LauncherActivity;
import com.packrboy.activities.NotificationActivity;
import com.packrboy.database.DBTasks;

import eu.inloop.easygcm.GcmListener;

/**
 * Created by addy on 28/06/15.
 */
public class PackrBoy extends Application implements GcmListener {

    public static PackrBoy sInstance;
    private static final String TAG = "Packrmate";
    private SharedPreferenceClass preferenceClass;
    String pushMessage;
    private static DBTasks mDatabase;
    int counter;


    public synchronized static DBTasks getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBTasks(getAppContext());
        }
        return mDatabase;
    }

    public static PackrBoy getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        preferenceClass = new SharedPreferenceClass(getAppContext());
        mDatabase = new DBTasks(this);

    }

    @Override
    public void onMessage(String s, Bundle bundle) {
        Log.v(TAG, "### message from: " + s);
        Log.v(TAG, "### data: " + s);
        for (String key : bundle.keySet()) {
            Log.v(TAG, "> " + key + ": " + bundle.get(key));
            if (key.contentEquals("message"))
            pushMessage = bundle.get(key).toString();
        }

        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon);
        //build the content of notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Packr");
        builder.setContentText(pushMessage);
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setLargeIcon(bmp);
        builder.setTicker("Packr.You have a new notification");
        builder.setAutoCancel(true);
        builder.setNumber(++counter);
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        //Provide the explicit intent for the notification
        Intent i = new Intent(this,NotificationActivity.class);

        //Add the backstack using stacknuilder and set the intent to pending intent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(i);

        PendingIntent pi_main = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pi_main);

        //Notification through notification manager
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1243,notification);

    }

    @Override
    public void sendRegistrationIdToBackend(String registrationId) {
        Log.v(TAG, "### registration id: " + registrationId);
        preferenceClass.saveDeviceToken(registrationId);


    }
}
