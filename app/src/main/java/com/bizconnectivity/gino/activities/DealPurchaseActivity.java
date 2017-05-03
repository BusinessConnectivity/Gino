package com.bizconnectivity.gino.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bizconnectivity.gino.R;

import org.w3c.dom.Text;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.shortToast;

public class DealPurchaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.text_quantity)
    TextView mTextViewQuantity;

    @BindView(R.id.text_total_price)
    TextView mTextViewTotalPrice;

    @BindView(R.id.text_total_payable)
    TextView mTextViewTotalPayable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_purchase);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @OnClick(R.id.button_decrease)
    public void decreaseOnClick(View view) {

        int quantity = Integer.parseInt(mTextViewQuantity.getText().toString());
        BigDecimal totalPrice = new BigDecimal(String.valueOf(mTextViewTotalPrice.getText().toString().substring(3))).subtract(new BigDecimal("49.90"));
        BigDecimal totalPayable = new BigDecimal(String.valueOf(mTextViewTotalPayable.getText().toString().substring(3))).subtract(new BigDecimal("49.90"));

        if (quantity > 1) {

            mTextViewQuantity.setText(String.valueOf(quantity - 1));
            mTextViewTotalPrice.setText("S$ " + totalPrice.toString());
            mTextViewTotalPayable.setText("S$ " + totalPayable.toString());
        }
    }

    @OnClick(R.id.button_increase)
    public void increaseOnClick(View view) {

        int quantity = Integer.parseInt(mTextViewQuantity.getText().toString()) + 1;
        BigDecimal totalPrice = new BigDecimal(String.valueOf(quantity)).multiply(new BigDecimal("49.90"));

        mTextViewQuantity.setText(String.valueOf(quantity));
        mTextViewTotalPrice.setText("S$ " + totalPrice.toString());
        mTextViewTotalPayable.setText("S$ " + totalPrice.toString());
    }

    @OnClick(R.id.text_promo)
    public void addPromoOnClick(View view) {


    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
