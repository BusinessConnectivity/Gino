package com.bizconnectivity.gino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.MainActivity;
import com.bizconnectivity.gino.adapters.PulseRecyclerListAdapter;
import com.bizconnectivity.gino.models.PulseList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PulseFragment extends Fragment implements PulseRecyclerListAdapter.AdapterCallBack{

    @BindView(R.id.pulses_list)
    RecyclerView mRecyclerView;

    PulseRecyclerListAdapter pulseListAdapter;

    private static int[] pulseImage = {
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
    };

    private static String[] pulseTitle = {
            "Surviving the Hard Times - Career Excellence Series",
            "Surviving the Hard Times - Career Excellence Series",
            "Surviving the Hard Times - Career Excellence Series",
            "Surviving the Hard Times - Career Excellence Series",
            "Surviving the Hard Times - Career Excellence Series",
    };

    private static String[] pulseDatetime = {
            "MON, 17 APR 2017 1:00PM",
            "MON, 17 APR 2017 1:00PM",
            "MON, 17 APR 2017 1:00PM",
            "MON, 17 APR 2017 1:00PM",
            "MON, 17 APR 2017 1:00PM",
    };

    private static String[] pulseLocation = {
            "Lifelong Learning Institute",
            "Lifelong Learning Institute",
            "Lifelong Learning Institute",
            "Lifelong Learning Institute",
            "Lifelong Learning Institute",
    };

    List<PulseList> pulseLists = new ArrayList<>();

    public PulseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pulse, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        for (int i=0; i<pulseImage.length; i++) {

            PulseList pulseList = new PulseList();
            pulseList.setPulseImage(pulseImage[i]);
            pulseList.setPulseTitle(pulseTitle[i]);
            pulseList.setPulseDatetime(pulseDatetime[i]);
            pulseList.setPulseLocation(pulseLocation[i]);

            pulseLists.add(pulseList);
        }

        // Layout Binding
        ButterKnife.bind(this, view);

        // Pulse RecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pulseListAdapter = new PulseRecyclerListAdapter(getContext(), pulseLists, this);
        mRecyclerView.setAdapter(pulseListAdapter);
    }

    @Override
    public void adapterOnClick(int adapterPosition) {

    }
}
