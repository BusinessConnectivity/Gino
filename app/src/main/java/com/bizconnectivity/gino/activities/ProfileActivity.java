package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.ChangePasswordAsyncTask;
import com.bizconnectivity.gino.asynctasks.CheckUserLoginAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveUserAsyncTask;
import com.bizconnectivity.gino.asynctasks.UpdateUserAsyncTask;
import com.bizconnectivity.gino.models.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.shortToast;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.LOGIN_EMAIL;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_EMAIL;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_ID;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_SIGN_IN_TYPE;
import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format2;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, RetrieveUserAsyncTask.AsyncResponse,
        UpdateUserAsyncTask.AsyncResponse, ChangePasswordAsyncTask.AsyncResponse, CheckUserLoginAsyncTask.AsyncResponse {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.edit_name)
    MaterialEditText mEditTextName;

    @BindView(R.id.text_birthday)
    TextView mTextViewBirthday;

    @BindView(R.id.spinner_gender)
    MaterialBetterSpinner mSpinnerGender;

    @BindView(R.id.spinner_location)
    MaterialBetterSpinner maSpinnerLocation;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    @BindView(R.id.change_password)
    LinearLayout mLayoutChangePassword;

    private SharedPreferences sharedPreferences;
    private TextInputEditText mEditTextOldPassword;
    private TextInputEditText mEditTextNewPassword;
    private TextInputEditText mEditTextNewPasswordAgain;
    private MaterialDialog dialog;
    private Calendar now = Calendar.getInstance();
    private User user;
    private View viewFocus;

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

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        initialDefaultData();

        switch (sharedPreferences.getString(SHARED_PREF_USER_SIGN_IN_TYPE, "")) {

            case LOGIN_EMAIL:
                mLayoutChangePassword.setVisibility(View.VISIBLE);
                break;
            default:
                mLayoutChangePassword.setVisibility(View.GONE);
                break;
        }

        fetchData();
    }

    private void initialDefaultData() {

        mTextViewBirthday.setText("01 / 01 / 1990");

        String[] gender = {"MALE", "FEMALE"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, gender);
        mSpinnerGender.setAdapter(adapterGender);

        String[] location = {"Singapore", "Malaysia", "Indonesia"};
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, location);
        maSpinnerLocation.setAdapter(adapterLocation);
    }

    private void fetchData(){

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(this)) {

            new RetrieveUserAsyncTask(this, sharedPreferences.getString(SHARED_PREF_USER_EMAIL, "")).execute();

        } else {

            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mTextViewMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void retrieveUserDetail(final User result) {

        if (result != null) user = result;

        updateUI();
    }

    private void updateUI (){

        if (user.getUserName() != null)
            mEditTextName.setText(user.getUserName());

        if (user.getUserDOB() != null) {

            try {
                mTextViewBirthday.setText(format2.format(format1.parse(user.getUserDOB())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (user.getUserGender() != null)
            mSpinnerGender.setText(user.getUserGender().toUpperCase());

        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void updateData() {

        mSwipeRefreshLayout.setRefreshing(true);

        String email = user.getUserEmail();
        String name = mEditTextName.getText().toString();
        String gender = mSpinnerGender.getText().toString();
        String dob = mTextViewBirthday.getText().toString();

        new UpdateUserAsyncTask(this, email, name, gender, dob).execute();
    }

    @Override
    public void updateUserResponse(boolean response) {

        if (response) {
            snackBar(mCoordinatorLayout, "Update Successfully");
        } else {
            snackBar(mCoordinatorLayout, "Update Failed");
        }

        mSwipeRefreshLayout.setRefreshing(false);
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
        dpd.showYearPickerFirst(true);
        dpd.setMaxDate(now);
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = dayOfMonth + " / " + (++monthOfYear) + " / " + year;
        mTextViewBirthday.setText(date);
    }

    @OnClick(R.id.change_password)
    public void changePasswordOnClick(View view) {

        dialog = new MaterialDialog.Builder(this)
                .title("Change Password")
                .customView(R.layout.custom_view, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .build();

        mEditTextOldPassword = (TextInputEditText) dialog.getCustomView().findViewById(R.id.current_password);
        mEditTextNewPassword = (TextInputEditText) dialog.getCustomView().findViewById(R.id.new_password);
        mEditTextNewPasswordAgain = (TextInputEditText) dialog.getCustomView().findViewById(R.id.new_password_again);
        View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);

        positiveAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        dialog.show();
    }

    private void changePassword() {

        mEditTextOldPassword.setError(null);
        mEditTextNewPassword.setError(null);
        mEditTextNewPasswordAgain.setError(null);

        if (isNetworkAvailable(getApplicationContext())) {

            if (mEditTextOldPassword.getText().toString().isEmpty() || mEditTextNewPassword.getText().toString().isEmpty()
                    || mEditTextNewPasswordAgain.getText().toString().isEmpty()) {

                if (mEditTextOldPassword.getText().toString().isEmpty()) {
                    mEditTextOldPassword.setError("Cannot empty");
                    viewFocus = mEditTextOldPassword;
                    viewFocus.requestFocus();
                } else if (mEditTextNewPassword.getText().toString().isEmpty()) {
                    mEditTextNewPassword.setError("Cannot empty");
                    viewFocus = mEditTextNewPassword;
                    viewFocus.requestFocus();
                } else {
                    mEditTextNewPasswordAgain.setError("Cannot empty");
                    viewFocus = mEditTextNewPasswordAgain;
                    viewFocus.requestFocus();
                }

            } else if (!mEditTextNewPassword.getText().toString()
                    .equals(mEditTextNewPasswordAgain.getText().toString())) {

                mEditTextNewPasswordAgain.setError("New Password doesn't match");
                viewFocus = mEditTextNewPasswordAgain;
                viewFocus.requestFocus();

            } else if (!mEditTextOldPassword.getText().toString().isEmpty() && !mEditTextNewPassword.getText().toString().isEmpty()
                    && !mEditTextNewPasswordAgain.getText().toString().isEmpty()) {

                new CheckUserLoginAsyncTask(ProfileActivity.this,
                        sharedPreferences.getString(SHARED_PREF_USER_EMAIL, ""), mEditTextOldPassword.getText().toString()).execute();
            }

        } else {
            shortToast(getApplicationContext(), ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    @Override
    public void checkUserLoginRespond(boolean response) {

        if (response) {
            new ChangePasswordAsyncTask(ProfileActivity.this, sharedPreferences.getInt(SHARED_PREF_USER_ID, 0),
                    mEditTextNewPassword.getText().toString()).execute();
        } else {
            mEditTextOldPassword.setError("Current Password incorrect");
            viewFocus = mEditTextOldPassword;
            viewFocus.requestFocus();
        }
    }

    @Override
    public void changePasswordRespond(boolean response) {

        if (response) {
            dialog.dismiss();
            snackBar(mCoordinatorLayout, "Password update successfully");
        } else {
            dialog.dismiss();
            snackBar(mCoordinatorLayout, "Error on updating");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_save) {

            if (isNetworkAvailable(this)){
                updateData();
            } else {
                snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
            }

        } else if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
