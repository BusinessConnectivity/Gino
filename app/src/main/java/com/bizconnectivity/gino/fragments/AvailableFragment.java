package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.OfferAvailableActivity;
import com.bizconnectivity.gino.adapters.AvailableDealsAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveAvailableDealAsyncTask;
import com.bizconnectivity.gino.models.PurchasedDeal;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_RECORD;
import static com.bizconnectivity.gino.Constant.ERR_MSG_USER_SIGN_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_ID;

public class AvailableFragment extends Fragment implements AvailableDealsAdapter.AdapterCallBack, RetrieveAvailableDealAsyncTask.AsyncResponse {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.available_list)
    RecyclerView mRecyclerViewAvailable;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    @BindView(R.id.text_loading)
    TextView mTextViewLoading;

    @BindView(R.id.layout_loading)
    LinearLayout mLayoutLoading;

    private SharedPreferences sharedPreferences;
    private AvailableDealsAdapter availableDealsAdapter;
    private List<PurchasedDeal> purchasedDealList = new ArrayList<>();
    private Circle mCircleDrawable;

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

        mRecyclerViewAvailable.setLayoutManager(new LinearLayoutManager(getActivity()));
        availableDealsAdapter = new AvailableDealsAdapter(purchasedDealList, this);
        mRecyclerViewAvailable.setAdapter(availableDealsAdapter);

        // Start Loading
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.parseColor("#00AA8D"));
        mTextViewLoading.setCompoundDrawables(null, null, mCircleDrawable, null);

        fetchData();
    }

    private void fetchData() {

        if (isNetworkAvailable(getContext())) {

            // Check User Sign In
            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                new RetrieveAvailableDealAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0)).execute();

            } else {

                mSwipeRefreshLayout.setRefreshing(false);
                snackBar(getParentView(), ERR_MSG_USER_SIGN_IN);
            }

        } else {

            mTextViewMessage.setText(ERR_MSG_NO_INTERNET_CONNECTION);
            mTextViewMessage.setVisibility(View.VISIBLE);
            mRecyclerViewAvailable.setVisibility(View.GONE);
            // Stop Loading
            mLayoutLoading.setVisibility(View.GONE);
            mCircleDrawable.stop();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    // Retrieve User Deal Callback
    @Override
    public void retrieveAvailableDeal(final List<PurchasedDeal> result) {

        if (result != null && result.size() > 0) {
            purchasedDealList = result;
        } else {
            purchasedDealList = new ArrayList<>();
        }

        updateUI();
    }

    // Update UI
    private void updateUI() {

        availableDealsAdapter.swapData(purchasedDealList);

        if (purchasedDealList.isEmpty()) {

            mTextViewMessage.setText(ERR_MSG_NO_RECORD);
            mTextViewMessage.setVisibility(View.VISIBLE);
            mRecyclerViewAvailable.setVisibility(View.GONE);
        }

        // Stop Loading
        mLayoutLoading.setVisibility(View.GONE);
        mCircleDrawable.stop();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // User Deal OnClick Callback
    @Override
    public void adapterOnClick(int position) {

        if (isNetworkAvailable(getContext())) {

            Intent intent = new Intent(getContext(), OfferAvailableActivity.class);
            intent.putExtra("USER_DEAL_ID", purchasedDealList.get(position).getUserDealId());
            startActivity(intent);

        } else {
            snackBar(getParentView(), ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @Nullable
    public View getParentView() {
        return  getParentFragment().getView().findViewById(R.id.coordinator_layout_purchased);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLayoutLoading.setVisibility(View.VISIBLE);
        mCircleDrawable.start();
        fetchData();
    }
}
