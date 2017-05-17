package com.bizconnectivity.gino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.gino.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Common.shortToast;
import static com.bizconnectivity.gino.Constant.*;

public class HomeFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private int[] tabIcons = {
            R.drawable.ic_event_white_24dp,
            R.drawable.ic_local_mall_white_24dp
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        // Action Bar
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        // View Pager
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabLayout();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabLayout() {

        if (mTabLayout != null) {
            if (mTabLayout.getTabAt(0) != null) {

                LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
                TextView tabOne = (TextView) tabLinearLayout.findViewById(R.id.custom_tab_title);
                tabOne.setText("  " + TAB_PULSE);
                tabOne.setCompoundDrawablesWithIntrinsicBounds(tabIcons[0], 0, 0, 0);
                mTabLayout.getTabAt(0).setCustomView(tabOne);
            }

            if (mTabLayout.getTabAt(1) != null) {

                LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
                TextView tabTwo = (TextView) tabLinearLayout.findViewById(R.id.custom_tab_title);
                tabTwo.setText("  " + TAB_OFFER);
                tabTwo.setCompoundDrawablesWithIntrinsicBounds(tabIcons[1], 0, 0, 0);
                mTabLayout.getTabAt(1).setCustomView(tabTwo);
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new PulseFragment());
        adapter.addFragment(new OfferFragment());

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }


    }
}
