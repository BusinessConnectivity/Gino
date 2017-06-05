package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.OfferDetailActivity;
import com.bizconnectivity.gino.adapters.FavouriteDealAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveFavouriteDealAsyncTask;
import com.bizconnectivity.gino.helpers.ItemTouchHelperCallback2;
import com.bizconnectivity.gino.models.Deal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_RECORD;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_ID;

public class FavouriteDealFragment extends Fragment implements FavouriteDealAdapter.AdapterCallBack, RetrieveFavouriteDealAsyncTask.AsyncResponse {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.favourite_deal_list)
    RecyclerView mRecyclerViewDeal;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    private List<Deal> dealList = new ArrayList<>();
    private FavouriteDealAdapter favouriteDealAdapter;
    private SharedPreferences sharedPreferences;

    public FavouriteDealFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_deal, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        // Shared Preferences
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Swipe Refresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        // Deal List Recycler View
        mRecyclerViewDeal.setLayoutManager(new LinearLayoutManager(getActivity()));
        favouriteDealAdapter = new FavouriteDealAdapter(dealList, this);
        mRecyclerViewDeal.setAdapter(favouriteDealAdapter);

        // ItemTouchHelper for Deals List RecyclerView
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback2(getContext(),
                sharedPreferences.getInt(SHARED_PREF_USER_ID, 0), favouriteDealAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeal);

        fetchData();
    }

    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(getContext())) {

            new RetrieveFavouriteDealAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0)).execute();

        } else {

            mTextViewMessage.setText(ERR_MSG_NO_INTERNET_CONNECTION);
            mTextViewMessage.setVisibility(View.VISIBLE);
            mRecyclerViewDeal.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    // Retrieve favourite deal Callback
    @Override
    public void retrieveFavouriteDeal(final List<Deal> result) {

        if (result != null && result.size() > 0) {
            dealList = result;
        } else {
            dealList = new ArrayList<>();
        }

        updateUI();
    }

    private void updateUI() {

        favouriteDealAdapter.swapData(dealList);

        if (dealList.isEmpty()) {

            mTextViewMessage.setText(ERR_MSG_NO_RECORD);
            mTextViewMessage.setVisibility(View.VISIBLE);
            mRecyclerViewDeal.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal OnClick Callback
    @Override
    public void adapterOnClick(int dealId) {

        if (isNetworkAvailable(getContext())) {

            Intent intent = new Intent(getContext(), OfferDetailActivity.class);
            intent.putExtra("POSITION", dealId);
            startActivity(intent);

        } else {

            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }
}
