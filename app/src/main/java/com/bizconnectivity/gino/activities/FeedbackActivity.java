package com.bizconnectivity.gino.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.bizconnectivity.gino.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.shortToast;

public class FeedbackActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.spinner)
    MaterialBetterSpinner mSpinner;

    @BindView(R.id.edit_message)
    MaterialEditText mEditTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String[] items = {"Give Feedback", "Report Issue"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items);
        mSpinner.setAdapter(adapter);
    }

    @OnClick(R.id.button_send)
    public void sendOnClick(View view) {

        mEditTextMessage.setText("");

        if (!mEditTextMessage.getText().toString().isEmpty()) {
            if (mSpinner.getText().toString().equals("Give Feedback")) {
                shortToast(this, "Your feedback was sent successfully. Thank you.");
            } else {
                shortToast(this, "The bug report was sent successfully. Thank you.");
            }
        } else {
            shortToast(this, "Please fill in the details.");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
