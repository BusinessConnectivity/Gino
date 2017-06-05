package com.bizconnectivity.gino.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.PulseDetailActivity;
import com.bizconnectivity.gino.adapters.PulseRecyclerListAdapter;
import com.bizconnectivity.gino.asynctasks.RetrieveEventAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveEventFilterAsyncTask;
import com.bizconnectivity.gino.models.Event;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LOCATION_SERVICE;
import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.shortToast;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.format2;

public class PulseFragment extends Fragment implements PulseRecyclerListAdapter.AdapterCallBack, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, RetrieveEventAsyncTask.AsyncResponse, DatePickerDialog.OnDateSetListener, RetrieveEventFilterAsyncTask.AsyncResponse {

    @BindView(R.id.pulses_list)
    RecyclerView mRecyclerViewEvent;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    private PulseRecyclerListAdapter pulseListAdapter;
    private List<Event> eventList = new ArrayList<>();
    private LocationManager mLocationManager;
    private GoogleApiClient mGoogleApiClient;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String latitude = "";
    private String longitude = "";
    private String kilometer = "5";
    private Calendar now = Calendar.getInstance();
    private MaterialEditText mTextName;
    private MaterialEditText mTextDistance;
    private TextView mTextStartDate;
    private TextView mTextEndDate;
    private boolean isStartDateOnClick = false;
    private MaterialDialog dialog;

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

        // Google API Client Instance
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Location Manager Instance
        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        // Swipe Refresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isNetworkAvailable(getContext())) {

                    fetchData();

                } else {

                    mSwipeRefreshLayout.setRefreshing(false);
                    mRecyclerViewEvent.setVisibility(View.GONE);
                    mTextViewMessage.setVisibility(View.VISIBLE);
                }
            }
        });

        // Floating Action Button for back to top
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecyclerViewEvent.scrollToPosition(0);
            }
        });

        // Recycler View Scrolling Detect
        mRecyclerViewEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        mRecyclerViewEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        pulseListAdapter = new PulseRecyclerListAdapter(getContext(), eventList, this);
        mRecyclerViewEvent.setAdapter(pulseListAdapter);

        fetchData();
    }

    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(getContext())) {

            if (checkLocationPermission()) {

                if (!longitude.isEmpty() && !longitude.isEmpty()) {

                    new RetrieveEventAsyncTask(this, latitude, longitude, kilometer).execute();

                } else {

                    updateUI();
                }
            }

        } else {

            mSwipeRefreshLayout.setRefreshing(false);
            mRecyclerViewEvent.setVisibility(View.GONE);
            mTextViewMessage.setVisibility(View.VISIBLE);
        }
    }

    // Retrieve Nearby Event Callback
    @Override
    public void retrieveNearbyEvent(List<Event> result) {

        if (result != null && result.size() > 0) eventList = result;

        updateUI();
    }

    // Update UI
    private void updateUI() {

        pulseListAdapter.swapData(eventList);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Event OnClick
    @Override
    public void adapterOnClick(int eventId) {

        Intent intent = new Intent(getContext(), PulseDetailActivity.class);
        intent.putExtra("POSITION", eventId);
        startActivity(intent);
    }

    // Check and Request Location Permission
    private boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);


            return false;

        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        startLocationUpdates();
                    }
                }
            }
        }
    }

    // Get Last Known Location
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());

            fetchData();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {

            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }

        } else {

            Log.d("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        }
    }

    protected void startLocationUpdates() {

        // Create the location request
        long UPDATE_INTERVAL = 2 * 1000;
        long FASTEST_INTERVAL = 2000;

        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        // Request location updates
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    // Menu Settings
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
                filterPulse();
                break;

            default:
                break;
        }

        return true;
    }

    private void filterPulse() {

        dialog = new MaterialDialog.Builder(getContext())
                .title("Pulse Filter")
                .customView(R.layout.pulse_filter_view, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .build();

        mTextName = (MaterialEditText) dialog.getCustomView().findViewById(R.id.text_name);
        mTextDistance = (MaterialEditText) dialog.getCustomView().findViewById(R.id.text_distance);
        mTextStartDate = (TextView) dialog.getCustomView().findViewById(R.id.text_start_date);
        mTextEndDate = (TextView) dialog.getCustomView().findViewById(R.id.text_end_date);

        mTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStartDateOnClick = true;

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PulseFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        mTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isStartDateOnClick = false;

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PulseFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });


        View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        positiveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(getContext())) {

                    fetchFilterData();

                } else {

                    shortToast(getContext(), ERR_MSG_NO_INTERNET_CONNECTION);
                }
            }
        });

        mTextStartDate.setText(format2.format(now.getTime()));
        mTextEndDate.setText(format2.format(now.getTime()));

        dialog.show();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {

        String date = dayOfMonth + " / " + (++monthOfYear) + " / " + year;

        if (isStartDateOnClick) {
            try {
                mTextStartDate.setText(format2.format(format2.parse(date)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mTextEndDate.setText(format2.format(format2.parse(date)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchFilterData() {

        String name, distance, startDate, endDate;

        if (mTextName.getText().toString().isEmpty()) {
            name = "";
        } else {
            name = mTextName.getText().toString();
        }

        if (mTextDistance.getText().toString().isEmpty()) {
            distance = "5";
        } else {
            distance = mTextDistance.getText().toString();
        }

        startDate = mTextStartDate.getText().toString();
        endDate = mTextEndDate.getText().toString();

        new RetrieveEventFilterAsyncTask(this, latitude, longitude, distance, startDate, endDate, name).execute();
    }

    @Override
    public void retrieveNearbyEventFilter(List<Event> result) {

        if (result != null && result.size() > 0) {
            eventList = result;
            pulseListAdapter.swapData(eventList);
            dialog.dismiss();
        } else {
            eventList = new ArrayList<>();
            pulseListAdapter.swapData(eventList);
            dialog.dismiss();
            snackBar(getParentView(), "No Record");
        }
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
    }

    @Nullable
    public View getParentView() {
        return getParentFragment().getView().findViewById(R.id.coordinator_layout);
    }
}
