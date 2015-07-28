package com.packrboy.network;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.packrboy.classes.PackrBoy;

/**
 * Created by Arindam on 07-Feb-15.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(PackrBoy.getAppContext());
        imageLoader = new ImageLoader(mRequestQueue,new ImageLoader.ImageCache() {
            private android.util.LruCache<String,Bitmap> cache = new android.util.LruCache<>((int)(Runtime.getRuntime().maxMemory()/1024/8));
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });

    }
    public static VolleySingleton getsInstance(){
        if (sInstance == null){
            sInstance = new VolleySingleton();
        }
        return  sInstance;

    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? "TAG" : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }


}
