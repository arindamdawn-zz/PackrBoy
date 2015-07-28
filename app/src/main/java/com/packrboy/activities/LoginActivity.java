package com.packrboy.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.packrboy.R;
import com.packrboy.classes.SharedPreferenceClass;
import com.packrboy.extras.Keys;
import com.packrboy.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.packrboy.extras.Keys.ServiceKeys.KEY_LOGGED_IN_USER;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_LOGIN_STATUS;
import static com.packrboy.extras.urlEndPoints.KEY_BASE_URL;
import static com.packrboy.extras.urlEndPoints.KEY_LOGIN;
import static com.packrboy.extras.urlEndPoints.KEY_TEST_URL;
import static com.packrboy.extras.urlEndPoints.KEY_UAT_BASE_URL;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_EMAIL;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout emailText, passwordText;
    EditText email, password;
    Button loginButton;
    TextView forgotPassword;
    RelativeLayout loginLayout;
    Toolbar toolbar;
    List<String> xxx;
    int size;
    String[] splitCookie, splitSessionId;
    SharedPreferenceClass preferenceClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        onClick();

        sendTokenRequest();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign in");
        setSupportActionBar(toolbar);
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
    }

    public void onClick() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationCheck()) {
                    sendLoginJsonRequest();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTestJsonRequest();
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
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public String getRequestUrl() {

        return "http://192.168.1.11/packr/public/api/csrf";
    }


    public void sendLoginJsonRequest() {
        final JsonObject loginObject = new JsonObject();
        final JsonObject jsonObject1 = new JsonObject();
        final JsonObject tokenObject = new JsonObject();
        try {
            jsonObject1.addProperty("email", email.getText().toString());
            jsonObject1.addProperty("password", password.getText().toString());
            loginObject.add("payload", jsonObject1);
            loginObject.addProperty("token", preferenceClass.getAccessToken());


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
                        xxx = result.getHeaders().getHeaders().getAll("Set-Cookie");
//
                        xxx = result.getHeaders().getHeaders().getAll("Set-Cookie");
                        size = xxx.size();
                        splitCookie = xxx.get(size - 1).split(";");
                        splitSessionId = splitCookie[0].split("=");

                        preferenceClass.saveCookie("laravel_session=" + splitSessionId[1]);

                        Log.e("error",result.getResult().toString());
                        parseJsonData(result.getResult());

                    }


                });
    }

    public static String getLoginRequestUrl() {
        return KEY_UAT_BASE_URL + KEY_LOGIN;
    }


    public void parseJsonData(JsonObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        if (jsonObject.has(KEY_LOGIN_STATUS)) {
            if (jsonObject.get(KEY_LOGIN_STATUS).getAsBoolean()) {
                Intent intent = new Intent(LoginActivity.this, TaskActivity.class);
                startActivity(intent);
                finish();

            } else {
                Log.e("error2","Invalid credentials");
                Snackbar.make(loginLayout, "Sorry you do not have an account with us. Go ahead create one and make life easy :)", Snackbar.LENGTH_LONG).show();
            }
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
        return KEY_UAT_BASE_URL + KEY_TEST_URL;
    }


}
