package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bizconnectivity.gino.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    boolean isActivityStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, LoginActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (isActivityStarted) finish();
    }
}
