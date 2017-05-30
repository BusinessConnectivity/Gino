package com.bizconnectivity.gino.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DismissedActivity extends AppCompatActivity implements OfferRecyclerListAdapter.AdapterCallBack{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.dismissed_list)
    RecyclerView mRecyclerViewDismissed;

    Realm realm;
    OfferRecyclerListAdapter mRecyclerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloved);

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

        dismissedDealList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dismissedDealList();
            }
        });
    }

    private void dismissedDealList() {

        // Retrieve favorite deal lists
//        List<DealList> dealLists = new ArrayList<>();
//        dealLists = realm.where(DealList.class).equalTo("isFavorite", "No").findAll();

        mRecyclerViewDismissed.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerListAdapter = new OfferRecyclerListAdapter(this,
//                realm.where(DealList.class).equalTo("isFavorite", "No").findAll(), true, realm, this);
        mRecyclerViewDismissed.setAdapter(mRecyclerListAdapter);

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