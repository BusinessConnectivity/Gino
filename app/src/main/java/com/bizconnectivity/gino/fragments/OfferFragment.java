package com.bizconnectivity.gino.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.DealsListActivity;
import com.bizconnectivity.gino.adapters.OfferCategoryAdapter;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.helpers.SimpleItemTouchHelperCallback;
import com.bizconnectivity.gino.models.DealCategoryList;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class OfferFragment extends Fragment implements OfferCategoryAdapter.AdapterCallBack, OfferRecyclerListAdapter.AdapterCallBack{

    @BindView(R.id.categories_list)
    RecyclerView mRecyclerViewCategory;

    @BindView(R.id.deals_list)
    RecyclerView mRecyclerViewDeals;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ItemTouchHelper mItemTouchHelper;
    OfferRecyclerListAdapter mRecyclerListAdapter;
    OfferCategoryAdapter offerCategoryAdapter;
    LinearLayoutManager linearLayoutManager;
    Realm realm;
    List<DealCategoryList> dealCategoryLists;

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

        // Initial Realm
        realm = Realm.getDefaultInstance();

        mSwipeRefreshLayout.setRefreshing(true);

        // Deal Category RecyclerView
        dealCategory();

        // Deals List RecyclerView
        dealList(getDealList());

        mSwipeRefreshLayout.setRefreshing(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dealCategory();
                dealList(getDealList());

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void dealCategory() {

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewCategory.setLayoutManager(linearLayoutManager);
        dealCategoryLists = new ArrayList<>();
        dealCategoryLists = getDealCategory();
        offerCategoryAdapter = new OfferCategoryAdapter(getContext(), dealCategoryLists, this);
        mRecyclerViewCategory.setAdapter(offerCategoryAdapter);
        mRecyclerViewCategory.setNestedScrollingEnabled(false);
    }

    private void dealList(List<DealList> dealLists) {

        mRecyclerViewDeals.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerListAdapter = new OfferRecyclerListAdapter(getContext(), realm, dealLists, this);
        mRecyclerViewDeals.setAdapter(mRecyclerListAdapter);
        mRecyclerViewDeals.setNestedScrollingEnabled(false);

        // ItemTouchHelper for Deals List RecyclerView
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(getContext(), mRecyclerListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeals);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_offer, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_search:
//
//                break;
//
//            case R.id.action_filter:
//
//                break;
//
//            default:
//                break;
//        }
//
//        return true;
//    }

    private List<DealCategoryList> getDealCategory() {

        List<DealCategoryList> dealCategoryLists = new ArrayList<>();

        for (DealCategoryList result : realm.where(DealCategoryList.class).findAll()) {

            dealCategoryLists.add(result);
        }

        return dealCategoryLists;
    }

    private List<DealList> getDealList() {

        List<DealList> dealLists = new ArrayList<>();

        for (DealList result : realm.where(DealList.class).notEqualTo("isFavorite", "No").findAll()) {

            dealLists.add(result);
        }

        return dealLists;
    }

    @Override
    public void categoryAdapterOnClick(int adapterPosition) {

        List<DealList> dealLists = new ArrayList<>();

        for (DealList result : realm.where(DealList.class).equalTo("dealCategoryID", dealCategoryLists.get(adapterPosition).getCategoryID()).findAll()) {

            dealLists.add(result);
        }

        dealList(dealLists);
    }

    @Override
    public void dealAdapterOnClick(int adapterPosition) {

    }
}
