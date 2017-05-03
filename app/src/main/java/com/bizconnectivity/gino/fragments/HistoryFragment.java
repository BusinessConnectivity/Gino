package com.bizconnectivity.gino.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.HistoryDealActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.deal1)
    public void deal1OnClick(View view) {

        Intent intent = new Intent(getContext(), HistoryDealActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.deal2)
    public void deal2OnClick(View view) {

        Intent intent = new Intent(getContext(), HistoryDealActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.deal3)
    public void deal3OnClick(View view) {

        Intent intent = new Intent(getContext(), HistoryDealActivity.class);
        startActivity(intent);
    }
}
