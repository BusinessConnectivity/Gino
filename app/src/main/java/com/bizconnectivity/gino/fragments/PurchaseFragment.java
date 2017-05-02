package com.bizconnectivity.gino.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.DealsActivity;
import com.bizconnectivity.gino.activities.PurchasedDealActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PurchaseFragment extends Fragment {

    @BindView(R.id.deal1)
    RippleView deal1;

    @BindView(R.id.deal2)
    RippleView deal2;

    @BindView(R.id.deal3)
    RippleView deal3;

    public PurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        deal1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                Intent intent = new Intent(getContext(), PurchasedDealActivity.class);
                startActivity(intent);
            }
        });

        deal2.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                Intent intent = new Intent(getContext(), PurchasedDealActivity.class);
                startActivity(intent);
            }
        });

        deal3.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                Intent intent = new Intent(getContext(), PurchasedDealActivity.class);
                startActivity(intent);
            }
        });
    }
}
