package com.bizconnectivity.gino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.AvailableDealsAdapter;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AvailableFragment extends Fragment implements AvailableDealsAdapter.AdapterCallBack{

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.available_list)
    RecyclerView mRecyclerViewAvailable;

    Realm realm;
    AvailableDealsAdapter mRecyclerListAdapter;

    public AvailableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        // Initial Realm
        realm = Realm.getDefaultInstance();

        mSwipeRefreshLayout.setRefreshing(true);

        availableDealList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                availableDealList();
            }
        });
    }

    private void availableDealList() {

        // Retrieve favorite deal lists
        List<DealList> dealLists = new ArrayList<>();
        dealLists = realm.where(DealList.class).equalTo("isPurchased", "Yes").findAll();

        mRecyclerViewAvailable.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerListAdapter = new AvailableDealsAdapter(getContext(), dealLists, this);
        mRecyclerViewAvailable.setAdapter(mRecyclerListAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume(){
        super.onResume();
        mSwipeRefreshLayout.setRefreshing(true);
        availableDealList();
    }

    @Override
    public void adapterOnClick(int adapterPosition) {

    }
}
