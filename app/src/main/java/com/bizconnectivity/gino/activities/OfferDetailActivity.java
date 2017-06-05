package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.RetrieveDealByIdAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveMerchantByIdAsyncTask;
import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.models.Merchant;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_OFFER_TAB;
import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format3;

public class OfferDetailActivity extends AppCompatActivity implements RetrieveDealByIdAsyncTask.AsyncResponse,
        RetrieveMerchantByIdAsyncTask.AsyncResponse{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.button_purchase)
    Button mButtonPurchase;

    @BindView(R.id.image_deal)
    ImageView mImageViewDeal;

    @BindView(R.id.text_title)
    TextView mTextViewTitle;

    @BindView(R.id.text_usual_price)
    TextView mTextViewUsualPrice;

    @BindView(R.id.text_promo_price)
    TextView mTextViewPromoPrice;

    @BindView(R.id.text_merchant)
    TextView mTextViewMerchant;

    @BindView(R.id.text_location)
    TextView mTextViewLocation;

    @BindView(R.id.text_description)
    TextView mTextViewDescription;

    @BindView(R.id.text_redeem_start)
    TextView mTextViewRedeemStart;

    @BindView(R.id.text_redeem_end)
    TextView mTextViewRedeemEnd;

    @BindView(R.id.text_promotion_end)
    TextView mTextViewPromotionEnd;

    @BindView(R.id.layout_content)
    NestedScrollView mLayoutContent;

    @BindView(R.id.layout_bottom)
    CardView mLayoutBottom;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int dealID;
    private Deal deal = new Deal();
    private Merchant merchant;
    private SharedPreferences sharedPreferences;
    private Calendar now = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals);

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

        // Retrieve Deal ID
        dealID = getIntent().getIntExtra("POSITION", 0);

        //Retrieve data from WS
        fetchData();
    }

    //Retrieve data from WS
    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(this)) {
            new RetrieveDealByIdAsyncTask(this, dealID).execute();
        } else {
            mLayoutContent.setVisibility(View.GONE);
            mLayoutBottom.setVisibility(View.GONE);
            mTextViewMessage.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    // Retrieve Deal by Id Callback
    @Override
    public void retrieveDealById(Deal result) {

        if (result != null) {
            deal = result;
            new RetrieveMerchantByIdAsyncTask(this, result.getMerchantID()).execute();
        } else {
            updateUI();
        }
    }

    @Override
    public void retrieveMerchant(Merchant result) {

        if (result != null) merchant = result;

        updateUI();
    }

    // Update UI
    private void updateUI() {

        if (deal.getDealImageFile() != null) {
            byte[] bloc = Base64.decode(deal.getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            mImageViewDeal.setImageBitmap(bitmap);
        }

        if (deal.getDealName() != null && !deal.getDealName().isEmpty())
            mTextViewTitle.setText(deal.getDealName());

        if (deal.getDealDescription() != null && !deal.getDealDescription().isEmpty())
            mTextViewDescription.setText(deal.getDealDescription());

        if (deal.getDealLocation() != null && !deal.getDealLocation().isEmpty())
            mTextViewLocation.setText(deal.getDealLocation());

        if (deal.getDealUsualPrice() != null && !deal.getDealUsualPrice().isEmpty()) {
            mTextViewUsualPrice.setText(deal.getDealUsualPrice());
            mTextViewUsualPrice.setPaintFlags(mTextViewUsualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (deal.getDealPromoPrice() != null && !deal.getDealPromoPrice().isEmpty())
            mTextViewPromoPrice.setText(deal.getDealPromoPrice());

        if (merchant.getMerchantName() != null && !merchant.getMerchantName().isEmpty())
            mTextViewMerchant.setText(merchant.getMerchantName());

        try {

            mTextViewRedeemStart.setText(format3.format(format1.parse(deal.getDealRedeemStartDate())));
            mTextViewRedeemEnd.setText(format3.format(format1.parse(deal.getDealRedeemEndDate())));
            mTextViewPromotionEnd.setText(format3.format(format1.parse(deal.getDealPromoEndDate())));

            if (format3.parse(format3.format(format1.parse(deal.getDealPromoEndDate()))).before(format3.parse(format3.format(now.getTime())))) {
                mLayoutBottom.setVisibility(View.GONE);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, 0, 0, 0);
                mLayoutContent.setLayoutParams(layoutParams);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.button_purchase)
    public void purchaseOnClick(View view) {

        Intent intent = new Intent(this, OfferPurchaseActivity.class);
        intent.putExtra("DEAL_ID", deal.getDealID());
        startActivity(intent);
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
        editor.putBoolean(SHARED_PREF_OFFER_TAB, true).apply();
        finish();
    }
}
