package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.PulseList;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.IntentPickerSheetView;
import com.squareup.picasso.Picasso;

import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.shortToast;

public class PulseDetailActivity extends AppCompatActivity {

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

    private List<PulseList> pulseList;
    private int pulsePosition;

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

        // Retrieve Extra
        pulseList = (List<PulseList>) getIntent().getSerializableExtra("PULSE");
        pulsePosition = getIntent().getIntExtra("POSITION", 0);

        inititalPulseData();
    }

    private void inititalPulseData() {

        mTextViewTitle.setText(pulseList.get(pulsePosition).getPulseTitle());
        mTextViewDescription.setText(pulseList.get(pulsePosition).getPulseDescription());
        mTextViewDatetime.setText(pulseList.get(pulsePosition).getPulseDatetime());
        mTextViewLocation.setText(pulseList.get(pulsePosition).getPulseLocation());
        mTextViewOrganizer.setText(pulseList.get(pulsePosition).getPulseOrganizer());

        if (!pulseList.get(pulsePosition).getPulseImage().isEmpty())
            Picasso.with(this).load(pulseList.get(pulsePosition).getPulseImage()).into(mImageViewPulse);
    }

    @OnClick(R.id.button_save)
    public void saveOnClick(View view) {
    }

    @OnClick(R.id.button_share)
    public void shareOnClick(View view) {
        showSheetView();
    }

    @OnClick(R.id.button_website)
    public void websiteOnClick(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(pulseList.get(pulsePosition).getPulseURL()));
        startActivity(intent);
    }

    private void showSheetView() {

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, pulseList.get(pulsePosition).getPulseURL());
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
}
