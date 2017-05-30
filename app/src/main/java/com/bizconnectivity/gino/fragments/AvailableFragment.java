package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bizconnectivity.gino.activities.OfferRedeemActivity;
import com.bizconnectivity.gino.adapters.AvailableDealsAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveUserDealAsyncTask;
import com.bizconnectivity.gino.models.DealModel;
import com.bizconnectivity.gino.models.UserDealModel;
import com.bizconnectivity.gino.models.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class AvailableFragment extends Fragment implements AvailableDealsAdapter.AdapterCallBack, RetrieveUserDealAsyncTask.AsyncResponse {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.available_list)
    RecyclerView mRecyclerViewAvailable;

    private SharedPreferences sharedPreferences;
    private Realm realm;
    private UserModel user;
    private RealmResults<UserDealModel> userDeal;

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

        // Initial Realm
        realm = Realm.getDefaultInstance();
        user = realm.where(UserModel.class).findFirst();
        userDeal = realm.where(UserDealModel.class).equalTo("isRedeemed", false).equalTo("isExpired", false).findAll();

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
                new RetrieveUserDealAsyncTask(this, user.getUserID()).execute();
            } else {
                updateUI();
            }

        } else {
            updateUI();
        }
    }

    @Override
    public void retrieveUserDeal(final List<UserDealModel> userDealModelList) {

        if (userDealModelList != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.where(UserDealModel.class).findAll().deleteAllFromRealm();

                    for (int i=0; i<userDealModelList.size(); i++) {

                        UserDealModel userDealModel = realm.createObject(UserDealModel.class, i+1);
                        userDealModel.setDealID(userDealModelList.get(i).getDealID());
                        userDealModel.setUserID(userDealModelList.get(i).getUserID());
                        userDealModel.setQuantity(userDealModelList.get(i).getQuantity());
                        userDealModel.setRedeemed(userDealModelList.get(i).isRedeemed());
                        userDealModel.setExpired(userDealModelList.get(i).isExpired());
                        userDealModel.setRedeemedDate(userDealModelList.get(i).getRedeemedDate());
                        userDealModel.getDeals().add(realm.where(DealModel.class)
                                .equalTo("dealID", userDealModelList.get(i).getDealID())
                                .findFirst());

                        realm.copyToRealmOrUpdate(userDealModel);
                    }
                }
            });

            updateUI();
        }
    }

    private void updateUI() {

        if (userDeal != null) {
            mRecyclerViewAvailable.setLayoutManager(new LinearLayoutManager(getActivity()));
            AvailableDealsAdapter mRecyclerListAdapter = new AvailableDealsAdapter(getContext(), userDeal, this);
            mRecyclerViewAvailable.setAdapter(mRecyclerListAdapter);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // User Deal OnClick Callback
    @Override
    public void adapterOnClick(int adapterPosition) {

        Intent intent = new Intent(getContext(), OfferRedeemActivity.class);
        intent.putExtra("POSITION", userDeal.get(adapterPosition).getUserDealID());
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
