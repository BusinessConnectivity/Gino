package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.CreateUserDealAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveDealByIdAsyncTask;
import com.bizconnectivity.gino.models.Deal;

import java.math.BigDecimal;
import java.text.ParseException;

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

public class OfferPurchaseActivity extends AppCompatActivity implements CreateUserDealAsyncTask.AsyncResponse, RetrieveDealByIdAsyncTask.AsyncResponse {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.text_quantity)
    TextView mTextViewQuantity;

    @BindView(R.id.text_total_price)
    TextView mTextViewTotalPrice;

    @BindView(R.id.text_subtotal)
    TextView mTextViewSubTotal;

    @BindView(R.id.image_deal)
    ImageView mImageViewDeal;

    @BindView(R.id.text_title)
    TextView mTextViewTitle;

    @BindView(R.id.text_redeem_end)
    TextView mTextViewRedeemEnd;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private SharedPreferences sharedPreferences;
    private int dealId;
    private Deal deal = new Deal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_purchase);

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

        // Retrieve Deal
        dealId = getIntent().getIntExtra("DEAL_ID", 0);

        // Retrieve data from WS
        fetchData();
    }

    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(this)) {

            new RetrieveDealByIdAsyncTask(this, dealId).execute();

        } else {

            mSwipeRefreshLayout.setRefreshing(false);
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void retrieveDealById(Deal result) {

        if (result != null) {
            deal = result;
        }

        updateUI();
    }

    private void updateUI() {

        if (deal.getDealName() != null && !deal.getDealName().isEmpty())
            mTextViewTitle.setText(deal.getDealName());

        if (deal.getDealImageFile() != null && !deal.getDealImageFile().isEmpty()) {
            byte[] bloc = Base64.decode(deal.getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            mImageViewDeal.setImageBitmap(bitmap);
        }

        try {
            mTextViewRedeemEnd.setText(format2.format(format1.parse(deal.getDealRedeemEndDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (deal.getDealPromoPrice() != null && !deal.getDealPromoPrice().isEmpty()) {
            String price = "S$ " + deal.getDealPromoPrice();
            mTextViewTotalPrice.setText(price);
            mTextViewSubTotal.setText(price);
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.button_decrease)
    public void decreaseOnClick(View view) {

        int quantity = Integer.parseInt(mTextViewQuantity.getText().toString());
        BigDecimal totalPayable = new BigDecimal(String.valueOf(mTextViewSubTotal.getText().toString().substring(3)))
                .subtract(new BigDecimal(String.valueOf(mTextViewTotalPrice.getText().toString().substring(3))));

        if (quantity > 1) {

            mTextViewQuantity.setText(String.valueOf(quantity - 1));
            String price = "S$ " + totalPayable.toString();
            mTextViewSubTotal.setText(price);
        }
    }

    @OnClick(R.id.button_increase)
    public void increaseOnClick(View view) {

        int quantity = Integer.parseInt(mTextViewQuantity.getText().toString()) + 1;
        BigDecimal totalPrice = new BigDecimal(String.valueOf(quantity)).multiply(new BigDecimal(
                String.valueOf(mTextViewTotalPrice.getText().toString().substring(3))));

        mTextViewQuantity.setText(String.valueOf(quantity));
        String price = "S$ " + totalPrice.toString();
        mTextViewSubTotal.setText(price);
    }

    @OnClick(R.id.button_buy)
    public void buyOnClick(View view) {

        if (isNetworkAvailable(this)) {

            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                new MaterialDialog.Builder(this)
                        .title("Purchase Confirmation")
                        .content("Are you sure you want to purchase this offer?")
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                updateData();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();

            } else {

                snackBar(mCoordinatorLayout, "Please sign in to buy this deal.");
            }

        } else {
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    private void updateData() {

        mSwipeRefreshLayout.setRefreshing(true);

        int quantity = Integer.parseInt(mTextViewQuantity.getText().toString());
        String total = mTextViewTotalPrice.getText().toString().substring(3);
        String subtotal = mTextViewSubTotal.getText().toString().substring(3);

        new CreateUserDealAsyncTask(this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0), dealId,
                quantity, total, subtotal).execute();
    }

    @Override
    public void createUserDealResponse(boolean response) {

        if (response) {
            finish();
            snackBar(mCoordinatorLayout, "Purchase Successfully");
        } else {
            snackBar(mCoordinatorLayout, "Purchase Failed");
        }

        mSwipeRefreshLayout.setRefreshing(false);
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
