package com.packrboy.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.packrboy.R;
import com.packrboy.activities.TaskActivity;
import com.packrboy.adapters.TaskAdapter;
import com.packrboy.classes.Shipment;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by arindam.paaltao on 29-Jul-15.
 */
public class AvailableTaskFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TaskAdapter mTaskAdapter;
    private JSONArray shipmentListArray;
    private TaskActivity activity;
    private ArrayList<Shipment> shipmentArrayList = new ArrayList<>();
    Long id;
    String requestType,streetNo,route,city,state,postalCode,imageURL,customerName,latitude,longitude,createdTime,uppdatedTime;
    View layout;

    public AvailableTaskFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.available_task_fragment, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.availableTaskRecyclerView);
        mTaskAdapter = new TaskAdapter(getActivity(), activity);
        mRecyclerView.setAdapter(mTaskAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

}