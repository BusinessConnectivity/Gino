package com.bizconnectivity.gino.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.helpers.ItemTouchHelperCallback;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class DealsListActivity extends AppCompatActivity implements OfferRecyclerListAdapter.AdapterCallBack{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.deal_list)
    RecyclerView mRecyclerViewDeals;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    OfferRecyclerListAdapter mRecyclerListAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private int dealCategoryPosition;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_list);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Retrieve Extra
        dealCategoryPosition = getIntent().getIntExtra("POSITION", 0);

        // Initial Realm
        realm = Realm.getDefaultInstance();

        mSwipeRefreshLayout.setRefreshing(true);

        // RecyclerView
        mRecyclerViewDeals.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerListAdapter = new OfferRecyclerListAdapter(this,
                realm.where(DealList.class).equalTo("dealCategoryID", dealCategoryPosition).findAll(), true, realm, this);
        mRecyclerViewDeals.setAdapter(mRecyclerListAdapter);

        // OnTouchHelper for Deals List RecycleView
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(this, mRecyclerListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeals);

        mSwipeRefreshLayout.setRefreshing(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private List<DealList> getDealList() {

        List<DealList> dealLists = new ArrayList<>();

        for (DealList result : realm.where(DealList.class).equalTo("dealCategoryID", dealCategoryPosition).findAll()) {

            dealLists.add(result);
        }

        return dealLists;
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
