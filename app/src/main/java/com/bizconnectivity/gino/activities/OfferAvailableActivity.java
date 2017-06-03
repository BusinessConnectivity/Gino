package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.RetrieveUserDealDetailAsyncTask;
import com.bizconnectivity.gino.asynctasks.UpdateUserDealAsyncTask;
import com.bizconnectivity.gino.models.UserDeal;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.max.slideview.SlideView;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format3;

public class OfferAvailableActivity extends AppCompatActivity implements RetrieveUserDealDetailAsyncTask.AsyncResponse {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.slide_view)
    SlideView mSlideView;

    @BindView(R.id.linear_slide)
    LinearLayout mLinearLayout;

    @BindView(R.id.image_deal)
    ImageView mImageViewDeal;

    @BindView(R.id.text_title)
    TextView mTextViewTitle;

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

    @BindView(R.id.layout_bottom)
    CardView mLayoutBottom;

    @BindView(R.id.layout_content)
    NestedScrollView mLayoutContent;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int userDealId;
    private UserDeal userDeal = new UserDeal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_available);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Retrieve Extra
        userDealId = getIntent().getIntExtra("USER_DEAL_ID", 0);

        // Swipe Refresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        // Slide View
        mSlideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {

                if (isNetworkAvailable(getApplicationContext())) {
                    updateData();
                } else {
                    snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
                }
            }
        });

        fetchData();
    }

    // Retrieve data from WS
    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(this)) {

            new RetrieveUserDealDetailAsyncTask(this, userDealId).execute();

        } else {

            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    // Retrieve purchased detail Callback
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

        try {
            mTextViewPurchased.setText(format3.format(format1.parse(userDeal.getCreatedDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!userDeal.getDealName().isEmpty())
            mTextViewTitle.setText(userDeal.getDealName());

        if (userDeal.getQuantity() != 0)
            mTextViewQuantity.setText(String.valueOf(userDeal.getQuantity()));

        if (!userDeal.getTotalPrice().isEmpty())
            mTextViewTotal.setText(userDeal.getTotalPrice());

        if (!userDeal.getSubtotalPrice().isEmpty())
            mTextViewSubtotal.setText(userDeal.getSubtotalPrice());

        mSwipeRefreshLayout.setRefreshing(false);
    }

    // Update user deal to redeem
    private void updateData() {

        mLinearLayout.setBackgroundColor(Color.parseColor("#E0E0E0"));
        mSlideView.setEnabled(false);
        mSlideView.setText("Redeemed");
        mSlideView.setTextColor(Color.parseColor("#000000"));
        mSlideView.setButtonBackgroundColor(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
        mSlideView.setBackgroundColor(Color.parseColor("#E0E0E0"));

        new UpdateUserDealAsyncTask(userDealId).execute();
        snackBar(mCoordinatorLayout, "Redeem Successfully");
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
        finish();
    }
}
