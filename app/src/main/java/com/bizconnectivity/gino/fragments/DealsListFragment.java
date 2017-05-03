package com.bizconnectivity.gino.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.OfferRecyclerListAdapter;
import com.bizconnectivity.gino.helpers.SimpleItemTouchHelperCallback;
import com.bizconnectivity.gino.models.DealList;

import java.util.ArrayList;
import java.util.List;

public class DealsListFragment extends Fragment {

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

    public DealsListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        for (int i=0; i<dealImage.length; i++) {

            DealList dealList = new DealList();
            dealList.setDealImage(dealImage[i]);
            dealList.setDealTitle(dealTitle[i]);
            dealList.setDealLocation(dealLocation[i]);
            dealList.setDealPrice(dealPrice[i]);

            dealarray.add(dealList);
        }

        OfferRecyclerListAdapter adapter = new OfferRecyclerListAdapter(getActivity(), dealarray);

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(getContext(), adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
