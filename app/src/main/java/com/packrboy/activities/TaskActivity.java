package com.packrboy.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.packrboy.R;
import com.packrboy.adapters.ViewPagerAdapter;
import com.packrboy.classes.SharedPreferenceClass;
import com.packrboy.fragments.AvailableTaskFragment;
import com.packrboy.fragments.CompletedTaskFragment;
import com.packrboy.fragments.PendingTaskFragment;

public class TaskActivity extends AppCompatActivity {

    SharedPreferenceClass preferenceClass;

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
