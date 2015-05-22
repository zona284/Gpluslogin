package com.example.ardhipc.gpluslogin.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ardhipc.gpluslogin.R;

/**
 * Created by Ardhipc on 5/23/2015.
 */
public class AllEventFragment extends Fragment {
    public AllEventFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_allevent, container, false);


        // Inflate the layout for this fragment
        return rootView;
    }
}
