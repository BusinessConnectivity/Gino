package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.RetrieveDealByIdAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveMerchantByIdAsyncTask;
import com.bizconnectivity.gino.models.DealModel;
import com.bizconnectivity.gino.models.MerchantModel;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format2;

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

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    Realm realm;
    private int dealID;
    private RealmResults<DealModel> deal;
    private RealmResults<MerchantModel> merchant;

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

        // Retrieve Deal ID
        dealID = getIntent().getIntExtra("POSITION", 0);
        deal = realm.where(DealModel.class).equalTo("dealID", dealID).findAll();
        merchant = realm.where(MerchantModel.class).equalTo("merchantID", deal.get(0).getMerchantID()).findAll();

        //Retrieve data from WS
        retrieveDataFromWS();
    }

    private void retrieveDataFromWS() {

        mProgressBar.setVisibility(View.VISIBLE);
        new RetrieveDealByIdAsyncTask(this, dealID).execute();
    }

    private void updateUI() throws ParseException {

        if (deal.get(0).getDealImageFile() != null) {
            byte[] bloc = Base64.decode(deal.get(0).getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            mImageViewDeal.setImageBitmap(bitmap);
        }

        mTextViewTitle.setText(deal.get(0).getDealName());
        mTextViewDescription.setText(deal.get(0).getDealDescription());
        mTextViewLocation.setText(deal.get(0).getDealLocation());
        mTextViewPrice.setText(deal.get(0).getDealPromoPrice());
        mTextViewMerchant.setText(merchant.get(0).getMerchantName());
        mTextViewRedeemStart.setText(format2.format(format1.parse(deal.get(0).getDealRedeemStartDate())));
        mTextViewRedeemEnd.setText(format2.format(format1.parse(deal.get(0).getDealRedeemEndDate())));
        mTextViewPromotionEnd.setText(format2.format(format1.parse(deal.get(0).getDealPromoEndDate())));

        mProgressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.button_purchase)
    public void purchaseOnClick(View view) {

        Intent intent = new Intent(this, OfferPurchaseActivity.class);
        intent.putExtra("POSITION", dealID);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void retrieveDealById(final DealModel dealModel) {

        if (dealModel != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.copyToRealmOrUpdate(dealModel);
                }
            });

            new RetrieveMerchantByIdAsyncTask(this, dealModel.getMerchantID()).execute();
        }
    }

    @Override
    public void retrieveMerchant(final MerchantModel merchantModel) {

        if (merchantModel != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.copyToRealmOrUpdate(merchantModel);
                }
            });

            try {
                updateUI();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
    }
}
