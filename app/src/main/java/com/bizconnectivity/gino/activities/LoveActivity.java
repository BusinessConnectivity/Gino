package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.OfferCategoryAdapter;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class LoveActivity extends AppCompatActivity implements OfferRecyclerListAdapter.AdapterCallBack{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.favorite_list)
    RecyclerView mRecyclerViewFavorite;

    Realm realm;
    OfferRecyclerListAdapter mRecyclerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initial Realm
        realm = Realm.getDefaultInstance();

        mSwipeRefreshLayout.setRefreshing(true);

        favoriteDealList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                favoriteDealList();
            }
        });
    }

    private void favoriteDealList() {

        // Retrieve favorite deal lists
        List<DealList> dealLists = new ArrayList<>();
        dealLists = realm.where(DealList.class).equalTo("isFavorite", "Yes").findAll();

        mRecyclerViewFavorite.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerListAdapter = new OfferRecyclerListAdapter(this, realm, dealLists, this);
        mRecyclerViewFavorite.setAdapter(mRecyclerListAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    @Override
    public void dealAdapterOnClick(int adapterPosition) {

    }
}
