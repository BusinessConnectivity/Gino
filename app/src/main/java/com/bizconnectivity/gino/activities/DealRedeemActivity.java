package com.bizconnectivity.gino.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.bizconnectivity.gino.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.max.slideview.SlideView;

public class DealRedeemActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.slide_view)
    SlideView mSlideView;

    @BindView(R.id.linear_slide)
    LinearLayout mLinearLayout;

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

        mSlideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {

                mLinearLayout.setBackgroundColor(Color.parseColor("#E0E0E0"));
                mSlideView.setEnabled(false);
                mSlideView.setText("Redeemed");
                mSlideView.setTextColor(Color.parseColor("#000000"));
                mSlideView.setButtonBackgroundColor(ColorStateList.valueOf(Color.parseColor("#9E9E9E")));
                mSlideView.setBackgroundColor(Color.parseColor("#E0E0E0"));

                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Redeem Successfully", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
