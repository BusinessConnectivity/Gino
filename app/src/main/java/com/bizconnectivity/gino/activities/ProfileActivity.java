package com.bizconnectivity.gino.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.RetrieveUserAsyncTask;
import com.bizconnectivity.gino.asynctasks.UpdateUserAsyncTask;
import com.bizconnectivity.gino.models.UserModel;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format2;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, RetrieveUserAsyncTask.AsyncResponse,
        UpdateUserAsyncTask.AsyncResponse{

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

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private Realm realm;
    private RealmResults<UserModel> user;
    private Calendar now = Calendar.getInstance();

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

        // Initial Realm
        realm = Realm.getDefaultInstance();
        user = realm.where(UserModel.class).findAll();

        mTextViewBirthday.setText("01 / 01 / 1990");

        String[] gender = {"MALE", "FEMALE"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, gender);
        mSpinnerGender.setAdapter(adapterGender);

        String[] location = {"Singapore", "Malaysia", "Indonesia"};
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, location);
        maSpinnerLocation.setAdapter(adapterLocation);

        fetchData();
    }

    private void fetchData(){

        mProgressBar.setVisibility(View.VISIBLE);

        if (isNetworkAvailable(this)) {
            new RetrieveUserAsyncTask(this, user.get(0).getUserEmail()).execute();
        } else {
            updateUI();
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    private void updateUI (){

        if (user.get(0).getUserName() != null)
            mEditTextName.setText(user.get(0).getUserName());
        if (user.get(0).getUserDOB() != null) {
            try {
                mTextViewBirthday.setText(format2.format(format1.parse(user.get(0).getUserDOB())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (user.get(0).getUserGender() != null)
            mSpinnerGender.setText(user.get(0).getUserGender().toUpperCase());

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void retrieveUserDetail(final UserModel userModel) {

        if (userModel != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.copyToRealmOrUpdate(userModel);
                }
            });
        }

        updateUI();
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

        new MaterialDialog.Builder(this)
                .title("Change Password")
                .customView(R.layout.custom_view, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .show();
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateData() {

        mProgressBar.setVisibility(View.VISIBLE);

        String email = user.get(0).getUserEmail();
        String name = mEditTextName.getText().toString();
        String gender = mSpinnerGender.getText().toString();
        String dob = mTextViewBirthday.getText().toString();

        new UpdateUserAsyncTask(this, email, name, gender, dob).execute();

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateUserResponse(boolean response) {

        if (response) {
            snackBar(mCoordinatorLayout, "Update Successfully");
        } else {
            snackBar(mCoordinatorLayout, "Update Failed");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
