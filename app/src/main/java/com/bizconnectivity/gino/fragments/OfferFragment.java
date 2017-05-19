package com.bizconnectivity.gino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import com.bizconnectivity.gino.adapters.OfferCategoryAdapter;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.helpers.SimpleItemTouchHelperCallback;
import com.bizconnectivity.gino.models.DealCategoryList;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class OfferFragment extends Fragment implements OfferCategoryAdapter.AdapterCallBack, OfferRecyclerListAdapter.AdapterCallBack{

    @BindView(R.id.categories_list)
    RecyclerView mRecyclerViewCategory;

    @BindView(R.id.deals_list)
    RecyclerView mRecyclerViewDeals;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ItemTouchHelper mItemTouchHelper;
    OfferRecyclerListAdapter offerDealListAdapter;
    OfferCategoryAdapter offerCategoryAdapter;
    Realm realm;
    List<DealCategoryList> dealCategoryLists;
    List<DealList> dealLists;

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
        dealCategoryRecyclerView();

        // Deals List RecyclerView
        dealListRecyclerView(getDealList());

        // Swipe Refresh Layout
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                offerCategoryAdapter.swapData(getDealCategory());
                offerDealListAdapter.swapData(getDealList());

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        // Floating Action Button for back to top
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNestedScrollView.scrollTo(0,0);
            }
        });

        // Nested Scroll View Scrolling Detect
        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
                p.setMargins(0, 0, 25, 145);
                fab.requestLayout();

                if (scrollY == 0 || scrollY > oldScrollY) {
                    fab.hide();
                } else {
                    fab.show();
                }

                View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff == 0) {
                    p.setMargins(0, 0, 25, 25);
                    fab.requestLayout();
                    fab.show();
                }
            }
        });
    }

    @OnClick(R.id.button_category_left)
    public void buttonLeftOnClick(View view) {

        // Deal Category Swipe Left
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerViewCategory.getLayoutManager();
        mRecyclerViewCategory.getLayoutManager().scrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
    }

    @OnClick(R.id.button_category_right)
    public void buttonRightOnClick(View view) {

        // Deal Category Swipe Right
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerViewCategory.getLayoutManager();
        mRecyclerViewCategory.getLayoutManager().scrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
    }

    private void dealCategoryRecyclerView() {

        // Category List Recycler View
        offerCategoryAdapter = new OfferCategoryAdapter(getContext(), getDealCategory(), this);
        mRecyclerViewCategory.setAdapter(offerCategoryAdapter);
        mRecyclerViewCategory.setNestedScrollingEnabled(false);
    }

    private void dealListRecyclerView(List<DealList> dealLists) {

        // Deal List Recycler View
        offerDealListAdapter = new OfferRecyclerListAdapter(getContext(), realm, dealLists, this);
        mRecyclerViewDeals.setAdapter(offerDealListAdapter);
        mRecyclerViewDeals.setNestedScrollingEnabled(false);

        // ItemTouchHelper for Deals List RecyclerView
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(getContext(), offerDealListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerViewDeals);
    }

    private List<DealCategoryList> getDealCategory() {

        dealCategoryLists = new ArrayList<>();

        for (DealCategoryList result : realm.where(DealCategoryList.class).findAll()) {

            dealCategoryLists.add(result);
        }

        return dealCategoryLists;
    }

    private List<DealList> getDealList() {

        dealLists = new ArrayList<>();

        for (DealList result : realm.where(DealList.class).notEqualTo("isFavorite", "No").findAll()) {

            dealLists.add(result);
        }

        return dealLists;
    }

    private List<DealList> getDealListByCategory(int categoryID) {

        dealLists = new ArrayList<>();

        for (DealList result : realm.where(DealList.class).equalTo("dealCategoryID", categoryID)
                .notEqualTo("isFavorite", "No").findAll()) {

            dealLists.add(result);
        }

        return dealLists;
    }

    @Override
    public void categoryAdapterOnClick(int adapterPosition) {

        mSwipeRefreshLayout.setRefreshing(true);

        // Change Recycler View Data
        offerDealListAdapter.swapData(getDealListByCategory(dealCategoryLists.get(adapterPosition).getCategoryID()));

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void dealAdapterOnClick(int adapterPosition) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_offer, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter:

                break;

            default:
                break;
        }

        return true;
    }
}
