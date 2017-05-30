package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.CreateUserDealAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveUserDealAsyncTask;
import com.bizconnectivity.gino.models.DealModel;
import com.bizconnectivity.gino.models.UserDealModel;
import com.bizconnectivity.gino.models.UserModel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format2;

public class OfferPurchaseActivity extends AppCompatActivity implements CreateUserDealAsyncTask.AsyncResponse,
        RetrieveUserDealAsyncTask.AsyncResponse{

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

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private SharedPreferences sharedPreferences;
    private Realm realm;
    private RealmResults<DealModel> deal;
    int dealID;
    int userID;

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

        // Initial Realm
        realm = Realm.getDefaultInstance();

        // Retrieve Deal
        dealID = getIntent().getIntExtra("POSITION", 0);
        deal = realm.where(DealModel.class).equalTo("dealID", dealID).findAll();
        UserModel user = realm.where(UserModel.class).findFirst();
        userID = user.getUserID();

        updateUI();
    }

    private void updateUI() {

        if (deal.get(0).getDealImageFile() != null) {
            byte[] bloc = Base64.decode(deal.get(0).getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            mImageViewDeal.setImageBitmap(bitmap);
        }
        try {
            mTextViewRedeemEnd.setText(format2.format(format1.parse(deal.get(0).getDealRedeemEndDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mTextViewTotalPrice.setText("S$ " + deal.get(0).getDealPromoPrice());
        mTextViewTotalPayable.setText("S$ " + deal.get(0).getDealPromoPrice());
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

        if (isNetworkAvailable(this)) {

            new MaterialDialog.Builder(this)
                    .title("Purchase Confirmation")
                    .content("Are you sure you want to purchase this offer?")
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                                updateData();

                            } else {
                                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                                startActivity(intent);
                            }
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .show();
        } else {
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    private void updateData() {

        int quantity = Integer.parseInt(mTextViewQuantity.getText().toString());

        new CreateUserDealAsyncTask(this, userID, dealID, quantity).execute();
    }

    @Override
    public void createUserDealResponse(boolean response) {

        if (response) {
            new RetrieveUserDealAsyncTask(this, userID).execute();
        } else {
            snackBar(mCoordinatorLayout, "Purchase Failed");
        }
    }

    @Override
    public void retrieveUserDeal(final List<UserDealModel> userDealModelList) {

        if (userDealModelList != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.where(UserDealModel.class).findAll().deleteAllFromRealm();

                    for (int i=0; i<userDealModelList.size(); i++) {

                        UserDealModel userDealModel = realm.createObject(UserDealModel.class, i+1);
                        userDealModel.setDealID(userDealModelList.get(i).getDealID());
                        userDealModel.setUserID(userDealModelList.get(i).getUserID());
                        userDealModel.setQuantity(userDealModelList.get(i).getQuantity());
                        userDealModel.setRedeemed(userDealModelList.get(i).isRedeemed());
                        userDealModel.setExpired(userDealModelList.get(i).isExpired());
                        userDealModel.setRedeemedDate(userDealModelList.get(i).getRedeemedDate());
                        userDealModel.getDeals().add(realm.where(DealModel.class)
                                .equalTo("dealID", userDealModelList.get(i).getDealID())
                                .findFirst());

                        realm.copyToRealmOrUpdate(userDealModel);
                    }
                }
            });

            snackBar(mCoordinatorLayout, "Purchase Successfully");

        } else {
            snackBar(mCoordinatorLayout, "Purchase Failed");
        }
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
