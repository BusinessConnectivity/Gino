package com.bizconnectivity.gino.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.DealList;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.gino.Common.shortToast;

public class DealPurchaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.text_quantity)
    TextView mTextViewQuantity;

    @BindView(R.id.text_total_price)
    TextView mTextViewTotalPrice;

    @BindView(R.id.text_total_payable)
    TextView mTextViewTotalPayable;

    @BindView(R.id.image_deal)
    ImageView mImageViewDeal;

    @BindView(R.id.text_redeem_end)
    TextView mTextViewRedeemEnd;

    private int dealID;
    Realm realm;

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

        // Initial Realm
        realm = Realm.getDefaultInstance();

        // Retrieve Extra
        dealID = getIntent().getIntExtra("POSITION", 0);

        initialDealData();
    }

    private void initialDealData() {

        DealList dealList = realm.where(DealList.class).equalTo("dealID", dealID).findFirst();

        Picasso.with(this).load(dealList.getDealImageURL()).into(mImageViewDeal);
        mTextViewRedeemEnd.setText(dealList.getDealRedeemEnd());
        mTextViewTotalPrice.setText("S$ " + dealList.getDealPrice());
        mTextViewTotalPayable.setText("S$ " + dealList.getDealPrice());
    }

    @OnClick(R.id.button_decrease)
    public void decreaseOnClick(View view) {

        int quantity = Integer.parseInt(mTextViewQuantity.getText().toString());
        BigDecimal totalPayable = new BigDecimal(String.valueOf(mTextViewTotalPayable.getText().toString().substring(3))).subtract(new BigDecimal(String.valueOf(mTextViewTotalPrice.getText().toString().substring(3))));

        if (quantity > 1) {

            mTextViewQuantity.setText(String.valueOf(quantity - 1));
            mTextViewTotalPayable.setText("S$ " + totalPayable.toString());
        }
    }

    @OnClick(R.id.button_increase)
    public void increaseOnClick(View view) {

        int quantity = Integer.parseInt(mTextViewQuantity.getText().toString()) + 1;
        BigDecimal totalPrice = new BigDecimal(String.valueOf(quantity)).multiply(new BigDecimal(String.valueOf(mTextViewTotalPrice.getText().toString().substring(3))));

        mTextViewQuantity.setText(String.valueOf(quantity));
        mTextViewTotalPayable.setText("S$ " + totalPrice.toString());
    }

    @OnClick(R.id.text_promo)
    public void addPromoOnClick(View view) {


    }

    @OnClick(R.id.button_buy)
    public void buyOnClick(View view) {

        updatePurchasedDealList();

        finish();

        shortToast(this, "Purchase Successfully!");
    }

    private void updatePurchasedDealList() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DealList dealList = realm.where(DealList.class).equalTo("dealID", dealID).findFirst();
                dealList.setIsPurchased("Yes");

                realm.copyToRealmOrUpdate(dealList);
            }
        });
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
