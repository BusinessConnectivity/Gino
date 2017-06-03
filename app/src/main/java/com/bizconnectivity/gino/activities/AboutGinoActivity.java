package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bizconnectivity.gino.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutGinoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_gino);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.playStore_layout)
    public void playStoreOnClick(View view) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.facebook.katana&hl=en")));
    }

    @OnClick(R.id.facebook_layout)
    public void facebookOnClick(View view) {

        startActivity(facebookIntent());
    }

    @OnClick(R.id.instagram_layout)
    public void instagramOnClick(View view) {

        startActivity(instagramIntent());
    }

    @OnClick(R.id.twitter_layout)
    public void twitterOnClick(View view) {

        startActivity(twitterIntent());
    }

    private Intent facebookIntent() {

        Uri uri = Uri.parse("https://www.facebook.com/n/?bizconnectivity");

        try {
            this.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, uri);
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, uri);
        }
    }

    private Intent instagramIntent() {

        Uri uri = Uri.parse("https://instagram.com/_u/instagram");

        try {
            this.getPackageManager().getPackageInfo("com.instagram.android", 0);
            return new Intent(Intent.ACTION_VIEW, uri);
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, uri);
        }
    }

    private Intent twitterIntent() {

        Uri uri = Uri.parse("https://twitter://user?user_id=Twitter");

        try {
            this.getPackageManager().getPackageInfo("com.twitter.android", 0);
            return new Intent(Intent.ACTION_VIEW, uri);
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, uri);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
