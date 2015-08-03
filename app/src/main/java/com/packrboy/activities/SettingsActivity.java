package com.packrboy.activities;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.packrboy.R;
import com.packrboy.services.CheckAvailableTask;

import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    Button smallNotification,bigNotification;
    private static int pendingNotificationsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Intent intent = new Intent(this, CheckAvailableTask.class);
        startService(intent);

        smallNotification = (Button)findViewById(R.id.notificationButton1);
        bigNotification = (Button)findViewById(R.id.notificationButton2);

        smallNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalNotification();
                stopService(new Intent(getBaseContext(), CheckAvailableTask.class));
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

       notifyMsgReceived("Arindam",0);

    }



    public void notifyMsgReceived(String senderName, int count) {
        int titleResId;
        String expandedText, sender;

        // Get the sender for the ticker text
        // i.e. "Message from <sender name>"
        if (senderName != null && TextUtils.isGraphic(senderName)) {
            sender = senderName;
        }
        else {
            // Use "unknown" if the sender is unidentifiable.
            sender = getString(R.string.abc_action_bar_home_description);
        }

        // Display the first line of the notification:
        // 1 new message: call name
        // more than 1 new message: <number of messages> + " new messages"
        if (count == 1) {
            titleResId = R.string.gcm_defaultSenderId;
            expandedText = sender;
        }
        else {
            titleResId = R.string.ga_trackingId;
            expandedText = getString(R.string.app_id, count);
        }

        Intent i = new Intent(SettingsActivity.this,NotificationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Event tracker")
                .setNumber(count)
                .setContentText(expandedText);



        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1882, mBuilder.build());
        notificationManager.cancel(1882);
        count = 0;
    }



    public void showBigPictureNotification(){


        //Assign a style of big notification text
        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.image_background);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Event tracker")
                .setContentText("Events received")
                .setNumber(++pendingNotificationsCount);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        String[] events = new String[6];
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");

// Moves events into the expanded layout
        for (int i=0; i < events.length; i++) {

            inboxStyle.addLine(events[i]);
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1882, mBuilder.build());

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showNormalNotification() {


        //Provide the explicit intent for the notification
        Intent i = new Intent(SettingsActivity.this,NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);


        //build the content of notification
        Notification.Builder builder = new Notification.Builder(SettingsActivity.this);
        builder.setContentTitle("Packr");
        builder.setContentText("A new pickup task is available in your area");
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setTicker("Packr.You have a new notification");
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setLights(0xff00ff00, 300, 100);
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        //Add the backstack using stacknuilder and set the intent to pending intent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(i);


        PendingIntent pi_main = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentIntent(pi_main);
        builder.setNumber(++pendingNotificationsCount);


        //Notification through notification manager
        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1243,builder.build());


    }
}
