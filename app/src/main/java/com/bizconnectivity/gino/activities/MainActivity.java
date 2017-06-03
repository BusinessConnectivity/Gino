package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.fragments.HomeFragment;
import com.bizconnectivity.gino.fragments.ProfileFragment;
import com.bizconnectivity.gino.fragments.PurchasedFragment;
import com.bizconnectivity.gino.fragments.SearchFragment;
import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    private SharedPreferences sharedPreferences;
    private int selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fabric.with(this, new Crashlytics());

        // Layout Binding
        ButterKnife.bind(this);

        // Bottom Navigation
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Default Fragment
        getSelectedFragment();

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Check User Signed IN
        isSignedIn();
    }

    private void getSelectedFragment() {

        selectedFragment = mBottomNavigationView.getSelectedItemId();
        switch (selectedFragment) {

            case R.id.navigation_home:
                switchFragment(new HomeFragment());
                break;

            case R.id.navigation_search:
                switchFragment(new SearchFragment());
                break;

            case R.id.navigation_purchase:
                switchFragment(new PurchasedFragment());
                break;

            case R.id.navigation_profile:
                switchFragment(new ProfileFragment());
                break;
        }
    }

    private void isSignedIn() {

        if (!sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        }
    }

    // Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    switchFragment(new HomeFragment());
                    return true;

                case R.id.navigation_search:
                    switchFragment(new SearchFragment());
                    return true;

                case R.id.navigation_purchase:
                    switchFragment(new PurchasedFragment());
                    return true;

                case R.id.navigation_profile:
                    switchFragment(new ProfileFragment());
                    return true;
            }
            return false;
        }
    };

    // Switch Fragment
    private void switchFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSelectedFragment();
    }
}
