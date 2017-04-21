package com.bizconnectivity.gino.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PulseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pulse, container, false);

        // Layout Binding
        ButterKnife.bind(this, view);

        return view;
    }
}
