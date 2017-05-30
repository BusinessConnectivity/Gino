package com.bizconnectivity.gino.activities;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.RetrieveMerchantByIdAsyncTask;
import com.bizconnectivity.gino.models.MerchantModel;
import com.bizconnectivity.gino.models.UserDealModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import ng.max.slideview.SlideView;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;

public class OfferRedeemActivity extends AppCompatActivity implements RetrieveMerchantByIdAsyncTask.AsyncResponse {

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

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private Realm realm;
    private UserDealModel userDeal;

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
        int userDealID = getIntent().getIntExtra("POSITION", 0);
        userDeal = realm.where(UserDealModel.class).equalTo("userDealID", userDealID).findFirst();

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

    private void fetchData() {

        if (isNetworkAvailable(this)) {
            new RetrieveMerchantByIdAsyncTask(this, userDeal.getDeals().get(0).getMerchantID()).execute();
        } else {
            updateUI();
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

            updateUI();
        }
    }

    private void updateUI() {

        MerchantModel merchant = realm.where(MerchantModel.class).equalTo("merchantID", userDeal.getDeals().get(0).getMerchantID()).findFirst();

        if (userDeal.getDeals().get(0).getDealImageFile() != null) {
            byte[] bloc = Base64.decode(userDeal.getDeals().get(0).getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            mImageViewDeal.setImageBitmap(bitmap);
        }

        mTextViewTitle.setText(userDeal.getDeals().get(0).getDealName());
        mTextViewDescription.setText(userDeal.getDeals().get(0).getDealDescription());
        mTextViewLocation.setText(userDeal.getDeals().get(0).getDealLocation());
        mTextViewPrice.setText(userDeal.getDeals().get(0).getDealPromoPrice());
        mTextViewMerchant.setText(merchant.getMerchantName());
        mTextViewRedeemStart.setText(userDeal.getDeals().get(0).getDealRedeemStartDate());
        mTextViewRedeemEnd.setText(userDeal.getDeals().get(0).getDealRedeemEndDate());
    }

    private void updateData() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                UserDealModel result = realm.where(UserDealModel.class).equalTo("userDealID", userDeal.getUserDealID()).findFirst();
                result.setRedeemed(true);
                realm.copyToRealmOrUpdate(result);
            }
        });

        mLinearLayout.setBackgroundColor(Color.parseColor("#E0E0E0"));
        mSlideView.setEnabled(false);
        mSlideView.setText("Redeemed");
        mSlideView.setTextColor(Color.parseColor("#000000"));
        mSlideView.setButtonBackgroundColor(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
        mSlideView.setBackgroundColor(Color.parseColor("#E0E0E0"));

        snackBar(mCoordinatorLayout, "Redeem Successfully");
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
