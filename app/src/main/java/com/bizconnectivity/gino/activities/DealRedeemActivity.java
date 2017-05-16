package com.bizconnectivity.gino.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.DealList;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import ng.max.slideview.SlideView;

public class DealRedeemActivity extends AppCompatActivity {

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

    @BindView(R.id.text_price)
    TextView mTextViewPrice;

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

    private int dealID;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_redeem);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initial Realm
        realm = Realm.getDefaultInstance();

        // Retrieve Extra
        dealID = getIntent().getIntExtra("POSITION", 0);

        initialDealData();

        mSlideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {

                mLinearLayout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                mSlideView.setEnabled(false);
                mSlideView.setText("Redeemed");
                mSlideView.setTextColor(Color.parseColor("#000000"));
                mSlideView.setButtonBackgroundColor(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                mSlideView.setBackgroundColor(Color.parseColor("#E0E0E0"));

                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Redeem Successfully", Snackbar.LENGTH_LONG);
                snackbar.show();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        DealList dealList = realm.where(DealList.class).equalTo("dealID", dealID).findFirst();
                        dealList.setIsPurchased("No");
                        dealList.setIsExpired("Redeemed");
                        realm.copyToRealmOrUpdate(dealList);
                    }
                });
            }
        });
    }

    private void initialDealData() {

        DealList dealList = realm.where(DealList.class).equalTo("dealID", dealID).findFirst();

        Picasso.with(this).load(dealList.getDealImageURL()).into(mImageViewDeal);
        mTextViewTitle.setText(dealList.getDealTitle());
        mTextViewDescription.setText(dealList.getDealDescription());
        mTextViewLocation.setText(dealList.getDealLocation());
        mTextViewPrice.setText(dealList.getDealPrice());
        mTextViewMerchant.setText(dealList.getDealMerchant());
        mTextViewRedeemStart.setText(dealList.getDealRedeemStart());
        mTextViewRedeemEnd.setText(dealList.getDealRedeemEnd());
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (!realm.isClosed()) realm.close();
    }
}
