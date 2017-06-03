package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.RetrieveUserDealDetailAsyncTask;
import com.bizconnectivity.gino.models.UserDeal;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_HISTORY_TAB;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format3;

public class OfferHistoryActivity extends AppCompatActivity implements RetrieveUserDealDetailAsyncTask.AsyncResponse {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.image_deal)
    ImageView mImageViewDeal;

    @BindView(R.id.text_title)
    TextView mTextViewTitle;

    @BindView(R.id.text_status)
    TextView mTextViewStatus;

    @BindView(R.id.text_status_date)
    TextView mTextViewStatusDate;

    @BindView(R.id.text_status_date_title)
    TextView mTextViewStatusDateTitle;

    @BindView(R.id.text_total)
    TextView mTextViewTotal;

    @BindView(R.id.text_subtotal)
    TextView mTextViewSubtotal;

    @BindView(R.id.text_purchased)
    TextView mTextViewPurchased;

    @BindView(R.id.text_quantity)
    TextView mTextViewQuantity;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.layout_content)
    NestedScrollView mLayoutContent;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int userDealId;
    private String dealStatus;
    private String dealStatusDate;
    private UserDeal userDeal = new UserDeal();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_history);

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

        // Swipe Refresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchData();
            }
        });

        // Retrieve Extra
        userDealId = getIntent().getIntExtra("USER_DEAL_ID", 0);
        dealStatus = getIntent().getStringExtra("DEAL_STATUS");
        dealStatusDate = getIntent().getStringExtra("DEAL_STATUS_DATE");

        fetchData();
    }

    // Retrieve data from WS
    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(this)) {

            new RetrieveUserDealDetailAsyncTask(this, userDealId).execute();

        } else {

            mSwipeRefreshLayout.setRefreshing(false);
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    // Retrieve purchased history Callback
    @Override
    public void retrieveAvailableDealDetail(UserDeal result) {

        if (result != null) {
            userDeal = result;
        } else {
            snackBar(mCoordinatorLayout, "ERROR");
        }

        updateUI();
    }

    // Update UI
    private void updateUI() {

        if (userDeal.getImageFile() != null) {
            byte[] bloc = Base64.decode(userDeal.getImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            mImageViewDeal.setImageBitmap(bitmap);
        }

        if (!dealStatus.isEmpty()) {

            mTextViewStatus.setText(dealStatus);

            if (dealStatus.equals("REDEEMED")) {
                mTextViewStatusDateTitle.setText("Redeemed On:");
            } else {
                mTextViewStatusDateTitle.setText("Expired On:");
            }
        }

        try {
            mTextViewPurchased.setText(format3.format(format1.parse(userDeal.getCreatedDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!userDeal.getDealName().isEmpty())
            mTextViewTitle.setText(userDeal.getDealName());

        try {
            if (!dealStatusDate.isEmpty())
                mTextViewStatusDate.setText(format3.format(format1.parse(dealStatusDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (userDeal.getQuantity() != 0)
            mTextViewQuantity.setText(String.valueOf(userDeal.getQuantity()));

        if (!userDeal.getTotalPrice().isEmpty())
            mTextViewTotal.setText(userDeal.getTotalPrice());

        if (!userDeal.getSubtotalPrice().isEmpty())
            mTextViewSubtotal.setText(userDeal.getSubtotalPrice());

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.text_view_more)
    public void viewMoreOnClick(View view) {

        if (isNetworkAvailable(this)) {
            Intent intent = new Intent(this, OfferDetailActivity.class);
            intent.putExtra("POSITION", userDeal.getDealId());
            startActivity(intent);
        } else {
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_HISTORY_TAB, true).apply();
        finish();
    }
}
