package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.bizconnectivity.gino.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DealsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.button_purchase)
    Button mButtonPurchase;

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
    }

    @OnClick(R.id.button_purchase)
    public void purchaseOnClick(View view) {

        Intent intent = new Intent(this, DealPurchaseActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
