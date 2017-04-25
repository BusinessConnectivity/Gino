package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.DealsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseFragment extends Fragment {

    @BindView(R.id.deal1)
    RippleView deal1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        ButterKnife.bind(this, view);

        deal1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                Toast.makeText(getContext(), "deal 1", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getContext(), DealsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

//    @OnClick(R.id.deal1)
//    public void deal1Onclick (View view) {
//
//        Toast.makeText(getContext(), "deal 1", Toast.LENGTH_LONG).show();
//
//        Intent intent = new Intent(getContext(), DealsActivity.class);
//        startActivity(intent);
//    }
}
