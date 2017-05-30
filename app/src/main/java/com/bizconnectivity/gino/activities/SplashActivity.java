package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bizconnectivity.gino.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class SplashActivity extends AppCompatActivity {

    boolean isActivityStarted = false;
    SharedPreferences sharedPreferences;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Layout Binding
        ButterKnife.bind(this);

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Initial Realm
        realm = Realm.getDefaultInstance();

        // Check User Signed IN
        isSignedIn();
    }

    private void isSignedIn() {

        if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            Intent intent = new Intent(this, MainActivity.class);
            isActivityStarted = true;
            startActivity(intent);
        }
    }

    @OnClick(R.id.button_signup)
    public void buttonSignUpOnClick(View view) {

        Intent intent = new Intent(this, SignUpActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @OnClick(R.id.button_login)
    public void buttonSignInOnClick(View view) {

        Intent intent = new Intent(this, SignInActivity.class);
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
        if (!realm.isClosed()) realm.close();
        if (isActivityStarted) finish();
    }
}
