package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.OfferDetailActivity;
import com.bizconnectivity.gino.adapters.OfferCategoryAdapter;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveDealAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveDealByCategoryAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveDealByDateAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveDealByViewAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveDealCategoryAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveUserDealAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveUserDealByCategoryAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveUserDealByDateAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveUserDealByViewAsyncTask;
import com.bizconnectivity.gino.asynctasks.UpdateDealNoOfViewAsyncTask;
import com.bizconnectivity.gino.helpers.ItemTouchHelperCallback;
import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.models.DealCategory;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_ID;

public class OfferFragment extends Fragment implements RetrieveUserDealAsyncTask.AsyncResponse, RetrieveDealAsyncTask.AsyncResponse,
        RetrieveDealCategoryAsyncTask.AsyncResponse, OfferRecyclerListAdapter.AdapterCallBack, OfferCategoryAdapter.AdapterCallBack,
        RetrieveUserDealByDateAsyncTask.AsyncResponse, RetrieveUserDealByViewAsyncTask.AsyncResponse, RetrieveDealByViewAsyncTask.AsyncResponse,
        RetrieveDealByDateAsyncTask.AsyncResponse, RetrieveUserDealByCategoryAsyncTask.AsyncResponse, RetrieveDealByCategoryAsyncTask.AsyncResponse {

    @BindView(R.id.categories_list)
    RecyclerView mRecyclerViewCategory;

    @BindView(R.id.deals_list)
    RecyclerView mRecyclerViewDeals;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.layout_deal_category)
    LinearLayout mLayoutDealCategory;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    @BindView(R.id.text_loading)
    TextView mTextViewLoading;

    @BindView(R.id.layout_loading)
    LinearLayout mLayoutLoading;

    private OfferCategoryAdapter offerCategoryAdapter;
    private OfferRecyclerListAdapter offerDealListAdapter;
    private SharedPreferences sharedPreferences;
    private List<Deal> dealList = new ArrayList<>();
    private List<DealCategory> dealCategoryList = new ArrayList<>();
    private Circle mCircleDrawable;

    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        // Shared Preferences
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Swipe Refresh OnRefresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        // Floating Action Button OnClick Listener
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNestedScrollView.scrollTo(0,0);
            }
        });

        // Nested Scroll View Scrolling Detect
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
                p.setMargins(0, 0, 25, 145);
                fab.requestLayout();

                if (scrollY == 0 || scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }

                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    p.setMargins(0, 0, 25, 25);
                    fab.requestLayout();
                    fab.show();
                }
            }
        });

        mLayoutDealCategory.setVisibility(View.GONE);

        // Category List Recycler View
        offerCategoryAdapter = new OfferCategoryAdapter(getContext(), dealCategoryList, this);
        mRecyclerViewCategory.setAdapter(offerCategoryAdapter);
        mRecyclerViewCategory.setNestedScrollingEnabled(false);

        // Deal List Recycler View
        offerDealListAdapter = new OfferRecyclerListAdapter(dealList, this);
        mRecyclerViewDeals.setAdapter(offerDealListAdapter);
        mRecyclerViewDeals.setNestedScrollingEnabled(false);

        int userId = sharedPreferences.getInt(SHARED_PREF_USER_ID, 0);

        // ItemTouchHelper for Deals List RecyclerView
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(getContext(), userId, offerDealListAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeals);

        // Start Loading
        mCircleDrawable = new Circle();
        mCircleDrawable.setBounds(0, 0, 100, 100);
        mCircleDrawable.setColor(Color.parseColor("#00AA8D"));
        mTextViewLoading.setCompoundDrawables(null, null, mCircleDrawable, null);

        fetchData();
    }

    // Retrieve Data From WS
    private void fetchData() {

        if (isNetworkAvailable(getContext())) {

            new RetrieveDealCategoryAsyncTask(this).execute();

        } else {

            mTextViewMessage.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            // Stop Loading
            mLayoutLoading.setVisibility(View.GONE);
            mCircleDrawable.stop();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    // Retrieve Deal Category Callback
    @Override
    public void retrieveDealCategory(List<DealCategory> result) {

        if (result != null && result.size() > 0) {

            dealCategoryList = result;
        }

        // Check User Sign In
        if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            new RetrieveUserDealAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0)).execute();

        } else {

            new RetrieveDealAsyncTask(this).execute();
        }
    }

    // Retrieve User Deal Callback
    @Override
    public void retrieveUserDeal(List<Deal> result) {

        if (result != null && result.size() > 0) {

            dealList = result;
        }

        updateUI();
    }

    // Retrieve Deal Callback
    @Override
    public void retrieveDeals(List<Deal> result) {

        if (result != null && result.size() > 0) {

            dealList = result;
        }

        updateUI();
    }

    // Update UI
    private void updateUI() {

        offerCategoryAdapter.swapData(dealCategoryList);
        offerDealListAdapter.swapData(dealList);

        mLayoutDealCategory.setVisibility(View.VISIBLE);

        // Stop Loading
        mLayoutLoading.setVisibility(View.GONE);
        mCircleDrawable.stop();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal Category OnClick
    @Override
    public void categoryAdapterOnClick(int position) {

        mSwipeRefreshLayout.setRefreshing(true);

        switch (dealCategoryList.get(position).getDealCategoryName()) {

            case "POPULAR":
                dealCategoryNew();
                break;

            case "NEW":
                dealCategoryPopular();
                break;

            default:
                dealCategorySelected(dealCategoryList.get(position).getDealCategoryID());
                break;
        }
    }

    // Deal Category order by created date
    private void dealCategoryNew() {

        mSwipeRefreshLayout.setRefreshing(true);

        // Check User Sign In
        if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            new RetrieveUserDealByDateAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0)).execute();

        } else {

            new RetrieveDealByDateAsyncTask(this).execute();
        }
    }

    // Retrieve Deal Category (New) Callback
    @Override
    public void retrieveUserDealByDate(List<Deal> result) {

        if (result != null && result.size() > 0) {
            offerDealListAdapter.swapData(result);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void retrieveDealByView(List<Deal> result) {

        if (result != null && result.size() > 0) {
            offerDealListAdapter.swapData(result);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal Category order by no of view
    private void dealCategoryPopular() {

        mSwipeRefreshLayout.setRefreshing(true);

        // Check User Sign In
        if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            new RetrieveUserDealByViewAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0)).execute();

        } else {

            new RetrieveDealByViewAsyncTask(this).execute();
        }
    }

    // Retrieve Deal Category (Popular) Callback
    @Override
    public void retrieveUserDealByView(List<Deal> result) {

        if (result != null && result.size() > 0) {
            offerDealListAdapter.swapData(result);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void retrieveDealByDate(List<Deal> result) {

        if (result != null && result.size() > 0) {
            offerDealListAdapter.swapData(result);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal Category Selected
    private void dealCategorySelected(int dealCategoryId) {

        // Check User Sign In
        if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            new RetrieveUserDealByCategoryAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0), dealCategoryId).execute();

        } else {

            new RetrieveDealByCategoryAsyncTask(this, dealCategoryId).execute();
        }
    }

    // Retrieve Deal Category (Selected) Callback
    @Override
    public void retrieveUserDealByCategory(List<Deal> result) {

        if (result != null && result.size() > 0) {
            offerDealListAdapter.swapData(result);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void retrieveDealByCategory(List<Deal> result) {

        if (result != null && result.size() > 0) {
            offerDealListAdapter.swapData(result);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal OnClick
    @Override
    public void dealAdapterOnClick(int dealId) {

        // Update Deal No of View
        if (isNetworkAvailable(getContext())) new UpdateDealNoOfViewAsyncTask(dealId).execute();

        Intent intent = new Intent(getContext(), OfferDetailActivity.class);
        intent.putExtra("POSITION", dealId);
        startActivity(intent);
    }

    @OnClick(R.id.button_category_left)
    public void buttonLeftOnClick(View view) {

        // Deal Category Swipe Left
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerViewCategory.getLayoutManager();
        mRecyclerViewCategory.getLayoutManager().scrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
    }

    @OnClick(R.id.button_category_right)
    public void buttonRightOnClick(View view) {

        // Deal Category Swipe Right
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerViewCategory.getLayoutManager();
        mRecyclerViewCategory.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
    }

    @Override
    public void onResume(){
        super.onResume();
        mLayoutLoading.setVisibility(View.VISIBLE);
        mCircleDrawable.start();
    }
}
