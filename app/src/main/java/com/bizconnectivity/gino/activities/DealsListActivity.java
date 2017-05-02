package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.helpers.OnStartDragListener;
import com.bizconnectivity.gino.helpers.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DealsListActivity extends AppCompatActivity implements OnStartDragListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.deal_list)
    RecyclerView mRecyclerViewDeals;

    OfferRecyclerListAdapter mRecyclerListAdapter;
    private ItemTouchHelper mItemTouchHelper;

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

        // RecyclerView
        mRecyclerViewDeals.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerListAdapter = new OfferRecyclerListAdapter(this, this);
        mRecyclerViewDeals.setAdapter(mRecyclerListAdapter);

        // OnTouchHelper for Deals List RecycleView
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this, mRecyclerListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeals);
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
