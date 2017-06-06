package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.PulseDetailActivity;
import com.bizconnectivity.gino.adapters.FavouriteEventAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveFavouriteEventAsyncTask;
import com.bizconnectivity.gino.models.Event;
import com.github.ybq.android.spinkit.style.Circle;

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

public class FavouriteEventFragment extends Fragment implements FavouriteEventAdapter.AdapterCallBack,
        RetrieveFavouriteEventAsyncTask.AsyncResponse {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.favourite_event_list)
    RecyclerView mRecyclerViewEvent;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    @BindView(R.id.text_loading)
    TextView mTextViewLoading;

    @BindView(R.id.layout_loading)
    LinearLayout mLayoutLoading;

    private SharedPreferences sharedPreferences;
    private FavouriteEventAdapter favouriteEventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private Circle mCircleDrawable;

    public FavouriteEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_event, container, false);
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

        mRecyclerViewEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        favouriteEventAdapter = new FavouriteEventAdapter(getContext(), eventList, this);
        mRecyclerViewEvent.setAdapter(favouriteEventAdapter);

        // Start Loading
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.parseColor("#00AA8D"));
        mTextViewLoading.setCompoundDrawables(null, null, mCircleDrawable, null);

        fetchData();
    }

    // Retrieve data from WS
    private void fetchData() {

        if (isNetworkAvailable(getContext())) {

            new RetrieveFavouriteEventAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0)).execute();

        } else {

            mTextViewMessage.setText(ERR_MSG_NO_INTERNET_CONNECTION);
            mTextViewMessage.setVisibility(View.VISIBLE);
            mRecyclerViewEvent.setVisibility(View.GONE);
            // Stop Loading
            mLayoutLoading.setVisibility(View.GONE);
            mCircleDrawable.stop();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    // Retrieve favourite event Callback
    @Override
    public void retrieveFavouriteEvent(final List<Event> result) {

        if (result != null && result.size() > 0) {
            eventList = result;
        } else {
            eventList = new ArrayList<>();
        }

        updateUI();
    }

    // Update UI
    private void updateUI() {

        favouriteEventAdapter.swapData(eventList);

        if (eventList.isEmpty()) {

            mTextViewMessage.setText(ERR_MSG_NO_RECORD);
            mTextViewMessage.setVisibility(View.VISIBLE);
            mRecyclerViewEvent.setVisibility(View.GONE);
        }

        // Stop Loading
        mLayoutLoading.setVisibility(View.GONE);
        mCircleDrawable.stop();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Event OnClick Callback
    @Override
    public void adapterOnClick(int eventId) {

        if (isNetworkAvailable(getContext())) {

            Intent intent = new Intent(getContext(), PulseDetailActivity.class);
            intent.putExtra("POSITION", eventId);
            startActivity(intent);

        } else {
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mLayoutLoading.setVisibility(View.VISIBLE);
//        mCircleDrawable.start();
//        fetchData();
    }
}
