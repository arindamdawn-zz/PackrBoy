package com.packrboy.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CheckAvailableTask extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public CheckAvailableTask() {
        super(CheckAvailableTask.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private String[] downloadData(String requestUrl) throws IOException, DownloadException {
        InputStream inputStream = null;

        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);

        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
        urlConnection.setRequestMethod("GET");

        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());

            String response = convertInputStreamToString(inputStream);

            String[] results = parseResult(response);

            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private String[] parseResult(String result) {

        String[] blogTitles = null;
        try {
            JSONObject response = new JSONObject(result);

            JSONArray posts = response.optJSONArray("posts");

            blogTitles = new String[posts.length()];

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");

                blogTitles[i] = title;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return blogTitles;
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}