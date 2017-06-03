package com.bizconnectivity.gino.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.fragments.FavouriteDealFragment;
import com.bizconnectivity.gino.fragments.FavouriteEventFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Constant.TAB_OFFER;
import static com.bizconnectivity.gino.Constant.TAB_PULSE;

public class FavouriteActivity extends AppCompatActivity{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private int[] tabIcons = {
            R.drawable.ic_event_white_24dp,
            R.drawable.ic_local_offer_white_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // View Pager
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabLayout();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FavouriteEventFragment());
        adapter.addFragment(new FavouriteDealFragment());

        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout() {

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        LinearLayout tabLinearLayout2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

        if (mTabLayout != null) {
            if (mTabLayout.getTabAt(0) != null) {

                TextView tabOne = (TextView) tabLinearLayout.findViewById(R.id.custom_tab_title);
                tabOne.setText(TAB_PULSE);
                tabOne.setCompoundDrawablesWithIntrinsicBounds(tabIcons[0], 0, 0, 0);
                mTabLayout.getTabAt(0).setCustomView(tabOne);
            }

            if (mTabLayout.getTabAt(1) != null) {

                TextView tabTwo = (TextView) tabLinearLayout2.findViewById(R.id.custom_tab_title);
                tabTwo.setText(TAB_OFFER);
                tabTwo.setCompoundDrawablesWithIntrinsicBounds(tabIcons[1], 0, 0, 0);
                mTabLayout.getTabAt(1).setCustomView(tabTwo);
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
