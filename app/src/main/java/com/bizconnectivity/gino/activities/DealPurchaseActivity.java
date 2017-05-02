package com.bizconnectivity.gino.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.bizconnectivity.gino.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DealPurchaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
