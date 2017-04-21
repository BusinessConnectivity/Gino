package com.bizconnectivity.gino.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.fragments.OfferFragment;
import com.bizconnectivity.gino.fragments.ProfileFragment;
import com.bizconnectivity.gino.fragments.PulseFragment;
import com.bizconnectivity.gino.fragments.PurchaseFragment;
import com.bizconnectivity.gino.fragments.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Constant.*;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    boolean isHomeTab = true;
    boolean isPurchaseTab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Tab Layout
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getText().toString()) {

                    case TAB_PULSE:
                        // Switch to Pulse Fragment
                        switchFragment(new PulseFragment());
                        isHomeTab = true;
                        break;

                    case TAB_OFFER:
                        // Switch to Offer Fragment
                        switchFragment(new OfferFragment());
                        isHomeTab = false;
                        break;

                    case TAB_PURCHASED:
                        // Switch to Purchase Fragment
                        switchFragment(new PurchaseFragment());
                        isPurchaseTab = true;
                        break;

                    case TAB_HISTORY:
                        // Switch to History Fragment
                        switchFragment(new PurchaseFragment());
                        isPurchaseTab = false;
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Bottom Navigation
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Default Navigation
        navigationHome();
    }

    // Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    navigationHome();
                    return true;

                case R.id.navigation_search:
                    navigationSearch();
                    return true;

                case R.id.navigation_purchase:
                    navigationPurchase();
                    return true;

                case R.id.navigation_profile:
                    navigationProfile();
                    return true;
            }
            return false;
        }

    };

    private void navigationHome() {

        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.getTabAt(0).setText(TAB_PULSE);
        mTabLayout.getTabAt(1).setText(TAB_OFFER);

        if (isHomeTab) {

            TabLayout.Tab tab = mTabLayout.getTabAt(0);
            tab.select();
            switchFragment(new PulseFragment());

        } else {

            TabLayout.Tab tab = mTabLayout.getTabAt(1);
            tab.select();
            switchFragment(new OfferFragment());
        }
    }

    private void navigationSearch() {

        mTabLayout.setVisibility(View.GONE);
        switchFragment(new SearchFragment());
    }

    private void navigationPurchase() {

        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.getTabAt(0).setText(TAB_PURCHASED);
        mTabLayout.getTabAt(1).setText(TAB_HISTORY);

        if (isPurchaseTab) {

            TabLayout.Tab tab = mTabLayout.getTabAt(0);
            tab.select();
            switchFragment(new PurchaseFragment());

        } else {

            TabLayout.Tab tab = mTabLayout.getTabAt(1);
            tab.select();
            switchFragment(new PurchaseFragment());
        }
    }

    private void navigationProfile() {

        mTabLayout.setVisibility(View.GONE);
        switchFragment(new ProfileFragment());
    }

    // Switch Fragment
    private void switchFragment(Fragment fragment) {

        Fade fade = new Fade();
        fade.setDuration(300);
        fragment.setEnterTransition(fade);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
