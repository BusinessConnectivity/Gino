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

public class HelpSupportActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpsupport);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.faq_layout)
    public void faqOnClick(View view) {

        Intent intent = new Intent(this, FAQActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.feedback_layout)
    public void feedbackOnClick(View view) {

        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.contact_layout)
    public void contactOnClick(View view) {

        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.privacy_layout)
    public void privacyOnClick(View view) {

        Intent intent = new Intent(this, PrivacyPolicyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.terms_layout)
    public void termsOnClick(View view) {

        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
