package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.OfferDetailActivity;
import com.bizconnectivity.gino.adapters.FavouriteDealAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveFavouriteDealAsyncTask;
import com.bizconnectivity.gino.helpers.ItemTouchHelperCallback2;
import com.bizconnectivity.gino.models.DealModel;
import com.bizconnectivity.gino.models.FavDealModel;
import com.bizconnectivity.gino.models.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.ERR_MSG_USER_SIGN_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class FavouriteDealFragment extends Fragment implements FavouriteDealAdapter.AdapterCallBack, RetrieveFavouriteDealAsyncTask.AsyncResponse {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.favourite_deal_list)
    RecyclerView mRecyclerViewDeal;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private Realm realm;
    private UserModel user;
    private RealmResults<FavDealModel> favDeal;
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

        // Initial Realm
        realm = Realm.getDefaultInstance();
        user = realm.where(UserModel.class).findFirst();
        favDeal = realm.where(FavDealModel.class).findAll();

        // Swipe Refresh Listener
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        fetchData();
    }

    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(getContext())) {

            // Check User Sign In
            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {
                new RetrieveFavouriteDealAsyncTask(this, user.getUserID()).execute();
            } else {
                updateUI();
                snackBar(mCoordinatorLayout, ERR_MSG_USER_SIGN_IN);
            }

        } else {
            updateUI();
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void retrieveFavouriteDeal(final List<FavDealModel> favDealList) {

        if (favDealList != null && favDealList.size() > 0) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.where(FavDealModel.class).findAll().deleteAllFromRealm();
                    realm.copyToRealmOrUpdate(favDealList);
                }
            });

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    for (int i=0; i<favDealList.size(); i++) {

                        DealModel deal = realm.where(DealModel.class).equalTo("dealID", favDealList.get(i).getDealID()).findFirst();

                        FavDealModel favDeal = realm.where(FavDealModel.class).equalTo("userFavDealID",
                                favDealList.get(i).getUserFavDealID()).findFirst();

                        favDeal.getDeals().add(deal);
                    }
                }
            });
        }

        updateUI();
    }

    private void updateUI() {

        // Deal List Recycler View
        mRecyclerViewDeal.setLayoutManager(new LinearLayoutManager(getActivity()));
        FavouriteDealAdapter favouriteDealAdapter = new FavouriteDealAdapter(favDeal, realm, this);
        mRecyclerViewDeal.setAdapter(favouriteDealAdapter);

        // ItemTouchHelper for Deals List RecyclerView
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback2(getContext(), favouriteDealAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeal);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Deal OnClick Callback
    @Override
    public void adapterOnClick(int dealId) {

        Intent intent = new Intent(getContext(), OfferDetailActivity.class);
        intent.putExtra("POSITION", dealId);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (realm != null) realm.close();
    }
}
