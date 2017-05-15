package com.bizconnectivity.gino.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bizconnectivity.gino.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.spinner_gender)
    MaterialBetterSpinner maSpinnerGender;

    @BindView(R.id.spinner_location)
    MaterialBetterSpinner maSpinnerLocation;

    @BindView(R.id.text_birthday)
    TextView mTextViewBirthday;

    Calendar now = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Layout Binding
        ButterKnife.bind(this);

        // Action Bar
        setSupportActionBar(mToolbar);

        // Back Button Navigation
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTextViewBirthday.setText("09 / 02 / 1990");

        String[] gender = {"Male", "Female"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, gender);
        maSpinnerGender.setAdapter(adapterGender);

        String[] location = {"Singapore", "Malaysia", "Indonesia"};
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, location);
        maSpinnerLocation.setAdapter(adapterLocation);
    }

    @OnClick(R.id.text_birthday)
    public void birthdayOnClick(View view) {

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ProfileActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = dayOfMonth + " / " + (++monthOfYear) + " / " + year;
        mTextViewBirthday.setText(date);
    }

    @OnClick(R.id.change_password)
    public void changePasswordOnClick(View view) {

        boolean wrapInScrollView = true;
        new MaterialDialog.Builder(this)
                .title("Change Password")
                .customView(R.layout.custom_view, wrapInScrollView)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
