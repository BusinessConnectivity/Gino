package com.bizconnectivity.gino.activities;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.fragments.HomeFragment;
import com.bizconnectivity.gino.fragments.ProfileFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Bottom Bar
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                switch (tabId) {

                    case R.id.tab_home:
                        switchFragment(new HomeFragment());
                        break;

                    case R.id.tab_search:
                        switchFragment(new SearchFragment());
                        break;

                    case R.id.tab_purchase:
                        switchFragment(new PurchaseFragment());
                        break;

                    case R.id.tab_profile:
                        switchFragment(new ProfileFragment());
                        break;
                }
            }
        });
    }

    private void switchFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
