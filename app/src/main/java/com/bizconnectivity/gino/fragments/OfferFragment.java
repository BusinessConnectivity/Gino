package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.adapters.GridViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfferFragment extends Fragment {

    public static String[] gridViewStrings = {
            "Android",
            "Java",
            "GridView",
            "ListView",
            "Adapter",
            "Custom GridView",
            "Material",
            "XML",
            "Code",

    };
    public static int[] gridViewImages = {
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1,
            R.drawable.feed1
    };

    @BindView(R.id.gridView)
    GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        ButterKnife.bind(this, view);

        mGridView.setAdapter(new GridViewAdapter(getContext(), gridViewStrings, gridViewImages));

        return view;
    }
}
