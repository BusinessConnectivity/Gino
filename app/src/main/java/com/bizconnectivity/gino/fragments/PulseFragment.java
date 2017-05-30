package com.bizconnectivity.gino.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.PulseDetailActivity;
import com.bizconnectivity.gino.adapters.PulseRecyclerListAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveEventAsyncTask;
import com.bizconnectivity.gino.models.EventModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;

public class PulseFragment extends Fragment implements PulseRecyclerListAdapter.AdapterCallBack, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener , RetrieveEventAsyncTask.AsyncResponse{

    @BindView(R.id.pulses_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Realm realm;
    private RealmResults<EventModel> event;
    private String latitude = "";
    private String longitude = "";
    private String kilometer = "5";
    private boolean isVisibleToUser;

    public PulseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pulse, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        // Initial Realm
        realm = Realm.getDefaultInstance();
        event = realm.where(EventModel.class).findAll();

        // Google API Client
        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    // The next two lines tell the new client that “this” current class will handle connection stuff
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    //fourth line adds the LocationServices API endpoint from GooglePlayServices
                    .addApi(LocationServices.API)
                    .build();
        }

        // Swipe Refresh Listener
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

        // Floating Action Button for back to top
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerView.scrollToPosition(0);
            }
        });

        // Recycler View Scrolling Detect
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
                p.setMargins(0, 0, 25, 145);
                fab.requestLayout();

                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }

                // check scrolling reach top or bottom
                LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                if (pastVisibleItems == 0) {
                    fab.hide();
                } else if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    p.setMargins(0, 0, 25, 25);
                    fab.requestLayout();
                    fab.show();
                }
            }
        });

        if (isVisibleToUser) {
            // Retrieve data from WS
            mSwipeRefreshLayout.setRefreshing(true);
            fetchData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible) {

        super.setUserVisibleHint(visible);

        if (visible) {
            isVisibleToUser = true;
        }
    }

    private void fetchData() {

        if (isNetworkAvailable(getContext())) {

            if (!longitude.isEmpty() && !longitude.isEmpty()) {
                new RetrieveEventAsyncTask(this, latitude, longitude, kilometer).execute();
            } else {
                updateUI(event);
            }

        } else {
            updateUI(event);
            snackBar(getParentView(), ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pulse, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter:

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        if (realm != null) realm.close();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else {

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {

                latitude = String.valueOf(mLastLocation.getLatitude());
                longitude = String.valueOf(mLastLocation.getLongitude());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {

            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }

        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.d("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

            fetchData();
        }
    }

    // Retrieve Nearby Event Callback
    @Override
    public void retrieveNearbyEvent(final List<EventModel> eventModelList) {

        if (eventModelList != null && eventModelList.size() > 0) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.where(EventModel.class).findAll().deleteAllFromRealm();

                    for (EventModel eventModel : eventModelList) {
                        realm.copyToRealm(eventModel);
                    }
                }
            });

            updateUI(event);
        } else {
            updateUI(event);
        }
    }

    @Override
    public void adapterOnClick(int eventId) {

        Intent intent = new Intent(getContext(), PulseDetailActivity.class);
        intent.putExtra("POSITION",  eventId);
        startActivity(intent);
    }

    private void updateUI(RealmResults<EventModel> event) {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PulseRecyclerListAdapter pulseListAdapter = new PulseRecyclerListAdapter(getContext(), event, PulseFragment.this);
        mRecyclerView.setAdapter(pulseListAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Nullable
    public View getParentView() {
        return (CoordinatorLayout) getParentFragment().getView().findViewById(R.id.coordinator_layout);
    }
}
