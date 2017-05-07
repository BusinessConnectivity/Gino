package com.bizconnectivity.gino.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.fragments.AvailableFragment;
import com.bizconnectivity.gino.fragments.HistoryFragment;
import com.bizconnectivity.gino.fragments.HomeFragment;
import com.bizconnectivity.gino.fragments.OfferFragment;
import com.bizconnectivity.gino.fragments.ProfileFragment;
import com.bizconnectivity.gino.fragments.PulseFragment;
import com.bizconnectivity.gino.fragments.PurchaseFragment;
import com.bizconnectivity.gino.fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Constant.*;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Layout Binding
        ButterKnife.bind(this);

        // Bottom Navigation
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Default Fragment
        switchFragment(new HomeFragment());
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
                    switchFragment(new PurchaseFragment());
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

//        Fade fade = new Fade();
//        fade.setDuration(300);
//        fragment.setEnterTransition(fade);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
