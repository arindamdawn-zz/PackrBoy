package com.packrboy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.packrboy.R;

/**
 * Created by arindam.paaltao on 27-Jul-15.
 */
public class CompletedTaskFragment extends Fragment {
    int color;
    public CompletedTaskFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.completed_task_fragment, container, false);

        return view;
    }

}