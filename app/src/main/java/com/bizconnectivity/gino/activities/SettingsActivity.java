package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bizconnectivity.gino.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.profile_layout)
    public void profileOnClick(View view) {

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.notification_layout)
    public void notificationOnClick(View view) {

        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.payment_layout)
    public void paymentOnClick(View view) {

        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logout_layout)
    public void logoutOnClick(View view) {


    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
