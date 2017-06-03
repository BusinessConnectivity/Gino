package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.DismissedDealAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveDismissedDealAsyncTask;
import com.bizconnectivity.gino.helpers.ItemTouchHelperCallback2;
import com.bizconnectivity.gino.models.Deal;

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

public class DismissedActivity extends AppCompatActivity implements DismissedDealAdapter.AdapterCallBack, RetrieveDismissedDealAsyncTask.AsyncResponse {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.dismissed_list)
    RecyclerView mRecyclerViewDismissed;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private DismissedDealAdapter dismissedDealAdapter;
    private SharedPreferences sharedPreferences;
    private List<Deal> dealList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dismissed);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Swipe Refresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        // Deal List Recycler View
        mRecyclerViewDismissed.setLayoutManager(new LinearLayoutManager(this));
        dismissedDealAdapter = new DismissedDealAdapter(dealList, this);
        mRecyclerViewDismissed.setAdapter(dismissedDealAdapter);

        // ItemTouchHelper for Deals List RecyclerView
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback2(this,
                sharedPreferences.getInt(SHARED_PREF_USER_ID, 0), dismissedDealAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDismissed);

        fetchData();
    }

    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(this)) {

            // Check User Sign In
            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                new RetrieveDismissedDealAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0)).execute();

            } else {

                snackBar(mCoordinatorLayout, ERR_MSG_USER_SIGN_IN);
            }

        } else {

            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mTextViewMessage.setVisibility(View.VISIBLE);
        }
    }

    // Retrieve favourite deal Callback
    @Override
    public void retrieveDismissedDeal(List<Deal> result) {

        if (result != null && result.size() > 0) dealList = result;

        updateUI();
    }

    private void updateUI() {

        dismissedDealAdapter.swapData(dealList);

        if (dealList.isEmpty()) snackBar(mCoordinatorLayout, "No Record");

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal OnClick Callback
    @Override
    public void adapterOnClick(int dealId) {

        if (isNetworkAvailable(this)) {

            Intent intent = new Intent(this, OfferDetailActivity.class);
            intent.putExtra("POSITION", dealId);
            startActivity(intent);

        } else {

            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
