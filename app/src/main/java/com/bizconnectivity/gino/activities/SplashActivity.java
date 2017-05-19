package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bizconnectivity.gino.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    boolean isActivityStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Layout Binding
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_signup)
    public void buttonSignUpOnClick(View view) {

        Intent intent = new Intent(this, SignUpActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @OnClick(R.id.button_login)
    public void buttonSignInOnClick(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @OnClick(R.id.button_skip)
    public void buttonSkipOnClick(View view){

        Intent intent = new Intent(this, MainActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (isActivityStarted) finish();
    }
}
