package com.bizconnectivity.gino.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.DealsListActivity;
import com.bizconnectivity.gino.adapters.OfferGridListAdapter;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.helpers.SimpleItemTouchHelperCallback;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfferFragment extends Fragment implements OfferGridListAdapter.ItemClickListener{

    public static String[] gridViewStrings = {
            "Food & Drinks",
            "Travel",
            "Beauty",
            "Nearby"

    };
    public static int[] gridViewImages = {
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
    };

    @BindView(R.id.categories_list)
    RecyclerView mRecyclerViewCategories;

    @BindView(R.id.deals_list)
    RecyclerView mRecyclerViewDeals;

    private ItemTouchHelper mItemTouchHelper;
    OfferGridListAdapter mGridListAdapter;
    OfferRecyclerListAdapter mRecyclerListAdapter;

    private static int[] dealImage = {
            R.drawable.deal1,
            R.drawable.deal1,
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
            "Ritz Apple Strudel",
            "Ritz Apple Strudel",
    };

    private static String[] dealLocation = {
            "Bugis Junction: B1-K12",
            "Bugis Junction: B1-K12",
            "Bugis Junction: B1-K12",
            "Bugis Junction: B1-K12",
            "Bugis Junction: B1-K12",
            "Bugis Junction: B1-K12",
            "Bugis Junction: B1-K12",
    };

    private static String[] dealPrice = {
            "S$49.90",
            "S$49.90",
            "S$49.90",
            "S$49.90",
            "S$49.90",
            "S$49.90",
            "S$49.90",
    };

    List<DealList> dealarray = new ArrayList<>();

    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        for (int i=0; i<dealImage.length; i++) {

            DealList dealList = new DealList();
            dealList.setDealImage(dealImage[i]);
            dealList.setDealTitle(dealTitle[i]);
            dealList.setDealLocation(dealLocation[i]);
            dealList.setDealPrice(dealPrice[i]);

            dealarray.add(dealList);
        }

        // Categories RecycleView
        mRecyclerViewCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mGridListAdapter = new OfferGridListAdapter(getContext(), gridViewStrings, gridViewImages);
        mRecyclerViewCategories.setAdapter(mGridListAdapter);
        mGridListAdapter.setClickListener(OfferFragment.this);


        // Deals List RecycleView
        mRecyclerViewDeals.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerListAdapter = new OfferRecyclerListAdapter(getContext(), dealarray);
        mRecyclerViewDeals.setAdapter(mRecyclerListAdapter);

        // ItemTouchHelper for Deals List RecycleView
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(getContext(), mRecyclerListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeals);
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent = new Intent(getContext(), DealsListActivity.class);
        startActivity(intent);
    }
}
