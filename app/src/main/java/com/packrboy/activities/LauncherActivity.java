package com.packrboy.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.packrboy.R;
import com.packrboy.classes.SharedPreferenceClass;

import eu.inloop.easygcm.GcmHelper;

public class LauncherActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000 ;
    private SharedPreferenceClass preferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        GcmHelper.init(this);
        initialize();
        onClick();


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (preferenceClass.getAccessToken() != null && preferenceClass.getAccessToken().length() != 0 && preferenceClass.getCustomerId() != null && preferenceClass.getCustomerId().length() != 0){
                Intent i = new Intent(LauncherActivity.this, TaskActivity.class);
                startActivity(i);}

                else {
                    Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
    @Override
    public void onResume(){
        super.onResume();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    public void initialize(){
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
    }

    public void onClick(){

    }
}
