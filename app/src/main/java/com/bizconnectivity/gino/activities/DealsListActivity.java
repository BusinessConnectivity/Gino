package com.bizconnectivity.gino.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.helpers.SimpleItemTouchHelperCallback;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DealsListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.deal_list)
    RecyclerView mRecyclerViewDeals;

    OfferRecyclerListAdapter mRecyclerListAdapter;
    private ItemTouchHelper mItemTouchHelper;

    private static int[] dealImage = {
            R.drawable.deal1,
            R.drawable.deal1,
            R.drawable.deal1,
            R.drawable.deal1,
            R.drawable.deal1,
    };

    private static String[] dealTitle = {
            "Ritz Apple Strudel",
            "Ritz Apple Strudel",
            "Ritz Apple Strudel",
            "Ritz Apple Strudel",
            "Ritz Apple Strudel",
    };

    private static String[] dealLocation = {
            "Bugis Junction: B1-K12.",
            "Bugis Junction: B1-K12.",
            "Bugis Junction: B1-K12.",
            "Bugis Junction: B1-K12.",
            "Bugis Junction: B1-K12.",
    };

    private static String[] dealPrice = {
            "S$49.90",
            "S$49.90",
            "S$49.90",
            "S$49.90",
            "S$49.90",
    };

    List<DealList> dealarray = new ArrayList<>();

    DealList dealList = new DealList();

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

        for (int i=0; i<dealImage.length; i++) {

            DealList dealList = new DealList();
            dealList.setDealImage(dealImage[i]);
            dealList.setDealTitle(dealTitle[i]);
            dealList.setDealLocation(dealLocation[i]);
            dealList.setDealPrice(dealPrice[i]);

            dealarray.add(dealList);
        }

        // RecyclerView
        mRecyclerViewDeals.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerListAdapter = new OfferRecyclerListAdapter(this, dealarray);
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
}
