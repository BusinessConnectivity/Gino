package com.bizconnectivity.gino.activities;

import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.fragments.OfferFragment;
import com.bizconnectivity.gino.fragments.ProfileFragment;
import com.bizconnectivity.gino.fragments.PulseFragment;
import com.bizconnectivity.gino.fragments.PurchaseFragment;
import com.bizconnectivity.gino.fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.bottom_bar)
    BottomBar mBottomBar;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Tab Layout
        mTabLayout.addTab(mTabLayout.newTab().setText("PULSE"));
        mTabLayout.addTab(mTabLayout.newTab().setText("OFFER"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText() == "PULSE") {

                    switchFragment(new PulseFragment());

                } else {

                    switchFragment(new OfferFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Bottom Bar
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                switch (tabId) {

                    case R.id.tab_home:
                        mTabLayout.setVisibility(View.VISIBLE);
                        switchFragment(new PulseFragment());
                        break;

                    case R.id.tab_search:
                        mTabLayout.setVisibility(View.GONE);
                        switchFragment(new SearchFragment());
                        break;

                    case R.id.tab_purchase:
                        mTabLayout.setVisibility(View.GONE);
                        switchFragment(new PurchaseFragment());
                        break;

                    case R.id.tab_profile:
                        mTabLayout.setVisibility(View.GONE);
                        switchFragment(new ProfileFragment());
                        break;
                }
            }
        });
    }

    // Switch Fragment
    private void switchFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
