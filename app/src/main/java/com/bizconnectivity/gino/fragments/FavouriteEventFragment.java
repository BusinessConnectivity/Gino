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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.PulseDetailActivity;
import com.bizconnectivity.gino.adapters.FavouriteEventAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveFavouriteEventAsyncTask;
import com.bizconnectivity.gino.models.EventModel;
import com.bizconnectivity.gino.models.FavEventModel;
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

public class FavouriteEventFragment extends Fragment implements FavouriteEventAdapter.AdapterCallBack,
        RetrieveFavouriteEventAsyncTask.AsyncResponse {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.favourite_event_list)
    RecyclerView mRecyclerViewEvent;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private Realm realm;
    private UserModel user;
    private RealmResults<FavEventModel> favEvent;
    private SharedPreferences sharedPreferences;


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

        // Initial Realm
        realm = Realm.getDefaultInstance();
        user = realm.where(UserModel.class).findFirst();
        favEvent = realm.where(FavEventModel.class).findAll();

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
                new RetrieveFavouriteEventAsyncTask(this, user.getUserID()).execute();
            } else {
                updateUI();
                snackBar(mCoordinatorLayout, ERR_MSG_USER_SIGN_IN);
            }

        } else {
            updateUI();
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    private void updateUI() {

        mRecyclerViewEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        FavouriteEventAdapter favouriteEventAdapter = new FavouriteEventAdapter(getContext(), favEvent, FavouriteEventFragment.this);
        mRecyclerViewEvent.setAdapter(favouriteEventAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void retrieveFavouriteEvent(final List<FavEventModel> favEventList) {

        if (favEventList != null && favEventList.size() > 0) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.where(FavEventModel.class).findAll().deleteAllFromRealm();
                    realm.copyToRealmOrUpdate(favEventList);
                }
            });

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    for (int i=0; i<favEventList.size(); i++) {

                        FavEventModel favEvent = realm.where(FavEventModel.class).equalTo("userFavEventID",
                                favEventList.get(i).getUserFavEventID()).findFirst();

                        EventModel event = realm.where(EventModel.class).equalTo("eventID", favEventList.get(i).getEventID()).findFirst();
                        favEvent.getEvents().add(event);
                    }
                }
            });
        }

        updateUI();
    }

    // Event OnClick Callback
    @Override
    public void adapterOnClick(int eventId) {

        Intent intent = new Intent(getContext(), PulseDetailActivity.class);
        intent.putExtra("POSITION",  eventId);
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
