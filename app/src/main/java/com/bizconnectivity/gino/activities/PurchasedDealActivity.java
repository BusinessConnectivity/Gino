package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.bizconnectivity.gino.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchasedDealActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.button_redeem)
    Button mButtonRedeem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_deal);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.button_redeem)
    public void redeemOnClick(View view) {

        Intent intent = new Intent(this, DealRedeemActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
