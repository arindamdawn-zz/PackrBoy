package com.packrboy.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.packrboy.R;

public class SettingsActivity extends AppCompatActivity {
    Button smallNotification,bigNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        smallNotification = (Button)findViewById(R.id.notificationButton1);
        bigNotification = (Button)findViewById(R.id.notificationButton2);

        smallNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalNotification();
            }
        });

        bigNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBigPictureNotification();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showBigNotification(){

        //Assign a style of big notification text
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.setBigContentTitle("Big style Packr Notification");
        style.bigText("You have a very big notification");

        //build the content of notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SettingsActivity.this);
        builder.setContentTitle("PackrMate");
        builder.setContentText("A new pickup task is available in your area.This is a notification that can show a large amount of text as compared to normal notification");
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setTicker("Packr.You have a new notification");
        builder.setAutoCancel(true);
        builder.setStyle(style);


        //Notification through notification manager
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(987, notification);

    }

    public void showBigPictureNotification(){

        //Assign a style of big notification text
        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.image_background);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("Big style Packr Notification");
        style.bigPicture(bmp);

        //build the content of notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SettingsActivity.this);
        builder.setContentTitle("PackrMate");
        builder.setContentText("A new pickup task is available in your area.This is a notification that can show a large amount of text as compared to normal notification");
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setTicker("Packr.You have a new notification");
        builder.setAutoCancel(true);
        builder.setStyle(style);


        //Notification through notification manager
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(987, notification);

    }


    public void showNormalNotification(){
        //build the content of notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(SettingsActivity.this);
        builder.setContentTitle("Packr");
        builder.setContentText("A new pickup task is available in your area");
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setTicker("Packr.You have a new notification");
        builder.setAutoCancel(true);

        //Provide the explicit intent for the notification
        Intent i = new Intent(SettingsActivity.this,NotificationActivity.class);

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
}
