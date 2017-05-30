package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.bizconnectivity.gino.models.UserModel;
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
import io.realm.Realm;

import static com.bizconnectivity.gino.Common.isEmailValid;
import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, CreateUserAsyncTask.AsyncResponse,
        CheckUserAsyncTask.AsyncResponse, CheckUserEmailAsyncTask.AsyncResponse, CheckUserLoginAsyncTask.AsyncResponse, RetrieveUserAsyncTask.AsyncResponse{

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

    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    boolean isActivityStarted = false;
    SharedPreferences sharedPreferences;
    Realm realm;
    View focusView;
    Snackbar snackbar;

    private String name = "";
    private String password = "";
    private String email = "";
    private String gender = "";
    private String dob = "";
    private String facebookId = "";
    private String googleId = "";
    private String photoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Layout Binding
        ButterKnife.bind(this);

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Initial Realm
        realm = Realm.getDefaultInstance();

        // Initialize & handle Facebook Sign In
        facebookSignIn();

        // Initialize Google Sign In
        initializeGoogle();
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
                showSnackBar("Facebook Sign In Cancelled");
            }

            @Override
            public void onError(FacebookException error) {

                mProgressBar.setVisibility(View.GONE);
                showSnackBar("Authentication Failed");
            }
        });
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

    // Google Sign In Result
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
                    new CheckUserAsyncTask(this, email).execute();
                }
            }

        } else {
            // Signed out, show unauthenticated UI.
            mProgressBar.setVisibility(View.GONE);
            showSnackBar("Authentication Failed");
        }
    }

    // Google & Facebook email checking Callback
    @Override
    public void checkUserRespond(boolean response) {

        if (response) {
            new CreateUserAsyncTask(this, name, password, email, gender, dob, facebookId, googleId, photoUrl).execute();
        } else {
            updateIsSignedIn();
            new RetrieveUserAsyncTask(this, email).execute();
        }
    }

    // Normal email checking Callback
    @Override
    public void checkUserEmailRespond(boolean response) {
        if (response) {
            new CheckUserLoginAsyncTask(this, mEmail.getText().toString(), mPassword.getText().toString()).execute();
        } else {
            mProgressBar.setVisibility(View.GONE);
            showSnackBar("This account doesn't exist");
        }
    }

    // Check User Login
    @Override
    public void checkUserLoginRespond(boolean response) {

        if (response) {
            updateIsSignedIn();
            new RetrieveUserAsyncTask(this, mEmail.getText().toString()).execute();
        } else {
            mProgressBar.setVisibility(View.GONE);
            showSnackBar("Password Incorrect");
        }
    }

    // Create New User Account Callback
    @Override
    public void createUserResponse(boolean response) {

        if (response) {
            updateIsSignedIn();
            new RetrieveUserAsyncTask(this, email).execute();
        } else {
            showSnackBar("Sign In failed");
        }
    }

    // Retrieve User Detail Callback
    @Override
    public void retrieveUserDetail(final UserModel response) {

        if (response != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    UserModel userModel = realm.createObject(UserModel.class, response.getUserID());
                    if (response.getUserEmail() != null) userModel.setUserEmail(response.getUserEmail());
                    if (response.getUserName() != null) userModel.setUserName(response.getUserName());
                    if (response.getUserDOB() != null) userModel.setUserDOB(response.getUserDOB());
                    if (response.getUserGender() != null) userModel.setUserGender(response.getUserGender());
                    if (response.getFacebookID() != null) userModel.setFacebookID(response.getFacebookID());
                    if (response.getGoogleID() != null) userModel.setGoogleID(response.getGoogleID());
                    if (response.getPhotoFile() != null) userModel.setPhotoFile(response.getPhotoFile());
                    if (response.getPhotoName() != null) userModel.setPhotoName(response.getPhotoName());
                    if (response.getPhotoExt() != null) userModel.setPhotoExt(response.getPhotoExt());
                    if (response.getPhotoUrl() != null) userModel.setPhotoUrl(response.getPhotoUrl());

                    realm.copyToRealmOrUpdate(userModel);
                }
            });

            mProgressBar.setVisibility(View.GONE);
            navigateToMain();
        }
    }

    // Update shared preference signed in
    private void updateIsSignedIn() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_IS_SIGNED_IN, true).apply();
    }

    // Navigate to main activity
    private void navigateToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        isActivityStarted = true;
        startActivity(intent);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        mProgressBar.setVisibility(View.GONE);
        showSnackBar("Google Play Services Error");
    }

    @OnClick(R.id.button_sign_in)
    public void buttonSignInOnClick(View view) {

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {

            showSnackBar("Email can't be empty");
            focusView = mEmail;
            focusView.requestFocus();

        } else if (TextUtils.isEmpty(password)) {

            showSnackBar("Password can't be empty");
            focusView = mPassword;
            focusView.requestFocus();

        } else if (!isEmailValid(email)) {

            showSnackBar("Invalid Email Format");
            focusView = mEmail;
            focusView.requestFocus();

        } else {

            if (isNetworkAvailable(this)) {
                mProgressBar.setVisibility(View.VISIBLE);
                new CheckUserEmailAsyncTask(this, email).execute();
            } else {
                showSnackBar("No Internet Connection");
            }
        }
    }

    @OnClick(R.id.text_forgot_password)
    public void forgotPasswordOnClick(View view) {

//        Intent intent = new Intent(this, ForgotPasswordActivity.class);
//        isActivityStarted = true;
//        startActivity(intent);
    }

    @OnClick(R.id.button_facebook)
    public void buttonFacebookOnClick (View view) {

        if (isNetworkAvailable(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            mButtonFacebookLogin.performClick();
        } else {
            showSnackBar("No Internet Connection");
        }
    }

    @OnClick(R.id.button_google)
    public void buttonGoogleOnClick(View view) {

        if (isNetworkAvailable(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            mButtonGoogleLogin.performClick();
            googleSignIn();
        } else {
            showSnackBar("No Internet Connection");
        }
    }

    @OnClick(R.id.text_sign_up)
    public void signUpOnClick(View view) {

        Intent intent = new Intent(this, SignUpActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    // Display Snackbar
    private void showSnackBar(String message){
        snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT);
        snackbar.show();
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
        if (!realm.isClosed()) realm.close();
        if (isActivityStarted) finish();
    }
}
