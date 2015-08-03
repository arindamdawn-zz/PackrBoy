package com.packrboy.activities;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.packrboy.R;
import com.packrboy.classes.SharedPreferenceClass;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout firstName,lastName,email,phoneNumber;
    private EditText firstNameText,lastNameText,emailText,phoneNumberText;
    private SharedPreferenceClass preferenceClass;
    private CircleImageView circleImageView;
    private TextView changePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("My profile");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
        initialize();
        onClick();

        Ion.with(getApplicationContext())
                .load("http://a5.files.biography.com/image/upload/c_fill,cs_srgb,dpr_1.0,g_face,h_300,q_80,w_300/MTE5NDg0MDU0OTM2NTg1NzQz.jpg")
                .withBitmap()
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .intoImageView(circleImageView);

        firstNameText.setText(preferenceClass.getFirstName());
        lastNameText.setText(preferenceClass.getLastName());
        emailText.setText(preferenceClass.getUserEmail());
        phoneNumberText.setText(preferenceClass.getUserPhoneNo());

        Log.e("email", preferenceClass.getUserEmail());
        Log.e("contact",preferenceClass.getUserPhoneNo());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
    }

    public void initialize(){
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        firstNameText = (EditText)findViewById(R.id.firstName__edit_text);
        lastNameText = (EditText)findViewById(R.id.lastName__edit_text);
        emailText = (EditText)findViewById(R.id.email__edit_text);
        phoneNumberText = (EditText)findViewById(R.id.mobile__edit_text);
        circleImageView = (CircleImageView) findViewById(R.id.profile_pic);
        changePassword = (TextView)findViewById(R.id.changePassword);
        disableEditText(firstNameText);
        disableEditText(lastNameText);
        disableEditText(emailText);
        disableEditText(phoneNumberText);
    }

    public void onClick() {
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
                alert.setTitle("Change Password");
                alert.setMessage("Please provide your new password");
                alert.setView(R.layout.change_password);
                alert.show();
            }
        });

        firstNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameText.setFocusable(true);
                firstNameText.setFocusableInTouchMode(true);

            }
        });
        lastNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastNameText.setFocusable(true);
                lastNameText.setFocusableInTouchMode(true);

            }
        });
        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText.setFocusable(true);
                emailText.setFocusableInTouchMode(true);

            }
        });
        phoneNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberText.setFocusable(true);
                phoneNumberText.setFocusableInTouchMode(true);

            }
        });
    }
}
