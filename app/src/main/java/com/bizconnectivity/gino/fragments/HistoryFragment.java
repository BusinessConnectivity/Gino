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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.OfferHistoryActivity;
import com.bizconnectivity.gino.adapters.HistoryDealsAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveHistoryDealAsyncTask;
import com.bizconnectivity.gino.models.PurchasedDeal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.ERR_MSG_USER_SIGN_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_ID;

public class HistoryFragment extends Fragment implements HistoryDealsAdapter.AdapterCallBack, RetrieveHistoryDealAsyncTask.AsyncResponse {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.history_list)
    RecyclerView mRecyclerViewHistory;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    private SharedPreferences sharedPreferences;
    private HistoryDealsAdapter historyDealsAdapter;
    private List<PurchasedDeal> purchasedDealList = new ArrayList<>();

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

        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyDealsAdapter = new HistoryDealsAdapter(purchasedDealList, this);
        mRecyclerViewHistory.setAdapter(historyDealsAdapter);

        fetchData();
    }

    // Retrieve data from WS
    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(getContext())) {

            // Check User Sign In
            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                new RetrieveHistoryDealAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0)).execute();

            } else {

                mSwipeRefreshLayout.setRefreshing(false);
                snackBar(getParentView(), ERR_MSG_USER_SIGN_IN);
            }

        } else {

            mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerViewHistory.setVisibility(View.GONE);
            mTextViewMessage.setVisibility(View.VISIBLE);
        }
    }

    // Retrieve User Deal Callback
    @Override
    public void retrieveHistoryDeal(List<PurchasedDeal> result) {

        if (result != null && result.size() > 0) {
            purchasedDealList = result;
        } else {
            purchasedDealList = new ArrayList<>();
        }

        updateUI();
    }

    // Update UI
    private void updateUI() {

        historyDealsAdapter.swapData(purchasedDealList);

        if (purchasedDealList.isEmpty()) snackBar(getParentView(), "No Record");

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // User Deal OnClick Callback
    @Override
    public void adapterOnClick(int position) {

        if (isNetworkAvailable(getContext())) {

            Intent intent = new Intent(getContext(), OfferHistoryActivity.class);
            intent.putExtra("USER_DEAL_ID", purchasedDealList.get(position).getUserDealId());

            if (purchasedDealList.get(position).isRedeemed()) {
                intent.putExtra("DEAL_STATUS", "REDEEMED");
                intent.putExtra("DEAL_STATUS_DATE", purchasedDealList.get(position).getRedeemedDate());
            } else {
                intent.putExtra("DEAL_STATUS", "EXPIRED");
                intent.putExtra("DEAL_STATUS_DATE", purchasedDealList.get(position).getDealRedeemEndDate());
            }

            startActivity(intent);

        } else {
            snackBar(getParentView(), ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @Nullable
    public View getParentView() {
        return (CoordinatorLayout) getParentFragment().getView().findViewById(R.id.coordinator_layout);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }
}
