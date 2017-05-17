package com.bizconnectivity.gino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.OfferCategoryAdapter;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.helpers.SimpleItemTouchHelperCallback;
import com.bizconnectivity.gino.models.DealCategoryList;
import com.bizconnectivity.gino.models.DealList;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

import static com.bizconnectivity.gino.Common.shortToast;

public class OfferFragment extends Fragment implements OfferCategoryAdapter.AdapterCallBack, OfferRecyclerListAdapter.AdapterCallBack{

    @BindView(R.id.categories_list)
    RecyclerView mRecyclerViewCategory;

    @BindView(R.id.deals_list)
    RecyclerView mRecyclerViewDeals;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.scroll_view)
    ObservableScrollView mScrollView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private ItemTouchHelper mItemTouchHelper;
    OfferRecyclerListAdapter mRecyclerListAdapter;
    OfferCategoryAdapter offerCategoryAdapter;
    Realm realm;
    List<DealCategoryList> dealCategoryLists;

    enum Type {
        SlideInLeft(new SlideInLeftAnimator()),
        SlideInRight(new SlideInRightAnimator());

        private BaseItemAnimator mAnimator;

        Type(BaseItemAnimator animator) {
            mAnimator = animator;
        }

        public BaseItemAnimator getAnimator() {
            return mAnimator;
        }
    }

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

        mSwipeRefreshLayout.setRefreshing(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dealCategoryRecyclerView();
                dealListRecyclerView(getDealList());

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        fab.attachToScrollView(mScrollView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mScrollView.scrollTo(0,0);
            }
        });
    }

    @OnClick(R.id.button_category_left)
    public void buttonLeftOnClick(View view) {

        mRecyclerViewCategory.setItemAnimator(Type.values()[0].getAnimator());
        mRecyclerViewCategory.getItemAnimator().setAddDuration(150);
        mRecyclerViewCategory.getItemAnimator().setRemoveDuration(0);

        offerCategoryAdapter.remove(3);
        offerCategoryAdapter.add(0, getDealCategory().get(0));
    }

    @OnClick(R.id.button_category_right)
    public void buttonRightOnClick(View view) {

        mRecyclerViewCategory.setItemAnimator(Type.values()[1].getAnimator());
        mRecyclerViewCategory.getItemAnimator().setAddDuration(150);
        mRecyclerViewCategory.getItemAnimator().setRemoveDuration(0);

        offerCategoryAdapter.remove(0);


        offerCategoryAdapter.add(3, getDealCategory().get(4));
    }

    private void dealCategoryRecyclerView() {

        dealCategoryLists = new ArrayList<>();

        if (getDealCategory().size() > 4) {

            for (int i=0; i<4; i++) {
                dealCategoryLists.add(getDealCategory().get(i));
            }
        }

        offerCategoryAdapter = new OfferCategoryAdapter(getContext(), dealCategoryLists, this);
        mRecyclerViewCategory.setAdapter(offerCategoryAdapter);
        mRecyclerViewCategory.setNestedScrollingEnabled(false);
    }

    private void dealListRecyclerView(List<DealList> dealLists) {

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

        mSwipeRefreshLayout.setRefreshing(true);

        List<DealList> dealLists = new ArrayList<>();

        for (DealList result : realm.where(DealList.class).equalTo("dealCategoryID", dealCategoryLists.get(adapterPosition).getCategoryID()).findAll()) {

            dealLists.add(result);
        }

        dealListRecyclerView(dealLists);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void dealAdapterOnClick(int adapterPosition) {

    }


}
