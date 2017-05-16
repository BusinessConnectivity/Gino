package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.DealList;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class DealsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.button_purchase)
    Button mButtonPurchase;

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

    @BindView(R.id.text_promotion_end)
    TextView mTextViewPromotionEnd;

    private int dealID;
    Realm realm;

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

        // Initial Realm
        realm = Realm.getDefaultInstance();

        // Retrieve Extra
        dealID = getIntent().getIntExtra("POSITION", 0);

        initialDealData();
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
        mTextViewPromotionEnd.setText(dealList.getDealPromotionEnd());
    }

    @OnClick(R.id.button_purchase)
    public void purchaseOnClick(View view) {

        Intent intent = new Intent(this, DealPurchaseActivity.class);
        intent.putExtra("POSITION", dealID);
        startActivity(intent);
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
