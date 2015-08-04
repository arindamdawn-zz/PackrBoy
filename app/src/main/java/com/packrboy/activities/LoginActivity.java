package com.packrboy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.packrboy.BuildConfig;
import com.packrboy.R;
import com.packrboy.classes.Connectivity;
import com.packrboy.classes.DeviceInfo;
import com.packrboy.classes.SharedPreferenceClass;
import com.packrboy.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import static com.packrboy.extras.Keys.ServiceKeys.KEY_EMAIL;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_FIRST_NAME;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_GENDER;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_ID;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_LAST_NAME;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_LOGGED_IN_USER;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_LOGIN_STATUS;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_PHONE_NO;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_PROFILE_PIC;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_USER_TYPE;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_USER_TYPE_OBJECT;
import static com.packrboy.extras.urlEndPoints.KEY_LOGIN;
import static com.packrboy.extras.urlEndPoints.KEY_TEST_URL;
import static com.packrboy.extras.urlEndPoints.KEY_TOKEN;
import static com.packrboy.extras.urlEndPoints.KEY_UAT_BASE_URL;
import static com.packrboy.extras.urlEndPoints.KEY_UAT_BASE_URL_API;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailText, passwordText;
    EditText email, password;
    Button loginButton;
    TextView forgotPassword;
    RelativeLayout loginLayout;
    Toolbar toolbar;
    List<String> xxx;
    int size;
    LayoutInflater inflater;
    String[] splitCookie, splitSessionId;
    String userType, firstName, lastName, gender, imageURL, phoneNo, userId, userEmail;
    SharedPreferenceClass preferenceClass;
    RelativeLayout progressWheel;
    DeviceInfo deviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        onClick();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign in");
        setSupportActionBar(toolbar);

        if (preferenceClass.getLastUserEmail() != null) {

            email.setText(preferenceClass.getLastUserEmail());
            email.setFocusable(false);
            email.setFocusableInTouchMode(true);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void initialize() {
        emailText = (TextInputLayout) findViewById(R.id.email_text_input_layout);
        passwordText = (TextInputLayout) findViewById(R.id.password_text_input_layout);
        email = (EditText) findViewById(R.id.email_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        loginButton = (Button) findViewById(R.id.loginButton);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);
        preferenceClass = new SharedPreferenceClass(getApplicationContext());
        progressWheel = (RelativeLayout) findViewById(R.id.progress_wheel);
        deviceInfo = new DeviceInfo();
    }

    public void onClick() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationCheck()) {
                    sendTokenRequest();
                    if (preferenceClass.getAccessToken() != null && preferenceClass.getAccessToken().length() != 0) {
                        sendLoginJsonRequest();
                    }
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle("Provide your email");
                alert.setMessage("A password reset link will be sent to your email id");
                alert.setView(R.layout.forgot_password);
                alert.show();
            }
        });
    }

    public Boolean validationCheck() {
        if (email.getText().length() == 0) {
            emailText.setErrorEnabled(true);
            emailText.setError("Email cannot be left empty");
        } else if (password.getText().length() < 6) {
            passwordText.setErrorEnabled(true);
            passwordText.setError("Password must be of minimum 6 characters");
        } else {
            return true;
        }
        return false;
    }


    public void sendTokenRequest() {
        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getRequestUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Value", s);
                preferenceClass.saveAccessToken(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    Snackbar.make(loginLayout, "Something is wrong with your internet connection. Please check your settings", Snackbar.LENGTH_LONG).show();

                } else if (volleyError instanceof AuthFailureError) {

                    //TODO
                } else if (volleyError instanceof ServerError) {

                    //TODO
                } else if (volleyError instanceof NetworkError) {
                    Snackbar.make(loginLayout, "Something is wrong with your internet connection. Please check your settings", Snackbar.LENGTH_LONG).show();

                    //TODO
                } else if (volleyError instanceof ParseError) {

                    //TODO
                }

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public String getRequestUrl() {

        return KEY_UAT_BASE_URL_API + KEY_TOKEN;
    }


    public void sendLoginJsonRequest() {
        if (!Connectivity.isConnected(getApplicationContext())) {
            Snackbar.make(loginLayout, "Something is wrong with your internet connection. Please check your settings", Snackbar.LENGTH_LONG).show();
        } else {
            progressWheel.setVisibility(View.VISIBLE);
            final JsonObject loginObject = new JsonObject();
            final JsonObject jsonObject1 = new JsonObject();
            final JsonObject deviceObject = new JsonObject();
            try {
                jsonObject1.addProperty("email", email.getText().toString());
                jsonObject1.addProperty("password", password.getText().toString());
                loginObject.add("payload", jsonObject1);
                loginObject.addProperty("token", preferenceClass.getAccessToken());
                deviceObject.addProperty("device_name", deviceInfo.getDeviceName());
                deviceObject.addProperty("device_type", "ANDROID");
                deviceObject.addProperty("device_os_ver", deviceInfo.getDeviceOSVersion());
                deviceObject.addProperty("device_resolution", getResources().getDisplayMetrics().widthPixels + "*" + getResources().getDisplayMetrics().heightPixels);
                deviceObject.addProperty("device_token", preferenceClass.getDeviceToken());
                deviceObject.addProperty("app_ver", BuildConfig.VERSION_NAME);
                deviceObject.addProperty("lon", "");
                deviceObject.addProperty("lat", "");
                deviceObject.addProperty("user_id", "");
                jsonObject1.add("device", deviceObject);

            } catch (JsonIOException e) {
                e.printStackTrace();
            }

            Ion.with(getApplicationContext())
                    .load(getLoginRequestUrl())
                    .setJsonObjectBody(loginObject)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<com.koushikdutta.ion.Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, com.koushikdutta.ion.Response<JsonObject> result) {
                            progressWheel.setVisibility(View.GONE);
                            if (e != null) {
                                Log.e("exception", e.getLocalizedMessage() + " " + e.getLocalizedMessage());
                                hideKeyboard();
                                Snackbar.make(loginLayout, "We are facing problems in connecting to our servers. Just chill and try after some time :)", Snackbar.LENGTH_LONG).show();
                            } else if (result != null) {
                                xxx = result.getHeaders().getHeaders().getAll("Set-Cookie");
//
                                xxx = result.getHeaders().getHeaders().getAll("Set-Cookie");
                                if (xxx.size() != 0) {
                                    size = xxx.size();
                                    splitCookie = xxx.get(size - 1).split(";");
                                    splitSessionId = splitCookie[0].split("=");

                                    preferenceClass.saveCookie("laravel_session=" + splitSessionId[1]);

                                }
                                parseJsonData(result.getResult());
                            } else {
                                Log.e("Exception", "There is a problem");
                                hideKeyboard();
                                Snackbar.make(loginLayout, "Something is wrong with the internet connection", Snackbar.LENGTH_LONG).show();
                            }


                        }


                    });

        }
    }

    public static String getLoginRequestUrl() {
        return KEY_UAT_BASE_URL_API + KEY_LOGIN;
    }


    public void parseJsonData(JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        if (jsonObject.has(KEY_LOGGED_IN_USER)) {
            JsonObject loggedInUserObject = jsonObject.getAsJsonObject(KEY_LOGGED_IN_USER);
            firstName = loggedInUserObject.get(KEY_FIRST_NAME).getAsString();
            preferenceClass.saveFirstName(firstName);
            lastName = loggedInUserObject.get(KEY_LAST_NAME).getAsString();
            preferenceClass.saveLastName(lastName);
            gender = loggedInUserObject.get(KEY_GENDER).getAsString();
            userEmail = loggedInUserObject.get(KEY_EMAIL).getAsString();
            preferenceClass.saveLastUserEmail(userEmail);
            preferenceClass.saveUserEmail(userEmail);
            phoneNo = loggedInUserObject.get(KEY_PHONE_NO).getAsString();
            preferenceClass.saveUserPhoneNo(phoneNo);
            userType = loggedInUserObject.get(KEY_USER_TYPE).getAsString();
            if (loggedInUserObject.get(KEY_PROFILE_PIC).getAsString() != null) {
                imageURL = loggedInUserObject.get(KEY_PROFILE_PIC).getAsString();
            }

            preferenceClass.saveProfileImageUrl(KEY_UAT_BASE_URL + imageURL);


            if (loggedInUserObject.has(KEY_USER_TYPE_OBJECT)) {
                JsonObject userTypeObject = loggedInUserObject.getAsJsonObject(KEY_USER_TYPE_OBJECT);
                userId = userTypeObject.get(KEY_ID).getAsString();
                preferenceClass.saveCustomerId(userId);

            }

        }
        if (jsonObject.has(KEY_LOGIN_STATUS)) {
            if (jsonObject.get(KEY_LOGIN_STATUS).getAsBoolean() && userType.contentEquals("packrboy")) {
                Intent intent = new Intent(LoginActivity.this, TaskActivity.class);
                startActivity(intent);
                finish();

            } else {
                Log.e("error2", "Invalid credentials");
                hideKeyboard();
                Snackbar.make(loginLayout, "Sorry you do not have an account with us. Go ahead create one and make life easy :)", Snackbar.LENGTH_LONG).show();
            }
        }


    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void sendTestJsonRequest() {
        final JSONObject testObject = new JSONObject();
        try {
            testObject.put("token", preferenceClass.getAccessToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getTestRequestUrl(), testObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("error", jsonObject.toString());
                Log.i("login", testObject.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                    Snackbar.make(loginLayout, "Something is wrong with your internet connection. Please check your settings", Snackbar.LENGTH_LONG).show();


                } else if (volleyError instanceof AuthFailureError) {

                    //TODO
                } else if (volleyError instanceof ServerError) {

                    //TODO
                } else if (volleyError instanceof NetworkError) {

                    //TODO
                } else if (volleyError instanceof ParseError) {

                    //TODO
                }

            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<String, String>();
                }

                headers.put("Cookie", preferenceClass.getCookie());
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public static String getTestRequestUrl() {
        return KEY_UAT_BASE_URL_API + KEY_TEST_URL;
    }


}
