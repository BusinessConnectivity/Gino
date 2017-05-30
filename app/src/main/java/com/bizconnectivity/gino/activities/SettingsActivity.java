package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.bizconnectivity.gino.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.profile_layout)
    LinearLayout mLinearLayoutProfile;

    private SharedPreferences sharedPreferences;

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

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
    }

    @OnClick(R.id.profile_layout)
    public void profileOnClick(View view) {

        if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            mLinearLayoutProfile.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);

        } else {
            mLinearLayoutProfile.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.notification_layout)
    public void notificationOnClick(View view) {

        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.payment_layout)
    public void paymentOnClick(View view) {

        Intent intent = new Intent(this, PaymentMethodActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
