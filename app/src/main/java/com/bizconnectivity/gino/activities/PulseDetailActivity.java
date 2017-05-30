package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.CreateFavouriteEventAsyncTask;
import com.bizconnectivity.gino.asynctasks.DeleteFavouriteEventAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveEventByIdAsyncTask;
import com.bizconnectivity.gino.models.EventModel;
import com.bizconnectivity.gino.models.FavEventModel;
import com.bizconnectivity.gino.models.UserModel;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.squareup.picasso.Picasso;

import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class PulseDetailActivity extends AppCompatActivity implements RetrieveEventByIdAsyncTask.AsyncResponse{

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

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.button_save)
    ImageButton mImageButtonSave;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private SharedPreferences sharedPreferences;
    private Realm realm;
    private int eventID;
    private RealmResults<EventModel> event;

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

        // Initial Realm
        realm = Realm.getDefaultInstance();

        // Retrieve Event ID
        eventID = getIntent().getIntExtra("POSITION", 0);
        event = realm.where(EventModel.class).equalTo("eventID", eventID).findAll();

        // Retrieve data from WS
        fetchData();
    }

    private void fetchData() {

        mProgressBar.setVisibility(View.VISIBLE);
        new RetrieveEventByIdAsyncTask(this, eventID).execute();
    }

    // Retrieve Event Callback
    @Override
    public void retrieveEventById(final EventModel eventModel) {

        if (eventModel != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.copyToRealmOrUpdate(eventModel);
                }
            });
        }

        updateUI();
    }

    private void updateUI() {

        mTextViewTitle.setText(event.get(0).getEventName());
        mTextViewDescription.setText(event.get(0).getEventDescription());
        mTextViewDatetime.setText(event.get(0).getEventStartDateTime());
        mTextViewLocation.setText(event.get(0).getEventLocation());
        mTextViewOrganizer.setText(event.get(0).getEventOrganizer());

        if (event.get(0).getImageUrl() != null && !event.get(0).getImageUrl().isEmpty())
            Picasso.with(this).load(event.get(0).getImageUrl()).into(mImageViewPulse);

        if (realm.where(FavEventModel.class).equalTo("eventID", eventID).count() > 0) {
            mImageButtonSave.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            mImageButtonSave.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

        mProgressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.button_save)
    public void saveOnClick(View view) {

        if (isNetworkAvailable(this)) {

            // Check User Sign In
            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                if (realm.where(FavEventModel.class).equalTo("eventID", eventID).count() == 0) {

                    mImageButtonSave.setImageResource(R.drawable.ic_favorite_white_24dp);

                    UserModel user = realm.where(UserModel.class).findFirst();
                    new CreateFavouriteEventAsyncTask(user.getUserID(), eventID).execute();

                } else {

                    mImageButtonSave.setImageResource(R.drawable.ic_favorite_border_white_24dp);

                    FavEventModel favEvent= realm.where(FavEventModel.class).equalTo("eventID", eventID).findFirst();
                    new DeleteFavouriteEventAsyncTask(favEvent.getUserFavEventID()).execute();
                }
            }

        } else {
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @OnClick(R.id.button_share)
    public void shareOnClick(View view) {

        if (event.get(0).getImageUrl() != null && !event.get(0).getImageUrl().isEmpty()) {
            showSheetView(event.get(0).getImageUrl());
        }
    }

    @OnClick(R.id.button_website)
    public void websiteOnClick(View view) {

        if (event.get(0).getImageUrl() != null && !event.get(0).getImageUrl().isEmpty()) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(event.get(0).getImageUrl()));
            startActivity(intent);
        }
    }

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (realm != null) realm.close();
    }
}
