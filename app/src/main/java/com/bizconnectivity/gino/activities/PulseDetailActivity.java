package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.CheckFavouriteEventAsyncTask;
import com.bizconnectivity.gino.asynctasks.CreateFavouriteEventAsyncTask;
import com.bizconnectivity.gino.asynctasks.DeleteFavouriteEventAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveEventByIdAsyncTask;
import com.bizconnectivity.gino.models.Event;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_ID;
import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format2;

public class PulseDetailActivity extends AppCompatActivity implements RetrieveEventByIdAsyncTask.AsyncResponse,
        CheckFavouriteEventAsyncTask.AsyncResponse {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.image_pulse)
    ImageView mImageViewPulse;

    @BindView(R.id.text_title)
    TextView mTextViewTitle;

    @BindView(R.id.text_description)
    TextView mTextViewDescription;

    @BindView(R.id.text_datetime)
    TextView mTextViewDatetime;

    @BindView(R.id.text_location)
    TextView mTextViewLocation;

    @BindView(R.id.text_organizer)
    TextView mTextViewOrganizer;

    @BindView(R.id.bottom_sheet_layout)
    BottomSheetLayout mBottomSheetLayout;

    @BindView(R.id.button_save)
    ImageButton mImageButtonSave;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    @BindView(R.id.content_layout)
    LinearLayout mLinearLayoutContent;

    private SharedPreferences sharedPreferences;
    private int eventId;
    private int userFavEventId;
    private Event event;
    private boolean isFavouriteEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_detail);

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

        // Swipe Refresh OnRefresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchData();
            }
        });

        // Retrieve Event ID
        eventId = getIntent().getIntExtra("POSITION", 0);

        fetchData();
    }

    // Retrieve data from WS
    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(this)) {

            new RetrieveEventByIdAsyncTask(this, eventId).execute();

        } else {

            mLinearLayoutContent.setVisibility(View.GONE);
            mTextViewMessage.setVisibility(View.VISIBLE);
        }
    }

    // Retrieve Event Callback
    @Override
    public void retrieveEventById(Event eventModel) {

        if (eventModel != null) {

            event = eventModel;

            // Check User Sign In
            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                new CheckFavouriteEventAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0),
                        eventModel.getEventID()).execute();
            }
            else {
                updateUI(false);
            }
        }
    }

    // Check User Favourite Event Callback
    @Override
    public void checkFavouriteEventRespond(Event result) {

        if (result != null && result.getUserFavEventId() != 0) {

            isFavouriteEvent = true;
            userFavEventId = result.getUserFavEventId();
            updateUI(true);
        } else {

            isFavouriteEvent = false;
            updateUI(false);
        }
    }

    // Update UI
    private void updateUI(boolean response) {

        if (event.getEventName() != null && !event.getEventName().isEmpty())
            mTextViewTitle.setText(event.getEventName());

        if (event.getEventDescription() != null && !event.getEventDescription().isEmpty())
            mTextViewDescription.setText(event.getEventDescription());

        if (event.getEventStartDateTime() != null && !event.getEventStartDateTime().isEmpty()) {
            try {
                mTextViewDatetime.setText(format2.format(format1.parse(event.getEventStartDateTime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (event.getEventLocation() != null && !event.getEventLocation().isEmpty())
            mTextViewLocation.setText(event.getEventLocation());

        if (event.getEventOrganizer() != null && !event.getEventOrganizer().isEmpty())
            mTextViewOrganizer.setText(event.getEventOrganizer());

        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty())
            Picasso.with(this).load(event.getImageUrl()).into(mImageViewPulse);

        if (response) {
            mImageButtonSave.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            mImageButtonSave.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.button_save)
    public void saveOnClick(View view) {

        // Check User Sign In
        if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            if (isNetworkAvailable(this)) {

                if (!isFavouriteEvent) {

                    new CreateFavouriteEventAsyncTask(sharedPreferences.getInt(SHARED_PREF_USER_ID, 0), eventId).execute();

                    mImageButtonSave.setImageResource(R.drawable.ic_favorite_white_24dp);

                    isFavouriteEvent = true;

                    fetchData();

                } else {

                    new DeleteFavouriteEventAsyncTask(userFavEventId).execute();

                    mImageButtonSave.setImageResource(R.drawable.ic_favorite_border_white_24dp);

                    isFavouriteEvent = false;
                }


            } else {
                snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
            }

        } else {
            snackBar(mCoordinatorLayout, "Please sign in to save the event");
        }
    }

    @OnClick(R.id.button_share)
    public void shareOnClick(View view) {

        if (event.getEventURL() != null && !event.getEventURL().isEmpty()) {
            showSheetView(event.getEventURL());
        }
    }

    @OnClick(R.id.button_website)
    public void websiteOnClick(View view) {

        if (event.getEventURL() != null && !event.getEventURL().isEmpty()) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(event.getEventURL()));
            startActivity(intent);
        }
    }

    // Bottom Sheet Layout
    private void showSheetView(String url) {

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");

        IntentPickerSheetView intentPickerSheetView = new IntentPickerSheetView(this, shareIntent, "Share with...", new IntentPickerSheetView.OnIntentPickedListener() {
            @Override
            public void onIntentPicked(IntentPickerSheetView.ActivityInfo activityInfo) {

                mBottomSheetLayout.dismissSheet();
                startActivity(activityInfo.getConcreteIntent(shareIntent));
            }
        });

        // Sort activities in reverse order for no good reason
        intentPickerSheetView.setSortMethod(new Comparator<IntentPickerSheetView.ActivityInfo>() {
            @Override
            public int compare(IntentPickerSheetView.ActivityInfo lhs, IntentPickerSheetView.ActivityInfo rhs) {
                return rhs.label.compareTo(lhs.label);
            }
        });

        mBottomSheetLayout.showWithSheetView(intentPickerSheetView);
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
