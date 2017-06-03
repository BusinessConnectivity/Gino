package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.CheckUserAsyncTask;
import com.bizconnectivity.gino.asynctasks.CheckUserEmailAsyncTask;
import com.bizconnectivity.gino.asynctasks.CheckUserLoginAsyncTask;
import com.bizconnectivity.gino.asynctasks.CreateUserAsyncTask;
import com.bizconnectivity.gino.asynctasks.RetrieveUserAsyncTask;
import com.bizconnectivity.gino.models.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.isEmailValid;
import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.LOGIN_EMAIL;
import static com.bizconnectivity.gino.Constant.LOGIN_FACEBOOK;
import static com.bizconnectivity.gino.Constant.LOGIN_GOOGLE;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_EMAIL;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_ID;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_SIGN_IN_TYPE;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, CreateUserAsyncTask.AsyncResponse,
        CheckUserAsyncTask.AsyncResponse, CheckUserEmailAsyncTask.AsyncResponse, CheckUserLoginAsyncTask.AsyncResponse,
        RetrieveUserAsyncTask.AsyncResponse{

    @BindView(R.id.button_facebook_login)
    LoginButton mButtonFacebookLogin;

    @BindView(R.id.button_google_login)
    SignInButton mButtonGoogleLogin;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.edit_email)
    TextInputEditText mEmail;

    @BindView(R.id.edit_password)
    TextInputEditText mPassword;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private boolean isActivityStarted = false;
    private SharedPreferences sharedPreferences;
    private View focusView;
    private String name = "";
    private String password = "";
    private String email = "";
    private String gender = "";
    private String dob = "";
    private String facebookId = "";
    private String googleId = "";
    private String photoUrl = "";
    private String signInType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Layout Binding
        ButterKnife.bind(this);

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Initialize Facebook Sign In
        facebookSignIn();

        // Initialize Google Sign In
        initializeGoogle();
    }

    //region Facebook Sign In
    @OnClick(R.id.button_facebook)
    public void buttonFacebookOnClick (View view) {

        if (isNetworkAvailable(this)) {

            mProgressBar.setVisibility(View.VISIBLE);
            mButtonFacebookLogin.performClick();

        } else {
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    private void facebookSignIn() {

        mCallbackManager = CallbackManager.Factory.create();
        mButtonFacebookLogin.setReadPermissions("email", "public_profile");

        mButtonFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // Retrieve User Details
                final GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {

                                    password = "";
                                    googleId = "";
                                    facebookId = object.isNull("id") ? null : object.getString("id");
                                    name = object.isNull("name") ? null : object.getString("name");
                                    dob = object.isNull("birthday") ? null : object.getString("birthday");
                                    gender = object.isNull("gender") ? null : object.getString("gender");
                                    email = object.isNull("email") ? null : object.getString("email");
                                    photoUrl = object.isNull("picture") ? null : object.getJSONObject("picture").getJSONObject("data").getString("url");

                                    if (!email.isEmpty() && email != null) {
                                        signInType = LOGIN_FACEBOOK;
                                        new CheckUserAsyncTask(SignInActivity.this, email).execute();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,birthday,gender,email,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                mProgressBar.setVisibility(View.GONE);
                snackBar(mCoordinatorLayout, "Facebook Sign In Cancelled");
            }

            @Override
            public void onError(FacebookException error) {

                mProgressBar.setVisibility(View.GONE);
                snackBar(mCoordinatorLayout, "Authentication Failed");
            }
        });
    }
    //endregion

    // region Google Sign In
    @OnClick(R.id.button_google)
    public void buttonGoogleOnClick(View view) {

        if (isNetworkAvailable(this)) {

            mProgressBar.setVisibility(View.VISIBLE);
            mButtonGoogleLogin.performClick();
            googleSignIn();

        } else {
            snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
        }
    }

    private void initializeGoogle() {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleGoggleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount googleAcc = result.getSignInAccount();

            if (googleAcc != null) {

                name = googleAcc.getDisplayName() == null ? null : googleAcc.getDisplayName();
                password = "";
                email = googleAcc.getEmail() == null ? null : googleAcc.getEmail();
                gender = "";
                dob = "";
                facebookId = "";
                googleId = googleAcc.getId() == null ? null : googleAcc.getId();
                photoUrl = googleAcc.getPhotoUrl() == null ? null : googleAcc.getPhotoUrl().toString();

                if (!email.isEmpty() && email != null) {
                    signInType = LOGIN_GOOGLE;
                    new CheckUserAsyncTask(this, email).execute();
                }
            }

        } else {
            // Signed out, show unauthenticated UI.
            mProgressBar.setVisibility(View.GONE);
            snackBar(mCoordinatorLayout, "Authentication Failed");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        mProgressBar.setVisibility(View.GONE);
        snackBar(mCoordinatorLayout, "Google Play Services Error");
    }
    // endregion

    // Google & Facebook email checking Callback
    @Override
    public void checkUserRespond(boolean response) {

        if (response) {
            new CreateUserAsyncTask(this, name, password, email, gender, dob, facebookId, googleId, photoUrl).execute();
        } else {
            new RetrieveUserAsyncTask(this, email).execute();
        }
    }

    // Normal email checking Callback
    @Override
    public void checkUserEmailRespond(boolean response) {

        if (!response) {
            new CheckUserLoginAsyncTask(this, mEmail.getText().toString(), mPassword.getText().toString()).execute();
        } else {
            mProgressBar.setVisibility(View.GONE);
            snackBar(mCoordinatorLayout, "This account doesn't exist");
        }
    }

    // Verify User Login
    @Override
    public void checkUserLoginRespond(boolean response) {

        if (response) {
            new RetrieveUserAsyncTask(this, mEmail.getText().toString()).execute();
        } else {
            mProgressBar.setVisibility(View.GONE);
            snackBar(mCoordinatorLayout, "Password Incorrect");
        }
    }

    // Create New User Account Callback
    @Override
    public void createUserResponse(boolean response) {

        if (response) {
            new RetrieveUserAsyncTask(this, email).execute();
        } else {
            snackBar(mCoordinatorLayout, "Sign In failed");
        }
    }

    // Retrieve User Detail Callback
    @Override
    public void retrieveUserDetail(User user) {

        if (user != null) updateIsSignedIn(user.getUserID(), user.getUserEmail());

        mProgressBar.setVisibility(View.GONE);
        finish();
    }

    // Update shared preference signed in
    private void updateIsSignedIn(int userId, String userEmail) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SHARED_PREF_USER_ID, userId).apply();
        editor.putString(SHARED_PREF_USER_EMAIL, userEmail).apply();
        editor.putString(SHARED_PREF_USER_SIGN_IN_TYPE, signInType).apply();
        editor.putBoolean(SHARED_PREF_IS_SIGNED_IN, true).apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoggleSignInResult(result);
        }
    }

    @OnClick(R.id.button_sign_in)
    public void buttonSignInOnClick(View view) {

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {

            snackBar(mCoordinatorLayout, "Email can't be empty");
            focusView = mEmail;
            focusView.requestFocus();

        } else if (TextUtils.isEmpty(password)) {

            snackBar(mCoordinatorLayout, "Password can't be empty");
            focusView = mPassword;
            focusView.requestFocus();

        } else if (!isEmailValid(email)) {

            snackBar(mCoordinatorLayout, "Invalid Email Format");
            focusView = mEmail;
            focusView.requestFocus();

        } else {

            if (isNetworkAvailable(this)) {
                mProgressBar.setVisibility(View.VISIBLE);
                signInType = LOGIN_EMAIL;
                new CheckUserEmailAsyncTask(this, email).execute();
            } else {
                snackBar(mCoordinatorLayout, "No Internet Connection");
            }
        }
    }

    @OnClick(R.id.text_forgot_password)
    public void forgotPasswordOnClick(View view) {
    }

    @OnClick(R.id.text_sign_up)
    public void signUpOnClick(View view) {

        Intent intent = new Intent(this, SignUpActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SplashActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isActivityStarted) finish();
    }
}
