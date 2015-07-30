package com.packrboy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
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
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.packrboy.R;
import com.packrboy.activities.TaskActivity;
import com.packrboy.adapters.TaskAdapter;
import com.packrboy.classes.SharedPreferenceClass;
import com.packrboy.classes.Shipment;
import com.packrboy.network.VolleySingleton;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.packrboy.extras.Keys.Shipment.KEY_CREATED_AT;
import static com.packrboy.extras.Keys.Shipment.KEY_ID;
import static com.packrboy.extras.Keys.Shipment.KEY_IN_TRANSIT_STATUS;
import static com.packrboy.extras.Keys.Shipment.KEY_ITEM_IMAGE;
import static com.packrboy.extras.Keys.Shipment.KEY_ITEM_QUANTITY;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_CITY;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_LATITUDE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_LONGITUDE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_POSTAL_CODE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_ROUTE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_STATE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_STREET_NO;
import static com.packrboy.extras.Keys.Shipment.KEY_SHIPMENT_ARRAY;
import static com.packrboy.extras.urlEndPoints.KEY_ASSIGN_DELIVERY;
import static com.packrboy.extras.urlEndPoints.KEY_ASSIGN_PICKUP;
import static com.packrboy.extras.urlEndPoints.KEY_AVAILABLE;
import static com.packrboy.extras.urlEndPoints.KEY_SHIPMENT_URL;
import static com.packrboy.extras.urlEndPoints.KEY_UAT_BASE_URL_API;
import static com.packrboy.extras.Keys.Shipment.KEY_TYPE;
import static com.packrboy.extras.Keys.Shipment.KEY_SHIPMENT;
import static com.packrboy.extras.Keys.ServiceKeys.KEY_ERROR_CODE;

/**
 * Created by arindam.paaltao on 29-Jul-15.
 */
public class AvailableTaskFragment extends Fragment implements TaskAdapter.ClickListener{
    private RecyclerView mRecyclerView;
    private TaskAdapter mTaskAdapter;
    private JSONArray shipmentListArray;
    private TaskActivity activity;
    private SharedPreferenceClass preferenceClass;
    private ArrayList<Shipment> shipmentArrayList = new ArrayList<>();
    int shipmentId;
    String userId,transitStatus,requestType,streetNo,route,city,state,postalCode,imageURL,customerName,latitude,longitude,createdTime,updatedTime,itemQuantity;
    View layout;
    RelativeLayout progressWheel,noAvailableTasks;

    public AvailableTaskFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.available_task_fragment, container, false);

        preferenceClass = new SharedPreferenceClass(getActivity());
        userId = preferenceClass.getCustomerId();
        progressWheel = (RelativeLayout)layout.findViewById(R.id.progress_wheel);
        noAvailableTasks = (RelativeLayout)layout.findViewById(R.id.no_available_tasks);
        sendJsonRequest();
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.availableTaskRecyclerView);
        mTaskAdapter = new TaskAdapter(getActivity(), activity);
        mRecyclerView.setAdapter(mTaskAdapter);
        mTaskAdapter.setClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return layout;
    }

    public static String getRequestUrl(){
        return KEY_UAT_BASE_URL_API + KEY_SHIPMENT_URL + KEY_AVAILABLE;
    }

    public void sendJsonRequest(){
        progressWheel.setVisibility(View.VISIBLE);
        final JSONObject testObject = new JSONObject();
        try {
            testObject.put("token", preferenceClass.getAccessToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(), testObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                progressWheel.setVisibility(View.GONE);
                Log.i("error", jsonObject.toString());
                Log.i("login", testObject.toString());
                shipmentArrayList = parseJsonResponse(jsonObject);
                mTaskAdapter.setShipmentArrayList(shipmentArrayList);

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

    public ArrayList<Shipment> parseJsonResponse(JSONObject response) {

        ArrayList<Shipment> shipmentArrayList = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                shipmentListArray  = response.getJSONArray(KEY_SHIPMENT_ARRAY);
                if (shipmentListArray.length() == 0){
                    if (noAvailableTasks.getVisibility() == View.GONE)
                        noAvailableTasks.setVisibility(View.VISIBLE);
                }else {
                    if (noAvailableTasks.getVisibility() == View.VISIBLE)
                        noAvailableTasks.setVisibility(View.GONE);

                    for (int i = 0; i < shipmentListArray.length(); i++) {
                        JSONObject shipmentObject = shipmentListArray.getJSONObject(i);
                        if (shipmentObject.has(KEY_TYPE)) {
                            requestType = shipmentObject.getString(KEY_TYPE);
                            transitStatus = shipmentObject.getString(KEY_IN_TRANSIT_STATUS);
                        }
                        JSONObject shipmentDetails = new JSONObject();
                        shipmentDetails = shipmentObject.getJSONObject(KEY_SHIPMENT);

                        streetNo = shipmentDetails.getString(KEY_PICKUP_STREET_NO);
                        route = shipmentDetails.getString(KEY_PICKUP_ROUTE);
                        city = shipmentDetails.getString(KEY_PICKUP_CITY);
                        state = shipmentDetails.getString(KEY_PICKUP_STATE);
                        postalCode = shipmentDetails.getString(KEY_PICKUP_POSTAL_CODE);
                        imageURL = shipmentDetails.getString(KEY_ITEM_IMAGE);
                        latitude = shipmentDetails.getString(KEY_PICKUP_LATITUDE);
                        longitude = shipmentDetails.getString(KEY_PICKUP_LONGITUDE);
                        createdTime = shipmentDetails.getString(KEY_CREATED_AT);
                        itemQuantity = shipmentDetails.getString(KEY_ITEM_QUANTITY);
                        shipmentId = Integer.parseInt(shipmentDetails.getString(KEY_ID));


                        Shipment current = new Shipment();
                        current.setImageURL(imageURL);
                        current.setCity(city);
                        current.setCreatedTime(createdTime);
                        current.setLatitude(Double.parseDouble(latitude));
                        current.setLongitude(Double.parseDouble(longitude));
                        current.setPostalCode(postalCode);
                        current.setState(state);
                        current.setStreetNo(streetNo);
                        current.setRoute(route);
                        current.setRequestType(requestType);
                        current.setItemQuantity(itemQuantity);
                        current.setItemId(shipmentId);
                        current.setTransitStatus(transitStatus);

                        shipmentArrayList.add(current);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return shipmentArrayList;
    }


    public static String getAcceptRequestRequestUrl(){
        return KEY_UAT_BASE_URL_API + KEY_SHIPMENT_URL + KEY_ASSIGN_PICKUP;
    }

    public void sendAcceptRequestJsonRequest(){
        final JSONObject shipmentObject = new JSONObject();
        final JSONObject pendingTaskObject = new JSONObject();
        try {
            shipmentObject.put("packrboy_id", userId);
            shipmentObject.put("shipment_id", shipmentId);
            pendingTaskObject.put("payload", shipmentObject);
            pendingTaskObject.put("token", preferenceClass.getAccessToken());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getAcceptRequestRequestUrl(), pendingTaskObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("error", jsonObject.toString());
                Log.i("login", pendingTaskObject.toString());
                if (jsonObject.has(KEY_ERROR_CODE)){
                    try {
                        String errorCode = jsonObject.getString(KEY_ERROR_CODE);
                        if (errorCode.contentEquals("200")){
                            SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager()).setTitle("Request Accepted").setMessage(R.string.pickup_request_accepted).show();
                        }
                        else {
                            SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager()).setTitle("Request cannot be accepted").setMessage(R.string.pickup_request_not_allowed).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

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


    public static String getAcceptDeliveryRequestRequestUrl(){
        return KEY_UAT_BASE_URL_API + KEY_SHIPMENT_URL + KEY_ASSIGN_DELIVERY;
    }

    public void sendAcceptDeliveryRequestJsonRequest(){
        final JSONObject shipmentObject = new JSONObject();
        final JSONObject pendingTaskObject = new JSONObject();
        try {
            shipmentObject.put("packrboy_id", userId);
            shipmentObject.put("shipment_id", shipmentId);
            pendingTaskObject.put("payload", shipmentObject);
            pendingTaskObject.put("token", preferenceClass.getAccessToken());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getAcceptDeliveryRequestRequestUrl(), pendingTaskObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("error", jsonObject.toString());
                Log.i("login", pendingTaskObject.toString());
                if (jsonObject.has(KEY_ERROR_CODE)){
                    try {
                        String errorCode = jsonObject.getString(KEY_ERROR_CODE);
                        if (errorCode.contentEquals("200")){
                            SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager()).setTitle("Request Accepted").setMessage("Delivery request has been accepted").show();
                        }
                        else {
                            SimpleDialogFragment.createBuilder(getActivity(), getFragmentManager()).setTitle("Request cannot be accepted").setMessage("The request cannot be accepted").show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

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














    @Override
    public void itemClicked(View view, int position) {
        shipmentId = shipmentArrayList.get(position).getItemId();
        if (shipmentArrayList.get(position).getRequestType().contentEquals("pickup")){
        sendAcceptRequestJsonRequest();}

        else{
            sendAcceptDeliveryRequestJsonRequest();
        }



    }
}