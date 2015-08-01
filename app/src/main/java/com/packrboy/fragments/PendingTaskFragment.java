package com.packrboy.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.packrboy.extras.Keys.ServiceKeys.KEY_ERROR_CODE;
import static com.packrboy.extras.Keys.Shipment.KEY_CREATED_AT;
import static com.packrboy.extras.Keys.Shipment.KEY_DELIVERY_TYPE_ID;
import static com.packrboy.extras.Keys.Shipment.KEY_ID;
import static com.packrboy.extras.Keys.Shipment.KEY_IN_TRANSIT_STATUS;
import static com.packrboy.extras.Keys.Shipment.KEY_ITEM_IMAGE;
import static com.packrboy.extras.Keys.Shipment.KEY_ITEM_QUANTITY;
import static com.packrboy.extras.Keys.Shipment.KEY_ITEM_TYPE_ID;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_CITY;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_LATITUDE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_LONGITUDE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_POSTAL_CODE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_ROUTE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_STATE;
import static com.packrboy.extras.Keys.Shipment.KEY_PICKUP_STREET_NO;
import static com.packrboy.extras.Keys.Shipment.KEY_SHIPMENT;
import static com.packrboy.extras.Keys.Shipment.KEY_SHIPMENT_ARRAY;
import static com.packrboy.extras.Keys.Shipment.KEY_TYPE;
import static com.packrboy.extras.urlEndPoints.KEY_ASSIGN_DELIVERY;
import static com.packrboy.extras.urlEndPoints.KEY_ASSIGN_PICKUP;
import static com.packrboy.extras.urlEndPoints.KEY_DELIVERED;
import static com.packrboy.extras.urlEndPoints.KEY_PENDING;
import static com.packrboy.extras.urlEndPoints.KEY_PICKED_UP;
import static com.packrboy.extras.urlEndPoints.KEY_SHIPMENT_URL;
import static com.packrboy.extras.urlEndPoints.KEY_UAT_BASE_URL_API;

/**
 * Created by arindam.paaltao on 27-Jul-15.
 */
public class PendingTaskFragment extends Fragment implements TaskAdapter.ClickListener {
    private RecyclerView mRecyclerView;
    private TaskAdapter mTaskAdapter;
    private JSONArray shipmentListArray;
    private TaskActivity activity;
    private SharedPreferenceClass preferenceClass;
    private ArrayList<Shipment> shipmentArrayList = new ArrayList<>();
    int shipmentId, deliveryTypeId, rowPosition,itemTypeId;
    String transitStatus,itemType, deliveryType, userId, requestType, streetNo, route, city, state, postalCode, imageURL, customerName, latitude, longitude, createdTime, updatedTime, itemQuantity;
    View layout;
    private ProgressWheel progressWheel;
    private TextView noAvailableTasks;
    private RelativeLayout pendingTaskLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public PendingTaskFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.pending_task_fragment, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.activity_main_swipe_refresh_layout);
        preferenceClass = new SharedPreferenceClass(getActivity());
        userId = preferenceClass.getCustomerId();
        progressWheel = (ProgressWheel) layout.findViewById(R.id.progress_wheel);
        noAvailableTasks = (TextView) layout.findViewById(R.id.no_available_tasks);
        sendJsonRequest();
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.pendingTaskRecyclerView);
        mTaskAdapter = new TaskAdapter(getActivity(), activity);
        mRecyclerView.setAdapter(mTaskAdapter);
        mTaskAdapter.setClickListener(this);
        pendingTaskLayout = (RelativeLayout)layout.findViewById(R.id.pendingTaskLayout);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        canScrollVerticallyAnyFurther(mRecyclerView, true);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendJsonRequest();
            }
        });


        return layout;
    }

    public static boolean canScrollVerticallyAnyFurther(View view, boolean downwardScroll) {
        return view.canScrollVertically(downwardScroll ? +1 : -1);
    }


    public static String getRequestUrl() {
        return KEY_UAT_BASE_URL_API + KEY_SHIPMENT_URL + KEY_PENDING;
    }

    public void sendJsonRequest() {
        progressWheel.setVisibility(View.VISIBLE);
        final JSONObject pendingTaskObject = new JSONObject();
        try {
            pendingTaskObject.put("token", preferenceClass.getAccessToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = VolleySingleton.getsInstance().getRequestQueue();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getRequestUrl(), pendingTaskObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                progressWheel.setVisibility(View.GONE);
                Log.i("error", jsonObject.toString());
                Log.i("login", pendingTaskObject.toString());
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
                shipmentListArray = response.getJSONArray(KEY_SHIPMENT_ARRAY);
                if (shipmentListArray.length() == 0) {
                    if (noAvailableTasks.getVisibility() == View.GONE)
                        noAvailableTasks.setVisibility(View.VISIBLE);
                } else {
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
                        deliveryTypeId = Integer.parseInt(shipmentDetails.getString(KEY_DELIVERY_TYPE_ID));
                        itemTypeId = Integer.parseInt(shipmentDetails.getString(KEY_ITEM_TYPE_ID));
                        if (deliveryTypeId == 1) {
                            deliveryType = "Local";
                        } else if (deliveryTypeId == 2) {
                            deliveryType = "National";
                        } else if (deliveryTypeId == 3) {
                            deliveryType = "International";
                        }

                        if (itemTypeId == 1){
                            itemType = "Document";
                        }else if (itemTypeId == 2){
                            itemType = "Parcel";
                        }else if (itemTypeId == 3){
                            itemType = "Goods";
                        }



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
                        current.setDeliveryType(deliveryType);
                        current.setItemType(itemType);


                        shipmentArrayList.add(current);

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return shipmentArrayList;
    }


    public static String getPickedUpRequestRequestUrl() {
        return KEY_UAT_BASE_URL_API + KEY_SHIPMENT_URL + KEY_PICKED_UP;
    }

    public void sendPickedUpRequestJsonRequest() {
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getPickedUpRequestRequestUrl(), pendingTaskObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("error", jsonObject.toString());
                Log.i("login", pendingTaskObject.toString());
                if (jsonObject.has(KEY_ERROR_CODE)) {
                    try {
                        String errorCode = jsonObject.getString(KEY_ERROR_CODE);
                        if (errorCode.contentEquals("200")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Succesfully Picked");
                            alert.setMessage(R.string.pickup_request_accepted);
                            alert.show();

                            mTaskAdapter.removeItem(rowPosition);
                            if (shipmentListArray.length() == 0) {
                                if (noAvailableTasks.getVisibility() == View.GONE)
                                    noAvailableTasks.setVisibility(View.VISIBLE);
                            }
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Request cannot be accepted");
                            alert.setMessage(R.string.pickup_request_not_allowed);
                            alert.show();
                            mTaskAdapter.removeItem(rowPosition);
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
                    Snackbar.make(pendingTaskLayout, "Something is wrong with your internet connection. Please check your settings", Snackbar.LENGTH_LONG).show();


                } else if (volleyError instanceof AuthFailureError) {

                    //TODO
                } else if (volleyError instanceof ServerError) {

                    //TODO
                } else if (volleyError instanceof NetworkError) {
                    Snackbar.make(pendingTaskLayout, "Something is wrong with your internet connection. Please check your settings", Snackbar.LENGTH_LONG).show();

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


    public static String getDeliveredRequestRequestUrl() {
        return KEY_UAT_BASE_URL_API + KEY_SHIPMENT_URL + KEY_DELIVERED;
    }

    public void sendDeliveredRequestJsonRequest() {
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, getDeliveredRequestRequestUrl(), pendingTaskObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i("error", jsonObject.toString());
                Log.i("login", pendingTaskObject.toString());
                if (jsonObject.has(KEY_ERROR_CODE)) {
                    try {
                        String errorCode = jsonObject.getString(KEY_ERROR_CODE);
                        if (errorCode.contentEquals("200")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Shipment Delivered");
                            alert.setMessage("Shipment has been delivered");
                            alert.show();
                            mTaskAdapter.removeItem(rowPosition);
                            mTaskAdapter.removeItem(rowPosition);
                            if (shipmentListArray.length() == 0) {
                                if (noAvailableTasks.getVisibility() == View.GONE)
                                    noAvailableTasks.setVisibility(View.VISIBLE);
                            }
                        } else {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Request cannot be delivered");
                            alert.setMessage("The requested item cannot be delivered");
                            alert.show();
                            mTaskAdapter.removeItem(rowPosition);
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
                    Snackbar.make(pendingTaskLayout, "Something is wrong with your internet connection. Please check your settings", Snackbar.LENGTH_LONG).show();


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
        rowPosition = position;
        if (shipmentArrayList.get(position).getRequestType().contentEquals("pickup")) {
            if (shipmentArrayList.get(position).getTransitStatus().contentEquals("pickup_done")){
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("ITEM NOT SUBMITTED");
                alert.setMessage("Please submit the item to your nearest packing station");
                alert.show();

            }else{

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("CONFIRM");
                alert.setMessage("Do you want to accept the request?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPickedUpRequestJsonRequest();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cancel
                    }
                });
                alert.show();
            }
        }

        else if (shipmentArrayList.get(position).getTransitStatus().contentEquals("shipped")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("CONFIRM");
            alert.setMessage("Do you want to accept the request?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendDeliveredRequestJsonRequest();
                }
            });
            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Cancel
                }
            });
            alert.show();


        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("ITEM NOT SHIPPED");
            alert.setMessage("Sorry this item has not been shipped yet");
            alert.show();
        }
    }
}