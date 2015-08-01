package com.packrboy.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.packrboy.R;
import com.packrboy.adapters.ViewPagerAdapter;
import com.packrboy.classes.SharedPreferenceClass;
import com.packrboy.fragments.AvailableTaskFragment;
import com.packrboy.fragments.CompletedTaskFragment;
import com.packrboy.fragments.PendingTaskFragment;

public class TaskActivity extends AppCompatActivity {

    SharedPreferenceClass preferenceClass;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initialize();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hello"+ " "+ preferenceClass.getFirstName()+"!");
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AvailableTaskFragment(), "Available Tasks");
        adapter.addFrag(new PendingTaskFragment(), "Pending Tasks");
        adapter.addFrag(new CompletedTaskFragment(), "Completed Tasks");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())super.onBackPressed();
        else Toast.makeText(getBaseContext(),"Press once again to exit",Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_notifications){
            Intent intent = new Intent(TaskActivity.this,NotificationActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_Logout){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Logout");
            alert.setMessage(R.string.confirm_logout);
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Cancel
                }
            });
            alert.show();
        } else if (id == R.id.action_settings){
            Intent intent = new Intent(TaskActivity.this,SettingsActivity.class);
            startActivity(intent);
        }






        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    public void initialize(){
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
    }

    public void onClick(){

    }
}
