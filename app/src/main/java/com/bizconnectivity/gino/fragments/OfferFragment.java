package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.GridViewAdapter;
import com.bizconnectivity.gino.adapters.RecyclerListAdapter;
import com.bizconnectivity.gino.helpers.OnStartDragListener;
import com.bizconnectivity.gino.helpers.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfferFragment extends Fragment implements OnStartDragListener{

    private ItemTouchHelper mItemTouchHelper;

    public static String[] gridViewStrings = {
            "Android",
            "Java",
            "GridView",
            "ListView"

    };
    public static int[] gridViewImages = {
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
    };

    @BindView(R.id.gridView)
    GridView mGridView;

    @BindView(R.id.dealsList)
    RecyclerView mRecycleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        ButterKnife.bind(this, view);

        mGridView.setAdapter(new GridViewAdapter(getContext(), gridViewStrings, gridViewImages));

        RecyclerListAdapter adapter = new RecyclerListAdapter(getActivity(), this);
        mRecycleView.setAdapter(adapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecycleView);

        return view;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
