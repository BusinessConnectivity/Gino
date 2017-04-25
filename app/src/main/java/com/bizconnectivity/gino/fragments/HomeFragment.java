package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import com.bizconnectivity.gino.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Constant.TAB_OFFER;
import static com.bizconnectivity.gino.Constant.TAB_PULSE;

public class HomeFragment extends Fragment {

//    @BindView(R.id.tabLayout)
//    TabLayout mTabLayout;

//    @BindView(R.id.viewPager)
//    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        // Tab Layout
//        mTabLayout.setupWithViewPager(mViewPager);

        // View Pager
//        setupViewPager(mViewPager);

        return view;
    }


}
