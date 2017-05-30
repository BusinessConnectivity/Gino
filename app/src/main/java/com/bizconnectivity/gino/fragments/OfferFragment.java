package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.OfferDetailActivity;
import com.bizconnectivity.gino.adapters.OfferCategoryAdapter;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveDealAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveDealCategoryAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveDismissedDealAsyncTask;
import com.bizconnectivity.gino.asynctasks.UpdateDealNoOfViewAsyncTask;
import com.bizconnectivity.gino.helpers.ItemTouchHelperCallback;
import com.bizconnectivity.gino.models.DealCategoryModel;
import com.bizconnectivity.gino.models.DealModel;
import com.bizconnectivity.gino.models.DismissedDealModel;
import com.bizconnectivity.gino.models.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class OfferFragment extends Fragment implements OfferCategoryAdapter.AdapterCallBack, OfferRecyclerListAdapter.AdapterCallBack,
        RetrieveDealAsyncTask.AsyncResponse, RetrieveDealCategoryAsyncTask.AsyncResponse, UpdateDealNoOfViewAsyncTask.AsyncResponse,
        RetrieveDismissedDealAsyncTask.AsyncResponse {

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

    private OfferRecyclerListAdapter offerDealListAdapter;
    private SharedPreferences sharedPreferences;
    private Realm realm;
    private RealmResults<DealModel> deal;
    private RealmResults<DealCategoryModel> dealCategory;
    private int isFirstTime = 0;

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

        // Initial Realm
        realm = Realm.getDefaultInstance();
        deal = realm.where(DealModel.class).equalTo("isDismissed", false).findAllSorted("createdDate", Sort.DESCENDING);
        dealCategory = realm.where(DealCategoryModel.class).findAll();

        // Swipe Refresh OnRefresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isNetworkAvailable(getContext())) {
                    fetchData();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    snackBar(getParentView(), ERR_MSG_NO_INTERNET_CONNECTION);
                }
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

        if (deal != null && dealCategory != null) {
            updateUI(deal, dealCategory);
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible) {

        super.setUserVisibleHint(visible);

        if (visible) {
            fetchData();
        }
    }

    // Retrieve Data From WS
    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(getContext())) {
            new RetrieveDealCategoryAsyncTask(this).execute();
        } else {
            updateUI(deal, dealCategory);
            snackBar(getParentView(), ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    // Update UI
    private void updateUI(RealmResults<DealModel> deal, RealmResults<DealCategoryModel> dealCategory) {

        // Category List Recycler View
        OfferCategoryAdapter offerCategoryAdapter = new OfferCategoryAdapter(getContext(), dealCategory, true, this);
        mRecyclerViewCategory.setAdapter(offerCategoryAdapter);
        mRecyclerViewCategory.setNestedScrollingEnabled(false);

        // Deal List Recycler View
        offerDealListAdapter = new OfferRecyclerListAdapter(deal, realm, this);
        mRecyclerViewDeals.setAdapter(offerDealListAdapter);
        mRecyclerViewDeals.setNestedScrollingEnabled(false);

        // ItemTouchHelper for Deals List RecyclerView
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(getContext(), offerDealListAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeals);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal Category OnClick
    @Override
    public void categoryAdapterOnClick(int categoryID) {

        mSwipeRefreshLayout.setRefreshing(true);

        switch (dealCategory.get(categoryID).getDealCategoryName()) {

            case "POPULAR":

                RealmResults<DealModel> resultsPopular = realm.where(DealModel.class).findAllSorted("dealNoOfView", Sort.DESCENDING);
                offerDealListAdapter = new OfferRecyclerListAdapter(resultsPopular, realm, this);

                break;

            case "NEW":

                RealmResults<DealModel> resultsNew = realm.where(DealModel.class).findAllSorted("createdDate", Sort.DESCENDING);
                offerDealListAdapter = new OfferRecyclerListAdapter(resultsNew, realm, this);
                break;

            default:

                offerDealListAdapter = new OfferRecyclerListAdapter(
                        realm.where(DealModel.class).equalTo("dealCategoryID", dealCategory.get(categoryID).getDealCategoryID()).findAll(), realm, this);
                break;
        }

        // Reset RecyclerView
        mRecyclerViewDeals.setAdapter(offerDealListAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal OnClick
    @Override
    public void dealAdapterOnClick(int dealId) {

        if (isNetworkAvailable(getContext())) {
            new UpdateDealNoOfViewAsyncTask(this, dealId).execute();
        } else {
            Intent intent = new Intent(getContext(), OfferDetailActivity.class);
            intent.putExtra("POSITION", dealId);
            startActivity(intent);
        }
    }

    @Override
    public void updateDealNoOfViewResponse(boolean response, int dealId) {

        Intent intent = new Intent(getContext(), OfferDetailActivity.class);
        intent.putExtra("POSITION", dealId);
        startActivity(intent);
    }

    // Retrieve Deal Category Callback
    @Override
    public void retrieveDealCategory(final List<DealCategoryModel> dealCategoryModelList) {

        if (dealCategoryModelList != null && dealCategoryModelList.size() > 0) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    for (DealCategoryModel dealCategoryModel : dealCategoryModelList) {
                        realm.copyToRealmOrUpdate(dealCategoryModel);
                    }
                }
            });
        }

        new RetrieveDealAsyncTask(this).execute();
    }

    // Retrieve Deal Callback
    @Override
    public void retrieveDeal(final List<DealModel> dealModelList) {

        if (dealModelList != null && dealModelList.size() > 0) {

            // Check User Sign In
            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        for (DealModel dealModel : dealModelList) {
                            dealModel.setDismissed(false);
                            realm.copyToRealmOrUpdate(dealModel);
                        }
                    }
                });

                UserModel user = realm.where(UserModel.class).findFirst();

                new RetrieveDismissedDealAsyncTask(this, user.getUserID()).execute();

            } else {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        for (DealModel dealModel : dealModelList) {
                            dealModel.setDismissed(false);
                            realm.copyToRealmOrUpdate(dealModel);
                        }
                    }
                });

                updateUI(deal, dealCategory);
            }
        }
    }

    // Retrieve User Dismissed Deal Callback
    @Override
    public void retrieveDismissedDeal(final List<DismissedDealModel> dismissedDealList) {

        if (dismissedDealList != null && dismissedDealList.size() > 0) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    for (DismissedDealModel result : dismissedDealList) {

                        DealModel deal = realm.where(DealModel.class).equalTo("dealID", result.getDealID()).findFirst();
                        deal.setDismissed(true);

                        realm.copyToRealmOrUpdate(deal);
                    }

                    realm.copyToRealmOrUpdate(dismissedDealList);
                }
            });
        }

        updateUI(deal, dealCategory);
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
    public void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (realm != null) realm.close();
    }

    @Nullable
    public View getParentView() {
        return (CoordinatorLayout) getParentFragment().getView().findViewById(R.id.coordinator_layout);
    }
}
